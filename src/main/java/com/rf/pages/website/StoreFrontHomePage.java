package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;


public class StoreFrontHomePage extends RFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(StoreFrontHomePage.class.getName());
	private Actions actions;

	private final By BUSINESS_LINK_LOC = By.cssSelector("a[id='corp-opp']"); 
	private final By ENROLL_NOW_LINK_LOC = By.cssSelector("a[title='Enroll Now']");	
	private final By LOGIN_LINK_LOC = By.cssSelector("li[id='log-in-button']>a");
	private final By LOGIN_BTN_LOC = By.cssSelector("input[value='Log in']");
	private final By USERNAME_TXTFLD_LOC = By.id("username");
	private final By PASSWORD_TXTFLD_LOC = By.id("password");
	private final By CONSULTANT_VALIDATION_POPUP_LESS_THAN_6_MONTH = By.xpath("//div[@id='inactiveConsultant180Popup']/div/div");


	public StoreFrontHomePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public void clickOnOurBusinessLink(){
		driver.waitForElementPresent(BUSINESS_LINK_LOC);
		driver.click(BUSINESS_LINK_LOC);
		logger.info("Our business Link clicked");
	}

	public StoreFrontEnrollNowPage clickOnOurEnrollNowLink(){
		driver.waitForElementPresent(ENROLL_NOW_LINK_LOC);
		driver.click(ENROLL_NOW_LINK_LOC);
		logger.info("Enroll Now Link clicked");
		driver.waitForLoadingImageToDisappear();
		return new StoreFrontEnrollNowPage(driver);
	}

	public StoreFrontConsultantPage loginAsConsultant(String username,String password){
		driver.waitForElementPresent(LOGIN_LINK_LOC);
		driver.click(LOGIN_LINK_LOC);
		logger.info("login link clicked");
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);		
		logger.info("login username is: "+username);
		logger.info("login password is: "+password);
		driver.click(LOGIN_BTN_LOC);	
		logger.info("login button clicked");
		driver.waitForPageLoad();
		return new StoreFrontConsultantPage(driver);
	}

	public StoreFrontRCUserPage loginAsRCUser(String username,String password){
		driver.waitForElementPresent(LOGIN_LINK_LOC);
		driver.click(LOGIN_LINK_LOC);
		logger.info("login link clicked");
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);		
		logger.info("login username is "+username);
		logger.info("login password is "+password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		driver.waitForPageLoad();
		return new StoreFrontRCUserPage(driver);
	}
	public StoreFrontPCUserPage loginAsPCUser(String username,String password){
		driver.waitForElementPresent(LOGIN_LINK_LOC);
		driver.click(LOGIN_LINK_LOC);		
		logger.info("login link clicked");
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);		
		logger.info("login username is "+username);
		logger.info("login password is "+password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		driver.waitForPageLoad();
		return new StoreFrontPCUserPage(driver);
	}

	public void openConsultantPWS(String pwsURL){
		logger.info("User PWS is "+pwsURL);
		driver.get(pwsURL);		
	}

	public boolean isCurrentURLShowsError() throws InterruptedException{
		driver.waitForPageLoad();
		logger.info("Curremt URL is "+driver.getCurrentUrl());
		return driver.getCurrentUrl().contains("login?error=true");
	}

	public void searchCID() throws InterruptedException{
		driver.waitForElementPresent(By.id("sponserparam"));
		driver.findElement(By.id("sponserparam")).sendKeys("test");
		driver.waitForElementPresent(By.id("search-sponsor-button"));
		driver.click(By.id("search-sponsor-button"));
		logger.info("Sponsor entered as 'test' and search button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void searchCID(String cid) throws InterruptedException{
		driver.waitForElementPresent(By.id("sponserparam"));
		driver.findElement(By.id("sponserparam")).sendKeys(cid);
		driver.waitForElementPresent(By.id("search-sponsor-button"));
		driver.click(By.id("search-sponsor-button"));
		logger.info("Sponsor entered as "+cid+" and search button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void mouseHoverSponsorDataAndClickContinue() throws InterruptedException{
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//div[@class='the-search-results']/form[1]/div[@class='sponsorDataDiv']"))).click(driver.findElement(By.cssSelector("input[value='Select & Continue']"))).build().perform();
		logger.info("First result of sponsor has been clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}
	
	public void mouseHoverOtherSponsorDataAndClickContinue() throws InterruptedException{
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//div[@class='the-search-results']/form[3]/div[@class='sponsorDataDiv']"))).click(driver.findElement(By.cssSelector("input[value='Select & Continue']"))).build().perform();
		logger.info("Second result of sponsor has been clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void selectEnrollmentKitPage(String kitPrice,String regimenName){
		driver.waitForLoadingImageToDisappear();
		kitPrice =  kitPrice.toUpperCase();
		driver.waitForElementPresent(By.xpath("//div[@class='kit-price' and contains(text(),'"+kitPrice+"')]"));
		logger.info("EnrollmentTest Kit is selected as "+kitPrice);
		driver.click(By.xpath("//div[@class='kit-price' and contains(text(),'"+kitPrice+"')]"));
		regimenName = regimenName.toUpperCase();
		driver.pauseExecutionFor(500);
		//driver.click(By.xpath("//div[@class='regimen-name' and contains(text(),'"+regimenName+"')]"));
		driver.click(By.xpath("//div[@class='regimen-kit-selection-content']//div[4]/div[@class='regimen-name']"));
		driver.pauseExecutionFor(500);
		logger.info("Regimen is selected as "+regimenName);
		driver.click(By.cssSelector("input[value='Next']"));
		logger.info("Next button clicked after selected Kit and regimen");
		driver.waitForLoadingImageToDisappear();
	}

	public void selectEnrollmentKitPage(String kitPrice){
		driver.waitForLoadingImageToDisappear();
		kitPrice =  kitPrice.toUpperCase();
		driver.waitForElementPresent(By.xpath("//div[@class='kit-price' and contains(text(),'"+kitPrice+"')]"));
		logger.info("EnrollmentTest Kit is selected as "+kitPrice);
		driver.click(By.xpath("//div[@class='kit-price' and contains(text(),'"+kitPrice+"')]"));
		logger.info("Next button clicked after selected Kit");
		driver.waitForLoadingImageToDisappear();
	}

	public void chooseEnrollmentOption(String option){
		option = option.toUpperCase();
		if(option.equalsIgnoreCase("EXPRESS ENROLLMENT")){
			driver.waitForElementPresent(By.id("express-enrollment"));
			driver.click(By.id("express-enrollment"));
			logger.info("Express EnrollmentTest is clicked");
		}
		else{
			driver.waitForElementPresent(By.id("standard-enrollment"));
			driver.click(By.id("standard-enrollment"));
			logger.info("Standard EnrollmentTest is clicked");
		}

		driver.click(By.cssSelector("input[value='Next']"));
		logger.info("Next button is clicked after selecting enrollment type");
		driver.waitForLoadingImageToDisappear();
	}

	public void enterFirstName(String firstName){
		driver.waitForElementPresent(By.id("first-name"));
		driver.type(By.id("first-name"), firstName);
		logger.info("first name entered as "+firstName);
	}

	public void enterLastName(String lastName){
		driver.type(By.id("last-name"),lastName);
	}

	public void enterEmailAddress(String emailAddress){		
		driver.type(By.id("email-account"), emailAddress+"\t");
		driver.waitForSpinImageToDisappear();
	}

	public void closePopUp(){
		driver.click(By.cssSelector("a[title='Close']"));
		driver.pauseExecutionFor(2000);
	}	

	public Boolean checkExistenceOfEmailAddress() throws InterruptedException{
		driver.findElement(By.id("email-account")).sendKeys("\t");		
		try{
			driver.findElement(By.xpath("//div[text()='Please fix this field.']"));
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}

	public void enterPassword(String password){
		driver.type(By.id("new-password-account"),password);
	}

	public void enterConfirmPassword(String password){
		driver.type(By.id("new-password-account2"),password);
	}

	public void enterAddressLine1(String addressLine1){
		driver.type(By.id("address-1"),addressLine1);
		logger.info("Address Line 1 entered is "+addressLine1);
	}

	public void enterCity(String city){
		driver.type(By.id("city"),city);
		logger.info("City entered is "+city);
	}

	public void selectProvince(){		
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
		driver.click(By.xpath("//select[@id='state']/option[2]"));
		logger.info("state selected");
	}

	public void selectProvince(String province){		
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[contains(text(),'"+province+"')]"));
		driver.click(By.xpath("//select[@id='state']/option[contains(text(),'"+province+"')]"));
		logger.info("state selected");
	}

	public boolean verifyQuebecProvinceIsDisabled(){
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[contains(text(),'"+TestConstants.PROVINCE_QUEBEC+"')]"));
		return !(driver.findElement(By.xpath("//select[@id='state']/option[contains(text(),'"+TestConstants.PROVINCE_QUEBEC+"')]")).isEnabled());
	}

	public void enterPostalCode(String postalCode){
		driver.type(By.id("postcode"),postalCode);
		logger.info("postal code entered is "+postalCode);
	}

	public void enterPhoneNumber(String phnNum){
		driver.type(By.id("phonenumber"),phnNum);
		logger.info("phone number entered is "+phnNum);
	}

	public void clickEnrollmentNextBtn() throws InterruptedException{
		driver.waitForElementPresent(By.id("enrollment-next-button"));
		driver.pauseExecutionFor(2000);
		driver.click(By.id("enrollment-next-button"));
		logger.info("EnrollmentTest Next Button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		//		try{
		//			driver.quickWaitForElementPresent(By.id("QAS_AcceptOriginal"));
		//			driver.click(By.id("QAS_AcceptOriginal"));
		//			logger.info("Accept the original button clicked");
		//		}
		//		catch(Exception e){
		//			logger.info("Accept the original pop up was NOT present");
		//		}
	}

	public void clickNextButton(){
		driver.waitForElementPresent(By.id("enrollment-next-button"));
		driver.pauseExecutionFor(2000);
		driver.click(By.id("enrollment-next-button"));
		logger.info("EnrollmentTest Next Button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		try{
			driver.quickWaitForElementPresent(By.id("QAS_AcceptOriginal"));
			driver.click(By.id("QAS_AcceptOriginal"));
			logger.info("Accept the original button clicked");
		}
		catch(Exception e){
			logger.info("Accept the original pop up was NOT present");
		}
	}

	public boolean verifySubsribeToPulseCheckBoxIsSelected() throws InterruptedException{		
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input[@checked='checked']"));
		return driver.isElementPresent(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input[@checked='checked']"));
	}

	public boolean verifySubsribeToPulseCheckBoxIsNotSelected(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input"));
		return !driver.findElement(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input")).isSelected();
	}

	public boolean verifyEnrollToCRPCheckBoxIsSelected(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input[@checked='checked']"));
		return driver.isElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input[@checked='checked']"));
	}

	public boolean verifyEnrollToCRPCheckBoxIsNotSelected(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input"));
		return !driver.findElement(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input")).isSelected();
	}

	public void uncheckPulseAndCRPEnrollment() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input"));
		driver.click(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input/.."));
		logger.info("Yes,Subscribe me to pulse checkbox is unchecked");
		driver.click(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input/.."));
		logger.info("Yes,enroll me in CRP checkbox is unchecked");
		driver.pauseExecutionFor(2000);
	}


	public void acceptTheVerifyYourShippingAddressPop() throws InterruptedException{
		driver.waitForElementPresent(By.id("QAS_AcceptOriginal"));
		driver.click(By.id("QAS_AcceptOriginal"));
		logger.info("accept the original button clicked");
	}

	public boolean verifySuggesstionsForEnteredAddressPop(){
		try{
			driver.waitForElementPresent(By.id("QAS_AcceptOriginal"));
			driver.click(By.id("QAS_AcceptOriginal"));
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}

	public void enterCardNumber(String cardNumber){
		driver.waitForElementPresent(By.id("card-nr"));
		driver.findElement(By.id("card-nr")).sendKeys(cardNumber+"\t");
		logger.info("card number entered as "+cardNumber);
	}

	public void enterNameOnCard(String nameOnCard){
		driver.findElement(By.id("card-name")).sendKeys(nameOnCard);
		logger.info("name on card entered is "+nameOnCard);
	}

	public boolean validateInvalidCreditCardMessage(){
		if(driver.findElement(By.xpath("//div[contains(text(),'Please enter a valid')]")).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean validateEmptyCreditCardMessage(){
		if(driver.findElement(By.xpath("//div[contains(text(),'This field is required')]")).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}

	public void clearCreditCardNumber(){
		driver.findElement(By.id("card-nr")).clear();
		logger.info("credit card number box cleared");
	}

	public void selectExpirationDate(String month,String year){
		Select monthDD = new Select(driver.findElement(By.id("expiryMonth")));
		Select yearDD = new Select(driver.findElement(By.id("expiryYear")));
		monthDD.selectByVisibleText(month.toUpperCase());
		yearDD.selectByVisibleText(year);		
	}

	public void enterSecurityCode(String securityCode){
		driver.findElement(By.id("security-code")).sendKeys(securityCode);
		logger.info("security code entered is "+securityCode);
	}

	public void enterSocialInsuranceNumber(String sin) throws InterruptedException{
		driver.findElement(By.id("S-S-N")).sendKeys(sin+"\t");
		logger.info("Social Insurance Number is "+sin);
	}

	public void enterNameAsItAppearsOnCard(String nameOnCard){
		driver.findElement(By.id("name-on-card")).sendKeys(nameOnCard);
		logger.info("name on card entered is "+nameOnCard);
	}

	public void checkThePoliciesAndProceduresCheckBox() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@id='policies-check']/.."));
		driver.click(By.xpath("//input[@id='policies-check']/.."));
		logger.info("The Policies And Procedures CheckBox is checked");
	}

	public void checkTheIAcknowledgeCheckBox(){
		driver.click(By.xpath("//input[@id='acknowledge-check']/.."));
		logger.info("The I Acknowledge CheckBox is checked");
	}

	public boolean isTheTermsAndConditionsCheckBoxDisplayed(){
		driver.waitForElementPresent(By.xpath("//input[@id='terms-check']/.."));
		return driver.IsElementVisible(driver.findElement(By.xpath("//input[@id='terms-check']/..")));
	}

	public void checkTheTermsAndConditionsCheckBox(){
		driver.waitForElementPresent(By.xpath("//input[@id='terms-check']/.."));
		driver.click(By.xpath("//input[@id='terms-check']/.."));		
		logger.info("The Terms And Conditions CheckBox selected");
	}

	public void clickOnEnrollmentNextButton(){
		driver.waitForElementPresent(By.id("enrollment-next-button"));
		driver.click(By.id("enrollment-next-button")); 
		logger.info("enrollment next button clicked");
	}
	public boolean verifyPopUpForPoliciesAndProcedures() throws InterruptedException{
		boolean isPopForTermsAndConditionsVisible = false;		
		driver.waitForElementPresent(By.xpath("//div[@class='popup-standard']/h2[text()='Please review and accept all policies and procedures.']"));
		isPopForTermsAndConditionsVisible = driver.IsElementVisible(driver.findElement(By.xpath("//div[@class='popup-standard']/h2[text()='Please review and accept all policies and procedures.']")));
		if(isPopForTermsAndConditionsVisible==true){
			driver.click(By.xpath("//div[@class='popup-standard']/h2[text()='Please review and accept all policies and procedures.']/following::a[@title='Close']"));
			driver.pauseExecutionFor(1000);
			return true;
		}
		return false;
	}

	public boolean verifyPopUpForTermsAndConditions() throws InterruptedException{
		boolean isPopForTermsAndConditionsVisible = false;		
		driver.waitForElementPresent(By.xpath("//div[@class='popup-standard']/h2[text()='Please accept our terms & conditions']"));
		isPopForTermsAndConditionsVisible = driver.IsElementVisible(driver.findElement(By.xpath("//div[@class='popup-standard']/h2[text()='Please accept our terms & conditions']")));
		if(isPopForTermsAndConditionsVisible==true){
			driver.click(By.xpath("//div[@class='popup-standard']/h2[text()='Please accept our terms & conditions']/following::a[@title='Close']"));
			driver.pauseExecutionFor(1000);
			return true;
		}
		return false;
	}

	public void checkTheIAgreeCheckBox(){
		driver.click(By.xpath("//input[@id='electronically-check']/.."));
		logger.info("I Agree checkbox clicked");
	}

	public void clickOnChargeMyCardAndEnrollMeBtn() throws InterruptedException{
		driver.waitForElementPresent(By.id("enroll-button"));
		driver.click(By.id("enroll-button"));
		logger.info("Charge my card button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnEnrollMeBtn() throws InterruptedException{
		clickOnChargeMyCardAndEnrollMeBtn();
	}

	public void clickOnConfirmAutomaticPayment() throws InterruptedException{
		driver.waitForElementPresent(By.id("enroll"));
		driver.click(By.id("enroll"));
		logger.info("Automatic payment confirmation button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyCongratsMessage(){
		driver.waitForElementPresent(By.id("Congrats"));
		return driver.IsElementVisible(driver.findElement(By.id("Congrats")));
	}

	public void clickOnAddToCRPButtonCreatingCRPUnderBizSite() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='quick-refine']/following::div[1]/div[2]/div[1]//input[@value='Add to crp']"));
		driver.click(By.xpath("//div[@id='quick-refine']/following::div[1]/div[2]/div[1]//input[@value='Add to crp']"));
		logger.info("Add to CRP button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnCRPCheckout(){
		driver.waitForElementPresent(By.id("crpCheckoutButton"));
		driver.click(By.id("crpCheckoutButton"));
		logger.info("checkout button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnUpdateCartShippingNextStepBtnDuringEnrollment() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.xpath("//input[@class='use_address']"));
		//action.moveToElement(driver.findElement(By.xpath("//input[@id='use_address']"))).double.build().perform();		
		action.moveToElement(driver.findElement(By.xpath("//input[@class='use_address']"))).click(driver.findElement(By.xpath("//input[@class='use_address']"))).build().perform();
		logger.info("Next button on shipping update cart clicked");	
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyOrderConfirmation(){
		logger.info("Asserting Order Confirmation Message");
		driver.waitForElementPresent(By.xpath("//div[@id='order-confirm']/span"));
		if(driver.findElement(By.xpath("//div[@id='order-confirm']/span")).getText().equalsIgnoreCase("Your CRP order has been created")){
			return true;
		}
		return false;
	}

	public void clickOnGoToMyAccountToCheckStatusOfCRP(){
		driver.waitForElementPresent(By.xpath("//div[@id='order-confirm']/a"));
		driver.click(By.xpath("//div[@id='order-confirm']/a"));	
		logger.info("Go to my account to check status of CRP button clicked");
	}

	public void clickOnAccountInfoLinkLeftSidePannel(){
		driver.waitForElementPresent(By.xpath("//div[@id='left-menu']//a[text()='ACCOUNT INFO']"));
		driver.click(By.xpath("//div[@id='left-menu']//a[text()='ACCOUNT INFO']"));
		logger.info("account info link on side panel clicked");
		driver.pauseExecutionFor(1000);
	}

	public boolean validateErrorMessageWithoutSelectingAllCheckboxes(){
		driver.waitForElementPresent(By.xpath("//h2[contains(text(),'Please accept our terms & conditions')]"));
		return driver.findElement(By.xpath("//h2[contains(text(),'Please accept our terms & conditions')]")).isDisplayed();
	}

	public void acceptTheProvideAccessToSpousePopup(){
		if(driver.findElement(By.id("acceptSpouse")).isDisplayed()){
			driver.click(By.id("acceptSpouse"));
		}
	}

	public void clickSwitchToExpressEnrollmentLink(){
		driver.waitForElementPresent(By.xpath("//a[@id='lnk-switch']"));
		driver.click(By.xpath("//a[@id='lnk-switch']"));
		driver.pauseExecutionFor(3000);
	}

	public void checkPulseCheckBox(){
		driver.waitForElementPresent(By.xpath("//input[@id='pulse-check']/.."));
		System.out.println("check"+(driver.findElement(By.xpath("//input[@id='pulse-check']/..")).isSelected()));
		if(driver.findElement(By.xpath("//input[@id='pulse-check']/..")).isSelected()){
			driver.click(By.xpath("//input[@id='pulse-check']/.."));
		}
		else{
			driver.pauseExecutionFor(500);
		}
	}

	public void checkCRPCheckBox(){
		driver.waitForElementPresent(By.xpath("//input[@id='CRP-check']/.."));
		if(driver.findElement(By.xpath("//input[@id='CRP-check']/..")).isSelected()){
			driver.click(By.xpath("//input[@id='CRP-check']/.."));
		}
		else{
			driver.pauseExecutionFor(500);
		}
	}
	public void selectProductAndProceedToAddToCRP() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='quick-refine']"));
		try{
			driver.click(By.xpath("//div[@id='quick-refine']/following::div[1]/div[2]/div[1]//input[@value='Add to crp']"));
			logger.info("Add to CRP button clicked");
		}catch(Exception e){
			driver.click(By.xpath("//div[@id='quick-refine']/following::div[2]/div[1]/div[2]//input[@value='Add to crp']"));
		}
		driver.waitForLoadingImageToDisappear();
	}
	public void clickNextOnCRPCartPage(){
		driver.waitForElementPresent(By.id("submitForm"));
		driver.click(By.id("submitForm"));
	}
	public void clickSwitchToExpressEnrollmentOnRecurringMonthlyChargesSection(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Switch to Express')]"));
		driver.findElement(By.xpath("//a[contains(text(),'Switch to Express')]")).click();
	}

	public void enterUserInformationForEnrollment(String kitName,String regimenName,String enrollmentType,String firstName,String lastName,String password,String addressLine1,String city,String postalCode,String phoneNumber){
		selectEnrollmentKitPage(kitName, regimenName);		
		chooseEnrollmentOption(enrollmentType);
		enterFirstName(firstName);
		enterLastName(lastName);
		enterPassword(password);
		enterConfirmPassword(password);
		enterAddressLine1(addressLine1);
		enterCity(city);
		selectProvince();
		enterPostalCode(postalCode);
		enterPhoneNumber(phoneNumber);
		enterEmailAddress(firstName+TestConstants.EMAIL_ADDRESS_SUFFIX);
	}

	//Method Overloaded without Kit and Regimen
	public void enterUserInformationForEnrollment(String firstName,String lastName,String password,String addressLine1,String city,String postalCode,String phoneNumber){
		enterFirstName(firstName);
		enterLastName(lastName);
		enterPassword(password);
		enterConfirmPassword(password);
		enterAddressLine1(addressLine1);
		enterCity(city);
		selectProvince();
		enterPostalCode(postalCode);
		enterPhoneNumber(phoneNumber);
		enterEmailAddress(firstName+TestConstants.EMAIL_ADDRESS_SUFFIX);
	}

	//method overloaded,no need for enrollment type if kit is portfolio
	public void enterUserInformationForEnrollment(String kitName,String regimenName,String firstName,String lastName,String password,String addressLine1,String city,String postalCode,String phoneNumber){
		selectEnrollmentKitPage(kitName);		
		enterFirstName(firstName);
		enterLastName(lastName);
		enterPassword(password);
		enterConfirmPassword(password);
		enterAddressLine1(addressLine1);
		enterCity(city);
		selectProvince();
		enterPostalCode(postalCode);
		enterPhoneNumber(phoneNumber);
		enterEmailAddress(firstName+TestConstants.EMAIL_ADDRESS_SUFFIX);
	}

	// method overloaded, parameter for province is there
	public void enterUserInformationForEnrollment(String kitName,String regimenName,String enrollmentType,String firstName,String lastName,String password,String addressLine1,String city,String province,String postalCode,String phoneNumber){
		selectEnrollmentKitPage(kitName, regimenName);		
		chooseEnrollmentOption(enrollmentType);
		enterFirstName(firstName);
		enterLastName(lastName);
		enterPassword(password);
		enterConfirmPassword(password);
		enterAddressLine1(addressLine1);
		enterCity(city);
		selectProvince(province);
		enterPostalCode(postalCode);
		enterPhoneNumber(phoneNumber);
		enterEmailAddress(firstName+TestConstants.EMAIL_ADDRESS_SUFFIX);
	}

	public void clickOnSwitchToStandardEnrollmentLink(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Switch to Standard Enrollment')]"));
		driver.click(By.xpath("//a[contains(text(),'Switch to Standard Enrollment')]"));
		driver.waitForPageLoad();
	}

	//	public boolean validateErrorMessageForActivePC(){
	//		String ActivePC="autopc@xyz.com";
	//		driver.findElement(By.xpath("//input[@id='email-account']")).sendKeys(ActivePC);
	//		driver.findElement(By.xpath("//input[@id='new-password-account']")).sendKeys(password);   
	//		boolean status=driver.findElement(By.xpath("//div[@class='tipsy-inner']")).isDisplayed();
	//		if(driver.findElement(By.xpath("//a[@class='fancybox-item fancybox-close']")).isDisplayed()){
	//			driver.click(By.xpath("//a[@class='fancybox-item fancybox-close']"));
	//		}
	//		driver.findElement(By.xpath("//input[@id='email-account']")).clear();
	//		return status;
	//	}

	//	public boolean validateErrorMessageForActiveConsultant(){
	//		String ActiveConsultant="con0708@yopmail.com";
	//		driver.findElement(By.xpath("//input[@id='email-account']")).sendKeys(ActiveConsultant);
	//		driver.findElement(By.xpath("//input[@id='new-password-account']")).sendKeys(password);   
	//		boolean status=driver.findElement(By.xpath("//div[@class='tipsy-inner']")).isDisplayed();
	//		if(driver.findElement(By.xpath("//a[@class='fancybox-item fancybox-close']")).isDisplayed()){
	//			driver.click(By.xpath("//a[@class='fancybox-item fancybox-close']"));
	//		}
	//		driver.findElement(By.xpath("//input[@id='email-account']")).clear();
	//		return status;
	//	}

	//	public boolean validateErrorMessageForActiveRC(){
	//		String ActiveRC="Retail29@mailinator.com";
	//		driver.findElement(By.xpath("//input[@id='email-account']")).sendKeys(ActiveRC);
	//		driver.findElement(By.xpath("//input[@id='new-password-account']")).sendKeys(password);   
	//		boolean status=driver.findElement(By.xpath("//div[@class='tipsy-inner']")).isDisplayed();
	//		if(driver.findElement(By.xpath("//a[@class='fancybox-item fancybox-close']")).isDisplayed()){
	//			driver.click(By.xpath("//a[@class='fancybox-item fancybox-close']"));
	//		}
	//		driver.findElement(By.xpath("//input[@id='email-account']")).clear();
	//		return status;
	//	}

	public boolean verifyPopUpForExistingActivePC() throws InterruptedException{
		boolean isPopForExistingAccountVisible = false;
		//Thread.sleep(5000);
		isPopForExistingAccountVisible = driver.findElement(By.xpath("//div[@id='activePCPopup']/div/div")).isDisplayed();
		if(isPopForExistingAccountVisible==true){
			driver.findElement(By.xpath("//a[contains(@class, 'fancybox-close')]")).click();
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyPopUpForExistingActiveCCLessThan6Month() throws InterruptedException{
		boolean isPopForExistingAccountVisible = false;
		driver.waitForElementPresent(CONSULTANT_VALIDATION_POPUP_LESS_THAN_6_MONTH);
		isPopForExistingAccountVisible = driver.findElement(CONSULTANT_VALIDATION_POPUP_LESS_THAN_6_MONTH).isDisplayed();
		if(isPopForExistingAccountVisible==true){
			driver.findElement(By.xpath("//a[contains(@class, 'fancybox-close')]")).click();
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyPopUpForExistingActiveRC() throws InterruptedException{
		boolean isPopForExistingAccountVisible = false;
		isPopForExistingAccountVisible = driver.findElement(By.xpath("//div[@id='activeRetailPopup']/div/div")).isDisplayed();
		if(isPopForExistingAccountVisible==true){
			driver.findElement(By.xpath("//a[contains(@class, 'fancybox-close')]")).click();
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyPopUpForExistingInactivePC90Days() throws InterruptedException{
		boolean isPopForExistingAccountVisible = false;
		isPopForExistingAccountVisible = driver.findElement(By.xpath("//div[@id='inactivePc90Popup']/div/div")).isDisplayed();
		if(isPopForExistingAccountVisible==true){
			driver.findElement(By.xpath("//a[contains(@class, 'fancybox-close')]")).click();
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyPopUpForExistingInactiveCC180Days() throws InterruptedException{
		boolean isPopForExistingAccountVisible = false;
		isPopForExistingAccountVisible = driver.findElement(By.xpath("//div[@id='inactiveConsultant180Popup']/div/div")).isDisplayed();
		if(isPopForExistingAccountVisible==true){
			driver.findElement(By.xpath("//a[contains(@class, 'fancybox-close')]")).click();

			return true;
		}else{
			return false;
		}
	}

	public boolean validateIncorrectLogin(){
		return driver.findElement(By.xpath("//p[text()='Your username or password was incorrect.']")).isDisplayed();
	}

	public boolean verifySwitchPCToUnderDifferentConsultant(){
		boolean flag = false;
		if(driver.findElement(By.id("inactivePc90Form")).isDisplayed()){
			flag = true;
			return flag;
		}else{
			return flag;
		}
	}

	public void mouseHoverSponsorDataAndClickContinueForPC() throws InterruptedException{
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.cssSelector("input[value='Select & Continue']"))).click().build().perform();
		logger.info("First result of sponsor has been clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void enterSponsorIdDuringCreationOfPC(String sponsorID){
		driver.waitForElementPresent(By.id("sponsor-name-id"));
		driver.findElement(By.id("sponsor-name-id")).sendKeys(sponsorID);
		driver.findElement(By.xpath("//input[@class='submitSponser']")).click();
	}

	public boolean validateMiniCart() {
		actions=new Actions(RFWebsiteDriver.driver);
		return driver.findElement(By.xpath("//a[@id='shopping-cart']")).isDisplayed();
	}

	public boolean clickMiniCartAndValidatePreaddedProductsOnCartPage(){
		driver.waitForElementPresent(By.xpath("//a[@id='shopping-cart']"));
		driver.click(By.xpath("//a[@id='shopping-cart']"));
		driver.waitForPageLoad();
		return driver.findElement(By.xpath("//div[@id='left-shopping']")).isDisplayed();
	}

	public void clickOnReviewAndConfirmShippingEditBtn(){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Shipping info')]/a[text()='Edit']"));
		driver.click(By.xpath("//h3[contains(text(),'Shipping info')]/a[text()='Edit']"));
	}

	public boolean isReviewAndConfirmPageContainsShippingAddress(String shippingAddress){
		driver.waitForElementPresent(By.xpath("//div[@id='summarySection']/div[2]//ul[1]/li[1]/p[1]"));
		return driver.findElement(By.xpath("//div[@id='summarySection']/div[2]//ul[1]/li[1]/p[1]")).getText().contains(shippingAddress);
	}

	public boolean isReviewAndConfirmPageContainsFirstAndLastName(String name){
		driver.waitForElementPresent(By.xpath("//div[@id='summarySection']/div[2]//ul[1]/li[1]/p[1]/span"));
		return driver.findElement(By.xpath("//div[@id='summarySection']/div[2]//ul[1]/li[1]/p[1]/span")).getText().toLowerCase().contains(name.toLowerCase());
	}

	public boolean validateUserLandsOnPWSbizSite(){
		return driver.getCurrentUrl().contains("biz");
	}

	public void cancelPulseSubscription(){
		driver.waitForElementPresent(By.xpath("//a[text()='Cancel my Pulse subscription »']"));
		driver.click(By.xpath("//a[text()='Cancel my Pulse subscription »']"));
		driver.pauseExecutionFor(2000);
		driver.click(By.xpath("//input[@id='cancel-pulse-button']"));
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public boolean validatePWS(){
		driver.waitForElementPresent(By.xpath("//div[@id='header-logo']"));
		return driver.findElement(By.xpath("//div[@id='header-logo']")).isDisplayed();
	}

	public void clickOnUSAtWelcomePage(){
		driver.click(By.xpath("//a[text()='United States']"));
	}

	public String navigateToCommercialWebsite(String bizURL){
		return bizURL.replaceFirst("biz", "com");
	}

	//	public void enterSponsorIdDuringCreationOfPC(String sponsorID){
	//		driver.waitForElementPresent(By.id("sponsor-name-id"));
	//		driver.findElement(By.id("sponsor-name-id")).sendKeys(sponsorID);
	//		driver.findElement(By.xpath("//input[@class='submitSponser']")).click();
	//	}
	//
	//	public void mouseHoverSponsorDataAndClickContinueForPC() throws InterruptedException{
	//		actions =  new Actions(RFWebsiteDriver.driver);
	//		actions.moveToElement(driver.findElement(By.cssSelector("input[value='Select & Continue']"))).click().build().perform();
	//		logger.info("First result of sponsor has been clicked");
	//		driver.waitForLoadingImageToDisappear();
	//		driver.waitForPageLoad();
	//	}

	public boolean validateShippingMethodDisclaimersForUPSGroundHD(){
		Select sel=new Select(driver.findElement(By.xpath("//select[@id='selectDeliveryModeForCRP']/../select")));
		sel.selectByValue("UPS Standard Overnight");
		driver.waitForLoadingImageToDisappear();
		return sel.getFirstSelectedOption().getText().contains("Overnight");
	}

	public boolean validatePCPerksCheckBoxIsDisplayed(){
		if(driver.findElement(By.xpath("//div[@id='pc-customer2-div-order-summary']")).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}

	public void clickOnNotYourSponsorLink(){
		driver.waitForElementPresent(By.xpath("//a[@id='not-your-sponsor']"));
		driver.click(By.xpath("//a[@id='not-your-sponsor']"));
	}
}


