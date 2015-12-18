USE DataMigration
Go
CREATE PROCEDURE Migration.Migration_Hybris_OrderItem_QA @LastRun DATETIME = '2014-05-01'
AS
    BEGIN 
        SET NOCOUNT ON

        SET ANSI_WARNINGS OFF 

        SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED

---------------------------------------------------------------------------------------------


        DECLARE @Country NVARCHAR(20)= 'US';
        DECLARE @ServerMod DATETIME = '2014-05-01' ,
            @RFOCount BIGINT ,
            @RowCount BIGINT ,
            @HybrisCount BIGINT 
        DECLARE @RFOCountry INT = ( SELECT  CountryID
                                    FROM    RFOperations.RFO_Reference.Countries (NOLOCK)
                                    WHERE   Alpha2Code = @Country
                                  ) ,
            @HybCountry BIGINT = ( SELECT   PK
                                   FROM     Hybris.dbo.currencies (NOLOCK)
                                   WHERE    isocode = 'USD'
                                 );
        DECLARE @ReturnOrderType BIGINT = ( SELECT  PK
                                            FROM    Hybris.dbo.composedtypes (NOLOCK)
                                            WHERE   InternalCode = 'RFReturnOrder'
                                          );


-----------------------------------------------------------------------------------------------------------------------
--Orderitem counts HYbris & RFO

        SELECT  @RFOCount = 
		COUNT(DISTINCT oi.OrderItemID)
        FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
                INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
				INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
                INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR) AND U.P_SOURCENAME='Hybris-DM'
                INNER JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
                --INNER JOIN hybris..products p ON p.p_rflegacyproductid = oi.ProductID
                --INNER JOIN Hybris..orders ho ON ho.code = o.OrderNumber
                LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
        WHERE   o.CountryID = @RFOCountry
                AND a.autoshipid IS NULL
                AND od.startdate >= @ServerMod
				AND od.ServerModifiedDate> @LastRun
                --AND p.p_catalog = 8796093088344
                --AND p.p_catalogversion = 8796093153881

       

        SELECT  @HybrisCount = 
		COUNT(DISTINCT oe.PK)
        FROM    Hybris.dbo.orders o ( NOLOCK )
                INNER JOIN Hybris..orderentries oe ON oe.orderpk = o.pk AND O.userpk in (select pk from hybris..users where P_SOURCENAME='Hybris-DM')
        WHERE   ( p_template = 0
                  OR p_template IS NULL
                )
                AND o.TypePkString <> @ReturnOrderType
                AND currencypk = @HybCountry 
				AND oe.modifiedTS> @LastRun
        
		
        SELECT  'OrderItems' ,
                @RFOCount AS RFO_Count ,
                @HybrisCount AS Hybris_Count ,
                @RFOCount - @HybrisCount AS Diff
				
-------------------------------------------------------------------------------------------------------------------------
-- Missing 
 -----------------------------------------------------------------------------------------------------------------------------


        IF OBJECT_ID('DataMigration.Migration.OrderItemsMissing') IS NOT NULL
            DROP TABLE DataMigration.Migration.OrderItemsMissing;
			
	    IF OBJECT_ID('DATAMIGRATION.MIGRATION.DIFF_COMBO') IS NOT NULL
            DROP TABLE DATAMIGRATION.MIGRATION.DIFF_COMBO;
			
		IF OBJECT_ID('DATAMIGRATION.MIGRATION.RFO_COMBO') IS NOT NULL
            DROP TABLE DATAMIGRATION.MIGRATION.RFO_COMBO;
			
		IF OBJECT_ID('DATAMIGRATION.MIGRATION.HYB_COMBO') IS NOT NULL
            DROP TABLE DATAMIGRATION.MIGRATION.HYB_COMBO;



        SELECT CAST(O.ORDERNUMBER AS NVARCHAR(MAX))+'-'+CAST(OI.PRODUCTID AS NVARCHAR(MAX))+'-'+CAST(CAST(QUANTITY AS DECIMAL(30,8)) AS NVARCHAR(MAX))+'-'+CAST(CAST(TOTALPRICE AS DECIMAL(30,8)) AS NVARCHAR(MAX)) as OrderItemId
			INTO DATAMIGRATION.MIGRATION.RFO_COMBO
                  FROM      RFOperations.Hybris.Orders o WITH ( NOLOCK )
                            INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
							INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
                            INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR) AND U.P_SOURCENAME='Hybris-DM'
                            INNER JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
                            LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
                  WHERE     o.CountryID = @RFOCountry
                            AND a.autoshipid IS NULL
                            AND od.startdate >= @ServerMod
                            
                
        SELECT  CAST(o.code AS NVARCHAR(MAX))+'-'+CAST(p_rflegacyproductid AS NVARCHAR(MAX))+'-'+CAST(QUANTITY AS NVARCHAR(MAX))+'-'+CAST(oe.totalprice AS NVARCHAR(MAX)) as PK
			INTO DATAMIGRATION.MIGRATION.HYB_COMBO
                FROM      Hybris.dbo.orders o ( NOLOCK )
                          INNER JOIN Hybris..orderentries oe ON oe.orderpk = o.pk AND O.userpk in (select pk from hybris..users where P_SOURCENAME='Hybris-DM')
                WHERE     ( p_template = 0
							OR p_template IS NULL
                           )
                AND o.TypePkString <> @ReturnOrderType
                AND currencypk = @HybCountry
               
        SELECT *
		INTO  DATAMIGRATION.MIGRATION.DIFF_COMBO
		FROM  DATAMIGRATION.MIGRATION.RFO_COMBO
		EXCEPT
		SELECT * FROM DATAMIGRATION.MIGRATION.HYB_COMBO

        SELECT  @RowCount = COUNT(*)
        FROM    DataMigration.Migration.DIFF_COMBO;

        
              
            

