
/* Autoship Clean Up Validation Scripts */

--  8.Multiple Active Autoships_Cleanup

/* Checking Templates made Active for Inactive*/
SELECT  COUNT(*) -- Should be NULL 
FROM    RFOperations.Hybris.Autoship a
WHERE   CAST(a.ServerModifiedDate AS DATE) = CAST(GETDATE() AS DATE)
        AND a.Active = 1; 


		/* Checking if Any Accounts have Multiple Active Templates type */

SELECT  a.AccountID ,
        a.AutoshipTypeID AS AS_Type ,
        COUNT(*) AS Counts
FROM    RFOperations.Hybris.Autoship a
        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
WHERE   a.CountryID = 236
        AND ab.AccountTypeID = 2
        AND a.AutoshipTypeID = 1
        AND a.Active = 1
GROUP BY a.AccountID ,
        a.AutoshipTypeID
HAVING  COUNT(*) > 1
UNION ALL
SELECT  a.AccountID ,
        a.AutoshipTypeID AS_Type ,
        COUNT(*) AS Counts
FROM    RFOperations.Hybris.Autoship a
        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
WHERE   a.CountryID = 236
        AND ab.AccountTypeID = 1
        AND a.AutoshipTypeID = 2
        AND a.Active = 1
GROUP BY a.AccountID ,
        a.AutoshipTypeID
HAVING  COUNT(*) > 1
UNION ALL
SELECT  a.AccountID ,
        a.AutoshipTypeID AS_Type ,
        COUNT(*) AS Counts
FROM    RFOperations.Hybris.Autoship a
        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
WHERE   a.CountryID = 236
        AND ab.AccountTypeID = 1
        AND a.AutoshipTypeID = 3
        AND a.Active = 1
GROUP BY a.AccountID ,
        a.AutoshipTypeID
HAVING  COUNT(*) > 1;
		


		/* Checking if updated Acive those templates having Recent Orders. */



WITH    Autoship
          AS ( SELECT   a.AutoshipID ,
                        a.AccountID ,
                        a.AutoshipTypeID
               FROM     RFOperations.Hybris.Autoship a
               WHERE    CAST(a.ServerModifiedDate AS DATE) = CAST(GETDATE() AS DATE)
                        AND a.Active = 0
             ),
        MAXAutoDate
          AS ( SELECT   a.AccountID ,
                        a.AutoshipID ,
                        MAX(o.CompletionDate) AS MaxOrderCompletion
               FROM     RFOperations.Hybris.Autoship a
                        JOIN Autoship b ON b.AccountID = a.AccountID
                                           AND a.AutoshipTypeID = b.AutoshipTypeID
                        LEFT JOIN RFOperations.Hybris.Orders o ON o.AutoShipID = a.AutoshipID
               GROUP BY a.AccountID ,
                        a.AutoshipID
             ),
			 Selected AS 
   (SELECT  a.AutoshipID ,
            a.AccountID ,
            a.AutoshipTypeID ,
            a.Active ,
            b.MaxOrderCompletion ,
            ROW_NUMBER() OVER ( PARTITION BY a.AccountID ORDER BY b.MaxOrderCompletion DESC ) AS rown
    FROM    RFOperations.Hybris.Autoship a
            JOIN MAXAutoDate b ON a.AutoshipID = b.AutoshipID
    WHERE   a.Active <> 1)
	SELECT * FROM Selected WHERE (Selected.Active<>1
	AND rown=1) OR (Selected.Active=1 AND rown<>1)
   



		
		
		
		
			












