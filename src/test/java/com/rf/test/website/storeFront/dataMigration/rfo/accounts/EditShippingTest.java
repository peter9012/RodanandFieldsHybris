package com.rf.test.website.storeFront.dataMigration.rfo.accounts;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.StoreFrontCartAutoShipPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontShippingInfoPage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class EditShippingTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(EditShippingTest.class.getName());


	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private String RFO_DB = null;

	//Hybris Phase 2-4326: View shipping address on 'Shipping Profile' page
	@Test
	public void testShippingAddressOnShippingProfile_HP2_4326() throws InterruptedException, SQLException{
		RFO_DB = driver.getDBNameRFO();	

		int totalShippingAddressesFromDB = 0;
		List<Map<String, Object>> shippingAddressCountList =  null;
		List<Map<String, Object>> defaultShippingAddressList =  null;
		List<Map<String, Object>> randomConsultantList =  null;
		String shippingAddressName=null;
		String consultantEmailID = null;
		String accountID = null;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");

		//------------------The same number of billing addresses is shown in RFO and Front end----------------------------------------------------------------------------------------------------------------------------
		shippingAddressCountList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_COUNT_QUERY,consultantEmailID),RFO_DB);
		totalShippingAddressesFromDB = (Integer) getValueFromQueryResult(shippingAddressCountList, "count");			
		assertEquals("Shipping Addresses count on UI is different from DB", totalShippingAddressesFromDB,storeFrontShippingInfoPage.getTotalShippingAddressesDisplayed());			

		//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		if(totalShippingAddressesFromDB > 1){

			//---------------------------------Radio button is checked for the default shipping address on Front end as per RFO--------------------------------------------------------------------------------------------
			defaultShippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_DEFAULT_BILLING_ADDRESS_QUERY,consultantEmailID),RFO_DB);
			shippingAddressName = (String) getValueFromQueryResult(defaultShippingAddressList, "AddressLine1");
			assertTrue("Default radio button in Shipping page is not selected", storeFrontShippingInfoPage.isDefaultShippingAddressSelected(shippingAddressName));
		}

		//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		s_assert.assertAll();
	}


	// Hybris Phase 2-2035 :: Version : 1 :: Edit shipping address on 'Shipping Profile' page
	@Test (invocationCount=20)
	public void testEditShippingAddressOnShippingProfilePage_2035() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontShippingInfoPage.clickOnEditForFirstAddress();
		String newShippingAdrressName = TestConstants.ADDRESS_NAME_US+randomNum;
		String lastName = "test";
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAdrressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(TestConstants.ADDRESS_LINE_1_US);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(TestConstants.CITY_US);
		storeFrontShippingInfoPage.selectNewShippingAddressState();
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(TestConstants.POSTAL_CODE_US);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER_US);
		storeFrontShippingInfoPage.selectFirstCardNumber();
		storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.SECURITY_NUMBER_US);
		storeFrontShippingInfoPage.selectUseThisShippingProfileFutureAutoshipChkbox();
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();

		//--------------- Verify that Newly added Shipping is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAdrressName), "New Shipping address is not selected listed on Shipping profile page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		//--------------- Verify That 'Autoship Order Address' Text is displayed under default shipping Address-------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAdrressName), "Autoship order text not present under the new Shipping Address");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();

		//---------------Verify that the new added shipping address is displayed in 'Shipment' section on update autoship cart page------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingAdrressName), "New Shipping address NOT added to update cart");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		s_assert.assertAll();

	}

	//Edit a shipping profile from MY ACCOUNT, having "Use this billing profile for your future auto-ship" check box NOT CHECKED:
	//Hybris Project-4465:Edit a shipping profile from MY ACCOUNT, having "Use this billing profile for your future auto-ship"
	@Test
	public void testEditShippingProfileMyAccountFutureAutoshipNotSelected_4465() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontShippingInfoPage.clickOnEditForFirstAddress();
		String newShippingAddressName = TestConstants.ADDRESS_NAME_US+randomNum;
		String lastName = "test";
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(TestConstants.ADDRESS_LINE_1_US);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(TestConstants.CITY_US);
		storeFrontShippingInfoPage.selectNewShippingAddressState();
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(TestConstants.POSTAL_CODE_US);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER_US);
		storeFrontShippingInfoPage.selectFirstCardNumber();
		storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.SECURITY_NUMBER_US);
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();

		//--------------- Verify that Newly edited Shipping is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "New Shipping address is not listed on Shipping profile page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//------------------ Verify that autoship template doesn't contains the newly edited shipping profile address by verifying by name------------------------------------------------------------		

		s_assert.assertFalse(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"Autoship Template Shipping Address contains the new shipping address even when future autoship checkbox not selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		//------------------ Verify that adhoc orders template doesn't contains the newly created shipping profile address by verifying by name------------------------------------------------------------

		s_assert.assertFalse(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"AdHoc Orders Template Shipping Address contains new shipping address even when future autoship checkbox not selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertAll();
	}
}