--------------------------------------------------------------------------------------------------------------------------------------
--Duplicates 
--------------------------------------------------------------------------------------------------------------------------------------
/*
        IF OBJECT_ID('TEMPDB.dbo.#Items_Dups') IS NOT NULL
            DROP TABLE #Items_Dups;


        SELECT  a.OrderID ,
                ProductID ,
                LineItemNo ,
                COUNT(PK) AS Hybris_Duplicates
        INTO    #Items_Dups
        FROM    RFOperations.Hybris.OrderItem (NOLOCK) a
                JOIN Hybris.dbo.orderentries (NOLOCK) b ON a.OrderItemID = b.PK
        GROUP BY a.OrderID ,
                ProductID ,
                LineItemNo
        HAVING  COUNT(PK) > 1; 

        SELECT  @RowCount = COUNT(*)
        FROM    #Items_Dups;


        IF @RowCount > 0
            BEGIN 

                SELECT  'Duplicate ' + @Country + ' Items in Hybris' ,
                        @RowCount;

                SELECT  'OrderItemDuplicates' ,
                        *
                FROM    #Items_Dups;

            END;  
*/	
---------------------------------------------------------------------------------------------------------------------
--- Load OrderItem to temp tables
----------------------------------------------------------------------------------------------------------------------

        IF OBJECT_ID('TEMPDB.dbo.#Item') IS NOT NULL
            DROP TABLE #Item
        IF OBJECT_ID('TEMPDB.dbo.#Hybris_Item') IS NOT NULL
            DROP TABLE #Hybris_Item
        IF OBJECT_ID('TEMPDB.dbo.#RFO_Item') IS NOT NULL
            DROP TABLE #RFO_Item


        IF OBJECT_ID('TEMPDB.dbo.#LoadedOrderItems') IS NOT NULL
            DROP TABLE #LoadedOrderItems

        SELECT   CAST(o.code AS NVARCHAR(MAX))+'-'+CAST(p_rflegacyproductid AS NVARCHAR(MAX))+'-'+CAST(EntryNumber AS NVARCHAR(MAX)) AS PK
        INTO    #LoadedOrderItems
        FROM    Hybris.dbo.orders o ( NOLOCK )
                INNER JOIN Hybris..orderentries oe ON oe.orderpk = o.pk
				INNER JOIN hybris..products p ON p.PK=oe.productpk
        WHERE   ( p_template = 0
                  OR p_template IS NULL
                )
                AND O.TypePkString <> @ReturnOrderType
                AND currencypk = @HybCountry 

        
---------------------------------------------------------------------------------------------------------------------
--- Load Order Items 
----------------------------------------------------------------------------------------------------------------------

        SELECT  CAST (OrderItemID AS NVARCHAR(100)) AS OrderItemID ,
       -- CAST('[]' AS NVARCHAR(100)) AS DiscountValues ,
                CAST(a.TotalPrice AS NVARCHAR(100)) AS TotalPrice ,
                CAST(WholesalePrice AS NVARCHAR(100)) AS WholesalePrice ,
                CAST(a.CV AS NVARCHAR(100)) AS CV ,
                CAST(a.QV AS NVARCHAR(100)) AS QV ,
                CAST(TaxablePrice AS NVARCHAR(100)) AS TaxablePrice ,
                CAST(Quantity AS NVARCHAR(100)) AS Quantity ,
                CAST(BasePrice AS NVARCHAR(100)) AS BasePrice ,
                CAST(( LineItemNo - 1 ) AS NVARCHAR(100)) AS LineItemNo ,
                CAST(RetailProfit AS NVARCHAR(100)) AS RetailProfit

