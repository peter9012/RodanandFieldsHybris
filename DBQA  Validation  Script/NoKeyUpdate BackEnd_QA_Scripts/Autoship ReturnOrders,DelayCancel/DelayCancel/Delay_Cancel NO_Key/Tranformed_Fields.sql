 USE RFOperations;
SET STATISTICS TIME ON;
GO

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;


		DECLARE @StartedTime TIME;
DECLARE @EndTime TIME; 

SELECT  'Autoship Header Loading TempTable ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);

		 DECLARE @HYB_key VARCHAR(100) = 'P_rfaccountId';
        DECLARE @RFO_key VARCHAR(100) = 'AccountId';
        DECLARE @sql_gen_1 NVARCHAR(MAX);
        DECLARE @cnt INT;
        DECLARE @lt_1 INT;
        DECLARE @lt_2 INT;
        DECLARE @temp TABLE
            (
              test_area VARCHAR(MAX) ,
              test_type VARCHAR(MAX) ,
              rfo_column VARCHAR(MAX) ,
              hybris_column VARCHAR(MAX) ,
              hyb_key VARCHAR(MAX) ,
              hyb_value VARCHAR(MAX) ,
              rfo_key VARCHAR(MAX) ,
              rfo_value VARCHAR(MAX)
            );
	   IF OBJECT_ID('TempDB..#Tempact') IS NOT NULL
            DROP TABLE #Tempact;			
	
        SELECT DISTINCT adcl.accountId INTO    #Tempact
        FROM    Logging.autoshipDelayCancellationLog adcl
                JOIN Hybris..users u ON CAST(adcl.accountId AS NVARCHAR) = u.p_rfaccountid
                JOIN Hybris..countries c ON u.p_country = c.PK
                                            AND c.isocode = 'US'
                LEFT JOIN Hybris..orders o ON o.code = adcl.templateId AND o.p_template=1
				JOIN Hybris..autoshiplogs at ON at.p_rfaccountid=adcl.accountId
        WHERE   adcl.[status] IS NOT NULL;


		CREATE CLUSTERED INDEX cls1 ON #Tempact (accountId)
		  
			
SELECT  'Autoship  DelayCancel Templtable load is Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Autoship DelayCancel Temp Table Loading ' AS Entity; 
		
		

		SELECT  'Autoship DelayCancel Transformed Columns Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
		SELECT  @StartedTime=CAST(GETDATE() AS TIME)

		SET @cnt = 1;
            SELECT  @lt_1 = COUNT(*)
            FROM    [DataMigration].[dbo].[map_tab]
            WHERE   flag = 'manual'
                    AND rfo_column <> @RFO_key
                    AND [owner] = '985-ASDC'; 

            WHILE @cnt <= @lt_1
                BEGIN




                    SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner]
                            + ''', ''' + flag + ''', ''' + RFO_Column
                            + ''' as rfo_column, ''' + Hybris_Column
                            + ''' as hybris_column, A.' + @HYB_key
                            + ' as hyb_key, A.' + Hybris_Column
                            + ' as hyb_value, B.' + @RFO_key
                            + ' as rfo_key, B.' + RFO_Column + ' as rfo_value

	FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                            + Hybris_Table + ' a, #tempact b where a.'
                            + @HYB_key + '=b.' + @RFO_key + '
	except
	SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM ' + RFO_Table
                            + ', #tempact b where a.' + @RFO_key + '=b.'
                            + @RFO_key + ') A  

	LEFT JOIN

	(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM ' + RFO_Table
                            + ' , #tempact b where a.' + @RFO_key + '=b.'
                            + @RFO_key + '
	except
	SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                            + Hybris_Table + ' a, #tempact b where a.'
                            + @HYB_key + '=b.' + @RFO_key + ') B
	ON A.' + @HYB_key + '=B.' + @RFO_key + '
	UNION
	SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', ''' + RFO_Column
                            + ''', ''' + Hybris_Column + ''', A.' + @HYB_key
                            + ', A.' + Hybris_Column + ', B.' + @RFO_key
                            + ',B.' + RFO_Column + '

	FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                            + Hybris_Table + ' a, #tempact b where a.'
                            + @HYB_key + '=b.' + @RFO_key + '
	except
	SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM ' + RFO_Table
                            + ' , #tempact b where a.' + @RFO_key + '=b.'
                            + @RFO_key + ') A  

	RIGHT JOIN

	(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM ' + RFO_Table
                            + ' , #tempact b where a.' + @RFO_key + '=b.'
                            + @RFO_key + '
	except
	SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                            + Hybris_Table + ' a, #tempact b where a.'
                            + @HYB_key + '=b.' + @RFO_key + ') B
	ON A.' + @HYB_key + '=B.' + @RFO_key + ''
                    FROM    ( SELECT    * ,
                                        ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                              FROM      [DataMigration].[dbo].[map_tab]
                              WHERE     flag = 'manual'
                                        AND rfo_column <> @RFO_key
                                        AND [owner] = '985-ASDC'
                            ) temp
                    WHERE   rn = @cnt;

                    PRINT @sql_gen_1;


                    INSERT  INTO @temp
                            ( test_area ,
                              test_type ,
                              rfo_column ,
                              hybris_column ,
                              hyb_key ,
                              hyb_value ,
                              rfo_key ,
                              rfo_value
                            )
                            EXEC sp_executesql @sql_gen_1;



	/* Added this code to get Counting the total of defects.****/

                    IF ( SELECT COUNT(*)
                         FROM   @temp
                       ) > 1
                        BEGIN
						  DECLARE @err_cnt INT;
                   
                            SELECT  @err_cnt = COUNT(DISTINCT hyb_key)
                            FROM    @temp;

                            UPDATE  a
                            SET     [prev_run_err] = @err_cnt
                            FROM    DataMigration.dbo.map_tab a ,
                                    @temp b
                            WHERE   a.hybris_column = b.hybris_column
                                    AND [owner] = '985-ASDC'; 
                        END;   

                    INSERT  INTO [DataMigration].[dbo].[dm_log]
                            SELECT TOP 5
                                    test_area ,
                                    test_type ,
                                    rfo_column ,
                                    hybris_column ,
                                    hyb_key ,
                                    hyb_value ,
                                    rfo_key ,
                                    rfo_value
                            FROM    @temp
                            WHERE   ( ( COALESCE(hyb_key, '~') = COALESCE(rfo_key,
                                                              '~') )
                                      AND ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                                    )
                            UNION
                            SELECT TOP 5
                                    test_area ,
                                    test_type ,
                                    rfo_column ,
                                    hybris_column ,
                                    hyb_key ,
                                    hyb_value ,
                                    rfo_key ,
                                    rfo_value
                            FROM    @temp
                            WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key,
                                                              '~') )
                                      OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                                    );

                    DELETE  FROM @temp;

                    SET @cnt = @cnt + 1;

                END;
   

        UPDATE  DataMigration.dbo.map_tab
        SET     [prev_run_err] = 0
        WHERE   [owner] = '985-ASDC'
                AND flag = 'Manual'
                AND Hybris_column NOT IN ( SELECT DISTINCT
                                                    hybris_column
                                           FROM     DataMigration.dbo.dm_log
                                           WHERE    test_area = '985-ASDC'
                                                    AND test_type = 'Manual' );
													
						
SELECT  'Autoship DelayCancel Transformed Columns Validation Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Autoship DelayCancel Trnsformed Columns Validation Completed' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Autoship DelayCancel ' AS Entity ,
                'Transformed' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;