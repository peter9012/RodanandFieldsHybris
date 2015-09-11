package com.rf.test.website.storeFront.miniRegression;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontRCUserPage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class AdhocOrdersTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AdhocOrdersTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;	
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private String RFO_DB = null;

	// Hybris Phase 2-1878 :: Version : 1 :: Create Adhoc Order For The Consultant Customer
	@Test
	public void testCreateAdhocOrderConsultant() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
	//	List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_STG2;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";

//		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,RFO_DB);
//		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnShopLink();
		storeFrontConsultantPage.clickOnAllProductsLink();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmation(),"Confirmation of order popup is not present");
		storeFrontUpdateCartPage.clickOnConfirmationOK();

		String subtotal = storeFrontUpdateCartPage.getSubtotal();
		System.out.println("subtotal ="+subtotal);
		String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
		System.out.println("deliveryCharges ="+deliveryCharges);
		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		System.out.println("handlingCharges ="+handlingCharges);
		String tax = storeFrontUpdateCartPage.getTax();
		System.out.println("tax ="+tax);
		String total = storeFrontUpdateCartPage.getTotal();
		System.out.println("total ="+total);
		String totalSV = storeFrontUpdateCartPage.getTotalSV();
		System.out.println("totalSV ="+totalSV);
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		System.out.println("shippingMethod ="+shippingMethod);
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();
		System.out.println("BillingAddress ="+BillingAddress);

		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();

		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateSubtotal(subtotal),"AdHoc Orders Template Subtotal is not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateHandlingCharges(handlingCharges),"AdHoc Orders Template Handling charges are not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateTotal(total),"AdHoc Orders Template Total is not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateTax(tax),"AdHoc Orders Tax is not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateTotalSV(totalSV),"AdHoc Orders Template Total SV vakue is not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyShippingMethodOnTemplateAfterOrderCreation(shippingMethod),"AdHoc Orders Template Shipping Method is not as expected for this order");


		s_assert.assertAll();
	}

	// Hybris Phase 2-1877 :: Version : 1 :: Create Adhoc Order For The Preferred Customer 
	@Test
	public void testCreateAdhocOrderPC() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();

		//List<Map<String, Object>> randomPCList =  null;
		String pcUserEmailID = TestConstants.PC_EMAIL_ID_STG2;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";

		//randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,RFO_DB);
		//pcUserEmailID = (String) getValueFromQueryResult(randomPCList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnShopLink();
		storeFrontPCUserPage.clickOnAllProductsLink();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmation(),"Confirmation of order popup is not present");
		storeFrontUpdateCartPage.clickOnConfirmationOK();

		String subtotal = storeFrontUpdateCartPage.getSubtotal();
		System.out.println("subtotal ="+subtotal);
		String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
		System.out.println("deliveryCharges ="+deliveryCharges);
		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		System.out.println("handlingCharges ="+handlingCharges);
		String tax = storeFrontUpdateCartPage.getTax();
		System.out.println("tax ="+tax);
		String total = storeFrontUpdateCartPage.getTotal();
		System.out.println("total ="+total);
		String totalSV = storeFrontUpdateCartPage.getTotalSV();
		System.out.println("totalSV ="+totalSV);
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		System.out.println("shippingMethod ="+shippingMethod);
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();
		System.out.println("BillingAddress ="+BillingAddress);

		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();

		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateSubtotal(subtotal),"AdHoc Orders Template Subtotal is not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateHandlingCharges(handlingCharges),"AdHoc Orders Template Handling charges are not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateTotal(total),"AdHoc Orders Template Total is not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateTax(tax),"AdHoc Orders Tax is not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateTotalSV(totalSV),"AdHoc Orders Template Total SV vakue is not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyShippingMethodOnTemplateAfterOrderCreation(shippingMethod),"AdHoc Orders Template Shipping Method is not as expected for this order");

		s_assert.assertAll();
	}

	// Hybris Phase 2-1879 :: Version : 1 :: Create Adhoc Order For The Retail Customer
	@Test
	public void testCreateAdhocOrderRC_1879() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		//List<Map<String, Object>> randomRCList =  null;

		String rcUserEmailID = TestConstants.RCUSER_EMAIL_ID;
		String accountID = null;

		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";

	//	randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,RFO_DB);
	//	rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");
	//	accountID = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
	//	logger.info("Account ID of the user is "+accountID);

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
		s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcUserEmailID),"RC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnShopLink();
		storeFrontRCUserPage.clickOnQuickShopImage();   
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnContinueWithoutSponsorLink();
		storeFrontUpdateCartPage.clickOnNextButtonAfterSelectingSponsor();

		String subtotal = storeFrontUpdateCartPage.getSubtotal();
		logger.info("Subtotal while creating order is "+subtotal);
		String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
		logger.info("Delivery charges while creating order is "+deliveryCharges);
		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		logger.info("Handling charges while creating order is "+handlingCharges);
		String tax = storeFrontUpdateCartPage.getTax();
		logger.info("Tax while creating order is "+tax);
		String total = storeFrontUpdateCartPage.getTotal();
		logger.info("Total while creating order is "+total);
		//  String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		//  String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();

		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		logger.info("Order Number after placing the order is "+orderNumber);

		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOrderNumber(orderNumber);

		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateSubtotal(subtotal),"Subtotal on AdHoc Orders Template is NOT "+subtotal);
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateHandlingCharges(handlingCharges),"Handling charges on AdHoc Orders Template is NOT "+handlingCharges);
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateTax(tax),"Tax on AdHoc Orders Template is NOT "+tax);
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateTotal(total),"Total on AdHoc Orders Template is NOT "+total);

		s_assert.assertAll();
	}

}