-- TotalTaxValues 

-------------------------------------------------------------------------------
--Derived Columns 
-------------------------------------------------------------------------------
                ,
                CAST (o.OrderID AS NVARCHAR(100)) AS OrderID ,
                CAST (SKU AS NVARCHAR(100)) AS SKU ,
                CAST (CONCAT('product "', SKU, '_', b.ProductID,
                             '" with Name "{}"') AS NVARCHAR(MAX)) AS Info ,
                CAST ('8796093054986' AS NVARCHAR(100)) AS UnitPK
        INTO    #RFO_Item
        FROM    RFOperations.Hybris.OrderItem a
				INNER JOIN Rfoperations.Hybris.orders o ON o.orderId=a.OrderID
				INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
                JOIN RFOperations.Hybris.ProductBase b ON a.ProductID = b.ProductID
        WHERE   EXISTS ( SELECT PK
                         FROM   #LoadedOrderItems loi
                         WHERE  loi.PK = o.OrderNumber+'-'+cast(a.productId as nvarchar)+'-'+cast(a.LineItemNo-1 as nvarchar))

        CREATE CLUSTERED INDEX MIX_RFItem ON #RFO_Item (OrderItemID)



        SELECT  1 ,
                GETDATE()


        SELECT  CAST (a.PK AS NVARCHAR(100)) AS PK ,
       -- CAST(discountvalues AS NVARCHAR(100)) AS discountvalues ,
                CAST(CAST(a.totalprice AS MONEY) AS NVARCHAR(100)) AS totalprice ,
                CAST(CAST(p_wholesaleprice AS MONEY) AS NVARCHAR(100)) AS p_wholesaleprice ,
                CAST(CAST (p_cv AS DECIMAL(20, 2)) AS NVARCHAR(100)) AS p_cv ,
                CAST(CAST(p_qv AS DECIMAL(20, 2)) AS NVARCHAR(100)) AS p_qv ,
                CAST(CAST(p_taxableprice AS MONEY) AS NVARCHAR(100)) AS p_taxableprice ,
                CAST(FLOOR(quantity) AS NVARCHAR(100)) AS quantity ,
                CAST(CAST (baseprice AS MONEY) AS NVARCHAR(100)) AS baseprice ,
                CAST(entrynumber AS NVARCHAR(100)) AS entrynumber ,
                CAST(CAST(p_retailprofit AS MONEY) AS NVARCHAR(100)) AS p_retailprofit

-- , CAST (taxvalues AS NVARCHAR(100)) AS taxvalues

-------------------------------------------------------------------------------
--Derived Columns 
-------------------------------------------------------------------------------
                ,
                CAST (orderpk AS NVARCHAR(100)) AS OrderPK ,
                CAST (p_catalogNumber AS NVARCHAR(100)) AS p_catalognumber ,
                CAST(info AS NVARCHAR(100)) AS info ,
                CAST(a.UnitPk AS NVARCHAR(100)) AS UnitPk
        INTO    #Hybris_Item
        FROM    Hybris.dbo.OrderEntries a
                JOIN Hybris.dbo.Products b ON a.productpk = b.pk
				INNER JOIN hybris..orders o ON O.PK=a.orderpk
        WHERE   EXISTS ( SELECT pk
                         FROM   #LoadedOrderItems loi
                         WHERE  CAST(o.code AS NVARCHAR(MAX))+'-'+CAST(B.p_rflegacyproductid AS NVARCHAR(MAX))+'-'+CAST(A.EntryNumber AS NVARCHAR(MAX)) = loi.pk )

        CREATE CLUSTERED INDEX MIX_HYItem ON #Hybris_Item (PK)

        SELECT  2 ,
                GETDATE()


        SELECT  *
        INTO    #Item
        FROM    #RFO_item
        EXCEPT
        SELECT  *
        FROM    #Hybris_Item

        SELECT  COUNT(*)
        FROM    #Item

         CREATE CLUSTERED INDEX MIX_Item ON #Item (OrderItemID)

