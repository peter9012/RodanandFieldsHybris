package com.rf.test.website.crm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.CRMHomePage;
import com.rf.pages.website.CRMLoginPage;
import com.rf.pages.website.StoreFrontAccountInfoPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class AccountDetailsTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AccountDetailsTest.class.getName());
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private CRMLoginPage crmLoginpage;
	private CRMHomePage crmHomePage;
	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String city = null;
	private String postalCode = null;
	private String phoneNumber = null;
	private String country = null;
	private String pcUserEmailAddress =null;
	private String rcUserEmailAddress = null;
	private String consultantEmailAddress = null;
	
	@Test
	public void testExpressEnrollmentRFExpressKitReverseRegimen() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REVERSE;
		consultantEmailAddress =TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_PRICE_EXPRESS_CA;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_PRICE_EXPRESS_US;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForTermsAndConditions(), "PopUp for terms and conditions is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		s_assert.assertAll();
	}

	@Test(dependsOnMethods="testExpressEnrollmentRFExpressKitReverseRegimen")
	public void verifyConsultantDetails() throws InterruptedException{
		consultantEmailAddress="auto758952@xyz.com";
		phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		driver.get(driver.getCrmURL());
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.searchUserById(consultantEmailAddress);
		crmHomePage.clickaccountNameForAccountDetailsPage(TestConstants.FIRST_NAME);
		s_assert.assertTrue(crmHomePage.verifyAccountTypeForConsultant(),"Account type is not for created Consultant user");
		//s_assert.assertTrue(crmHomePage.verifyPhoneNumber(phoneNumber),"Phone number is not valid for user");
		s_assert.assertTrue(crmHomePage.verifyEmailAddress(consultantEmailAddress),"Email address is not valid for user");
		s_assert.assertTrue(crmHomePage.verifyEditPWSDomainButton(), "Edit Pws Domain button is not present");
		s_assert.assertTrue(crmHomePage.verifyPulseButton(),"Pulse button is not present for this user");
		s_assert.assertTrue(crmHomePage.verifyListHoverAutoShipTitle(),"List hover autoship title is not present for this user");
		s_assert.assertTrue(crmHomePage.verifyListHoverPerformanceKPI(),"Performance KPI link is not present for this user");
		s_assert.assertTrue(crmHomePage.verifyListHoverAccountActivities(),"Account activities link is not present for this user");
		s_assert.assertTrue(crmHomePage.verifyCountryName(),"Country Name is not as expected for this user");
		s_assert.assertAll();
	}
	@Test
	public void testPCEnrollment() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);	
		country = driver.getCountry();
		if(country.equalsIgnoreCase("CA")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		pcUserEmailAddress =TestConstants.PC_FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.clickOnShopLink();
		storeFrontHomePage.clickOnAllProductsLink();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product  and proceed to buy it
		storeFrontHomePage.applyPriceFilterHighToLow();
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Update Quantity of product
		storeFrontHomePage.addQuantityOfProduct("10");
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(TestConstants.PC_FIRST_NAME,TestConstants.PC_LAST_NAME,pcUserEmailAddress,TestConstants.PC_USER_PASSWORD_STG2);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo(addressLine1,city,postalCode,phoneNumber);
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();	

	}
	@Test(dependsOnMethods="testPCEnrollment")
	public void verifyPCUserDetails() throws InterruptedException{
		pcUserEmailAddress="PCUser931791@xyz.com";
		phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		driver.get(driver.getCrmURL());
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.searchUserById(pcUserEmailAddress);
		crmHomePage.clickaccountNameForAccountDetailsPage(TestConstants.PC_FIRST_NAME);
		s_assert.assertTrue(crmHomePage.verifyAccountTypeForPCUser(),"Account type is not for created PC user");
		s_assert.assertTrue(crmHomePage.verifyPhoneNumber(phoneNumber),"Phone number is not valid for user");
		s_assert.assertTrue(crmHomePage.verifyEmailAddress(pcUserEmailAddress),"Email address is not valid for user");
		s_assert.assertFalse(crmHomePage.verifyEditPWSDomainButton(), "Edit Pws Domain button is present");
		s_assert.assertFalse(crmHomePage.verifyPulseButton(),"Pulse button is present for this user");
		s_assert.assertTrue(crmHomePage.verifyListHoverAutoShipTitle(),"List hover autoship title is not present for this user");
		s_assert.assertFalse(crmHomePage.verifyListHoverPerformanceKPI(),"Performance KPI link is present for this user");
		s_assert.assertFalse(crmHomePage.verifyListHoverAccountActivities(),"Account activities link is present for this user");
		s_assert.assertTrue(crmHomePage.verifyCountryName(),"Country Name is not as expected for this user");
		s_assert.assertAll();
	}
	@Test
	public void testRCEnrollment() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		country = driver.getCountry();
		if(country.equalsIgnoreCase("CA")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		rcUserEmailAddress = TestConstants.RC_FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.clickOnShopLink();
		storeFrontHomePage.clickOnAllProductsLink();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.applyPriceFilterHighToLow();
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Update Quantity of product
		storeFrontHomePage.addQuantityOfProduct("10");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(TestConstants.RC_FIRST_NAME,TestConstants.RC_LAST_NAME,rcUserEmailAddress,TestConstants.RC_USER_PASSWORD_STG2);

		//CheckoutPage is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCheckoutPageDisplayed(), "Checkout page has NOT displayed");
		logger.info("Checkout page has displayed");

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo(addressLine1,city,postalCode,phoneNumber);
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();	

	}
	@Test(dependsOnMethods="testRCEnrollment")
	public void verifyRCUserDetails() throws InterruptedException{
		driver.get(driver.getCrmURL());
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.searchUserById(rcUserEmailAddress);
		crmHomePage.clickaccountNameForAccountDetailsPage(TestConstants.RC_FIRST_NAME);
		s_assert.assertTrue(crmHomePage.verifyAccountTypeForRCUser(),"Account type is not for created RC user");
		s_assert.assertTrue(crmHomePage.verifyPhoneNumber(phoneNumber),"Phone number is not valid for user");
		s_assert.assertTrue(crmHomePage.verifyEmailAddress(rcUserEmailAddress),"Email address is not valid for user");
		s_assert.assertFalse(crmHomePage.verifyEditPWSDomainButton(), "Edit Pws Domain button is present");
		s_assert.assertFalse(crmHomePage.verifyPulseButton(),"Pulse button is present for this user");
		s_assert.assertFalse(crmHomePage.verifyListHoverAutoShipTitle(),"List hover autoship title is present for this user");
		s_assert.assertFalse(crmHomePage.verifyListHoverPerformanceKPI(),"Performance KPI link is present for this user");
		s_assert.assertFalse(crmHomePage.verifyListHoverAccountActivities(),"Account activities link is present for this user");
		s_assert.assertTrue(crmHomePage.verifyCountryName(),"Country Name is not as expected for this user");
		s_assert.assertAll();
}
}