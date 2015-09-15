package com.rf.test.website;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.HtmlLogger;
import com.rf.test.base.RFBaseTest;

/**
 * @author ShubhamMathur RFWebsiteBaseTest is the super class for all
 *         desktop Test classes initializes the driver is used for execution.
 *
 */
public class RFWebsiteBaseTest extends RFBaseTest {
 StringBuilder verificationErrors = new StringBuilder();
 protected String password = null;

 protected RFWebsiteDriver driver = new RFWebsiteDriver(propertyFile);
 private static final Logger logger = LogManager
   .getLogger(RFWebsiteBaseTest.class.getName());

 /**
  * @throws Exception
  *             setup function loads the driver and environment and baseUrl
  *             for the tests
  */
 @BeforeSuite(alwaysRun=true)
 public void setUp() throws Exception {
  driver.loadApplication();  
  logger.info("Application loaded");    
  driver.setDBConnectionString();  
 }

 @BeforeMethod(alwaysRun=true)
 public void beforeMethod(){
  driver.get(driver.getURL()+"/"+driver.getCountry());
  setPassword(driver.getPassword());
  try{
   logout();
  }catch(NoSuchElementException e){

  }  
 }

 /**
  * @throws Exception
  */
 @AfterSuite(alwaysRun = true)
 public void tearDown() throws Exception {
  new HtmlLogger().createHtmlLogFile();  
  driver.quit();
 }

 public void setPassword(String pass){
  password=pass;
 }
 
 public void logout(){
  driver.findElement(By.xpath("//li[@id='account-info-button']")).click();
  driver.waitForElementPresent(By.linkText("Log out"));
  driver.findElement(By.linkText("Log out")).click();
  logger.info("Logout");  
 }
 public void crmLogout(){
		driver.findElement(By.id("userNavLabel")).click();
		driver.waitForElementPresent(By.id("app_logout"));
		driver.findElement(By.id("app_logout")).click();
		logger.info("Logout");
		
	}

 // This assertion for the UI Texts
 public void assertTrue(String message, boolean condition) {
  if (!condition) {
   logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
     + message + "]");   
   Assert.fail(message);    
  }

 }


 //This assertion for the Database validations
 public boolean assertTrueDB(String message, boolean condition,String dbName) {
  if (!condition) {
   logger.info("[DATABASE ASSERTION FAILURE -  "+dbName+" ----------- " +message + "]");
   if(!dbName.equals(driver.getDBNameRFL())){
    Assert.fail(message);
   }
   else{
    return false;
   }
  }
  return true;
 }

 public void assertTrue(boolean condition, String message) {

  if (!condition) {
   logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
     + message + "]");
   Assert.fail(message);
  }
 }

 public void assertEquals(Object obj1, Object obj2, String message) {
  if (!obj1.equals(obj2)) {
   logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
     + message + "]");
   Assert.fail(message);
  }
 }

 public boolean assertEqualsDB(Object obj1, Object obj2, String message,String dbName) {
  if (!obj1.equals(obj2)) {
   logger.info("[DATABASE ASSERTION FAILURE -  "+dbName+" ----------- " +message + "]");
   if(!dbName.equals(driver.getDBNameRFL())){
    Assert.fail(message);
   } 
   else{
    return false;
   }
  }
  return true;  
 }

 public boolean assertEqualsDB(String message, int num1,int num2,String dbName) {
  if (!(num1==num2)) {
   logger.info("[RFL DATABASE ASSERTION FAILURE -  "+message + "]");
   if(!dbName.equals(driver.getDBNameRFL())){
    Assert.fail(message);
   }
   else{
    return false;
   }
  }
  return true;
 }

 public void assertEquals(String message, int num1,int num2) {
  if (!(num1==num2)) {
   logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
     + message + "]");
   Assert.fail(message);
  }
 }

 public void assertFalse(boolean condition, String message) {

  if (condition) {
   logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
     + message + "]");
   Assert.fail(message);
  }
 }

 public void assertFalse(String message, boolean condition) {

  if (condition) {
   logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
     + message + "]");
   Assert.fail(message);
  }
 }

 // public void assertTrue(boolean condition) {
 //
 //  try {
 //   assertTrue(condition);
 //
 //  } catch (Exception e) {
 //   logger.trace(e.getMessage());
 //  }
 // }

 public void assertEquals(String message, float num1,float num2) {

  if (!(num1==num2)) {
   logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
     + message + "]");
   Assert.fail(message);
  }
 }


 public Object getValueFromQueryResult(List<Map<String, Object>> userDataList,String column){
  Object value = null;
  for (Map<String, Object> map : userDataList) {
   logger.info("query result:" + map.get(column));
   value = map.get(column);   
  }
  return value;
 }
 
 public List<String> getValuesFromQueryResult(List<Map<String, Object>> userDataList,String column){
  List<String> allReturnedValuesFromQuery = new ArrayList<String>();
  Object value = null;
  for (Map<String, Object> map : userDataList) {
   logger.info("query result:" + map.get(column));
   value = map.get(column);
   allReturnedValuesFromQuery.add(String.valueOf(value));
  }
  return allReturnedValuesFromQuery;
 }

}