-------------------------------------------------------------------------------------------------------------------------------------------




        TRUNCATE TABLE DataMigration.Migration.ErrorLog_Orders


        DECLARE @LastRUN DATETIME = '05/01/1901'

        DECLARE @I INT = ( SELECT   MIN(MapID)
                           FROM     DataMigration.Migration.Metadata_Orders
                           WHERE    HybrisObject = 'OrderEntries'
                         ) ,
            @C INT = ( SELECT   MAX(MapID)
                       FROM     DataMigration.Migration.Metadata_Orders
                       WHERE    HybrisObject = 'OrderEntries'
                     ) 


        DECLARE @DesKey NVARCHAR(50) 

        DECLARE @SrcKey NVARCHAR(50) 

        DECLARE @Skip BIT 

        WHILE ( @I <= @c )
            BEGIN 

                SELECT  @Skip = ( SELECT    Skip
                                  FROM      DataMigration.Migration.Metadata_Orders
                                  WHERE     MapID = @I
                                ); 


                IF ( @Skip = 1 )
                    SET @I = @I + 1;

                ELSE
                    BEGIN 
  


                --DECLARE @SrcCol NVARCHAR(50) = ( SELECT RFO_Column
                --                                 FROM   RFOperations.dbo.Metadata_Accounts
                --                                 WHERE  MapID = @I
                --                               )

                        DECLARE @DesTemp NVARCHAR(50)  = ( SELECT
                                                              CASE
                                                              WHEN HybrisObject = 'Orders'
                                                              THEN '#Hybris_Orders'
                                                              WHEN HybrisObject = 'OrderEntries'
                                                              THEN '#Hybris_Item'
                                                              WHEN HybrisObject = 'Consignments'
                                                              OR HybrisObject = 'ConsignmentEntries'
                                                              THEN '#Hybris_SPItem'
                                                              WHEN HybrisObject = 'PaymentInfos'
                                                              OR HybrisObject = 'PaymentTransaction'
                                                              THEN '#Hybris_Pay'
                                                              WHEN HybrisObject = 'paymnttrnsctentries'
                                                              THEN '#Hybris_Tran'
                                                              WHEN HybrisObject = 'Addresses_Billing'
                                                              THEN '#Hybris_BlAdr'
                                                              WHEN HybrisObject = 'Addresses'
                                                              THEN '#Hybris_ShAdr'
                                                              WHEN HybrisObject = 'OrderNotes'
                                                              THEN '#Hybris_Note'
                                                              END
                                                           FROM
                                                              DataMigration.Migration.Metadata_Orders
                                                           WHERE
                                                              MapID = @I
                                                         );  

                        DECLARE @DesCol NVARCHAR(50) = ( SELECT
                                                              Hybris_Column
                                                         FROM DataMigration.Migration.Metadata_Orders
                                                         WHERE
                                                              MapID = @I
                                                       )

                        SET @SrcKey = ( SELECT  RFO_Key
                                        FROM    DataMigration.Migration.Metadata_Orders
                                        WHERE   MapID = @I
                                      )

                        SET @DesKey = ( SELECT  'PK'
				 --CASE WHEN HybrisObject= 'Orders'
     --                                        THEN 'PK' END
                                        FROM    DataMigration.Migration.Metadata_Orders
                                        WHERE   MapID = @I
                                      ); 


                        DECLARE @SQL1 NVARCHAR(MAX) = ( SELECT
                                                              SqlStmt
                                                        FROM  DataMigration.Migration.Metadata_Orders
                                                        WHERE MapID = @I
                                                      )
                        DECLARE @SQL2 NVARCHAR(MAX) = ' 
 UPDATE A 
SET a.Hybris_Value = b. ' + @DesCol
                            + ' FROM DAtaMigration.Migration.ErrorLog_Orders a  JOIN '
                            + @DesTemp + ' b  ON a.RecordID= b.' + @DesKey
                            + ' WHERE a.MAPID = ' + CAST(@I AS NVARCHAR)



                        DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
                            ' INSERT INTO DAtaMigration.Migration.ErrorLog_Orders (Identifier,MapID,RecordID,RFO_Value) '
                            + @SQL1 + @SQL2

                        BEGIN TRY
                            EXEC sp_executesql @SQL3, N'@ServerMod DATETIME',
                                @ServerMod = @LastRun



                            SET @I = @I + 1
                            SELECT  @I ,
                                    GETDATE()

                        END TRY

                        BEGIN CATCH

                            SELECT  @SQL3

                            SET @I = @I + 1

                        END CATCH
                    END 

            END 



        SELECT  RFO_column ,
                a.mapid ,
                COUNT(*)
        FROM    DataMigration.Migration.ErrorLog_Orders a
                JOIN DataMigration.Migration.Metadata_orders b ON a.MapID = b.MapID
        GROUP BY a.MapID ,
                RFO_column


    END
