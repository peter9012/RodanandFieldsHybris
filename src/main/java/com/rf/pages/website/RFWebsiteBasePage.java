package com.rf.pages.website;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Site;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.RFBasePage;


public class RFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(RFWebsiteBasePage.class.getName());

	//private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//div[@id='header-middle-top']//a");//fixed
	private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//div[@id='header-logo']//a");
	private final By WELCOME_DD_EDIT_CRP_LINK_LOC = By.xpath("//a[contains(text(),'Edit')]");
	private final By WELCOME_USER_DD_LOC = By.id("account-info-button");
	private final By WELCOME_DD_ORDERS_LINK_LOC = By.xpath("//a[text()='Orders']");
	private final By YOUR_ACCOUNT_DROPDOWN_LOC = By.xpath("//button[@class='btn btn-default dropdown-toggle']");

	protected RFWebsiteDriver driver;
	private String RFO_DB = null;
	public RFWebsiteBasePage(RFWebsiteDriver driver){		
		super(driver);
		this.driver = driver;
	}

	//contains the common methods useful for all the pages inherited

	public static String convertDBDateFormatToUIFormat(String DBDate){
		String UIMonth=null;
		String[] splittedDate = DBDate.split(" ");
		String date = (splittedDate[0].split("-")[2].charAt(0))=='0'?splittedDate[0].split("-")[2].split("0")[1]:splittedDate[0].split("-")[2];
		String month = (splittedDate[0].split("-")[1].charAt(0))=='0'?splittedDate[0].split("-")[1].split("0")[1]:splittedDate[0].split("-")[1];		
		String year = splittedDate[0].split("-")[0];		
		switch (Integer.parseInt(month)) {		
		case 1:
			UIMonth="January";
			break;
		case 2:
			UIMonth="February";
			break;
		case 3:
			UIMonth="March";
			break;
		case 4:
			UIMonth="April";
			break;
		case 5:
			UIMonth="May";
			break;
		case 6:
			UIMonth="June";
			break;
		case 7:
			UIMonth="July";
			break;
		case 8:
			UIMonth="August";
			break;
		case 9:
			UIMonth="September";
			break;
		case 10:
			UIMonth="October";
			break;
		case 11:
			UIMonth="November";
			break;
		case 12:
			UIMonth="December";
			break;		
		}

		return UIMonth+" "+date+", "+year;
	}

	public void clickOnShopLink(){
		driver.waitForElementPresent(By.id("our-products"));
		driver.click(By.id("our-products"));
		logger.info("Shop link clicked ");
	}

	public void clickOnAllProductsLink(){
		try{
			//driver.waitForElementPresent(By.xpath("//a[@title='All Products']"));
			//driver.click(By.xpath("//a[@title='All Products']"));
			driver.moveToELement(By.xpath("//*[@id='header']//A[@id='our-products']"));
			//driver.waitForElementPresent(By.xpath("//A[contains(text(),'All Products')]"));
			driver.moveToELement(By.xpath("//A[contains(text(),'All Products')]"));
			driver.click(By.xpath("//A[contains(text(),'All Products')]"));
		}catch(NoSuchElementException e){
			logger.info("All products link was not present");
			driver.click(By.xpath("//div[@id='dropdown-menu']//a[@href='/us/quick-shop/quickShop']"));
		}
		logger.info("All products link clicked "+"//a[@title='All Products']");	
		driver.waitForPageLoad();
	}

	public StoreFrontUpdateCartPage clickOnQuickShopImage(){
		driver.waitForElementToBeVisible(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"), 30);
		driver.waitForElementPresent(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"));
		driver.click(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"));
		logger.info("Quick shop Img link clicked "+"//a[@href='/us/quick-shop/quickShop' and @title='']");
		driver.waitForPageLoad();
		return new StoreFrontUpdateCartPage(driver);
	}

	public boolean areProductsDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[contains(@class,'quickshop-section')]"));
		return driver.isElementPresent(By.xpath("//div[contains(@class,'quickshop-section')]"));
	}

	public void selectProductAndProceedToBuy() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			if(driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button")).isEnabled()==true)
				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			else
				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[2]//form[@id='productDetailForm']/button"));
			logger.info("Add To Bag button clicked");
			driver.waitForLoadingImageToDisappear();
			driver.waitForPageLoad();

		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
			driver.click(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
			logger.info("Add To Bag button clicked");
			driver.waitForLoadingImageToDisappear();
			driver.waitForPageLoad();

		}

	}

	public void selectProductAndProceedToAddToCRP() throws InterruptedException{
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']"));
		if(driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']")).isEnabled()==true)
			driver.click(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']"));
		else
			driver.click(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[2]//input[@value='Add to crp']"));
		logger.info("Add to CRP button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void addQuantityOfProduct(String qty) throws InterruptedException{
		driver.waitForElementPresent(By.id("quantity0"));
		driver.findElement(By.id("quantity0")).clear();
		driver.findElement(By.id("quantity0")).sendKeys(qty);
		logger.info("quantity added is "+qty);
		driver.click(By.xpath("//a[@class='updateLink']"));
		logger.info("Update button clicked after adding quantity");
		driver.pauseExecutionFor(1000);
		driver.waitForPageLoad();
	}

	public void clickOnNextBtnAfterAddingProductAndQty() throws InterruptedException{
		driver.waitForElementPresent(By.id("submitForm"));
		driver.click(By.id("submitForm"));
		logger.info("Next button after adding quantity clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public boolean isCartPageDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/h1"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='left-shopping']/h1")));
	}

	//	public void addAnotherProduct() throws InterruptedException{
	//		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
	//		driver.click(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
	//		logger.info("Continue shopping link clicked");
	//		driver.waitForPageLoad();
	//		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//		if(driver.getCountry().equalsIgnoreCase("CA")){
	//			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//			logger.info("Buy Now button clicked and another product selected");
	//			driver.waitForPageLoad();
	//		}
	//		else if(driver.getCountry().equalsIgnoreCase("US")){
	//			driver.click(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
	//			logger.info("Buy Now button clicked and another product selected");
	//			driver.waitForPageLoad();
	//		}
	//
	//	}

	public void addAnotherProduct() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		driver.quickWaitForElementPresent(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
		action.moveToElement(driver.findElement(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"))).doubleClick(driver.findElement(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"))).build().perform();
		//driver.click(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
		logger.info("Continue shopping link clicked");
		driver.pauseExecutionFor(2000);
		driver.waitForPageLoad();
		//  driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			logger.info("Buy Now button clicked and another product selected");
			driver.waitForPageLoad();
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.waitForElementPresent(By.xpath("//div[@class='quickshop-section blue']/div[contains(@class,'quick-product')]/div[contains(@class,'product-third-module')][2]//form[@action='/us/cart/add']/button"));
			driver.click(By.xpath("//div[@class='quickshop-section blue']/div[contains(@class,'quick-product')]/div[contains(@class,'product-third-module')][2]//form[@action='/us/cart/add']/button"));
			logger.info("Buy Now button clicked and another product selected");
			driver.waitForPageLoad();
		}

	}

	public boolean verifyNumberOfProductsInCart(String numberOfProductsInCart){
		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/h1/span"));
		return driver.findElement(By.xpath("//div[@id='left-shopping']/h1/span")).getText().contains(numberOfProductsInCart);
	}

	public void clickOnCheckoutButton(){
		driver.waitForElementPresent(By.xpath("//input[@value='PLACE ORDER']"));
		driver.click(By.xpath("//input[@value='PLACE ORDER']"));
		logger.info("checkout button clicked");
		driver.waitForPageLoad();
	}

	public boolean isLoginOrCreateAccountPageDisplayed(){
		driver.waitForElementPresent(By.xpath("//h1[text()='Log in or register']"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//h1[text()='Log in or register']")));
	}

	public void enterNewRCDetails(String firstName,String lastName,String password) throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String emailAddress = firstName+randomNum+"@xyz.com";
		driver.findElement(By.id("first-Name")).sendKeys(firstName);
		logger.info("first name entered as "+firstName);
		driver.findElement(By.id("last-name")).sendKeys(lastName);
		logger.info("last name entered as "+lastName);
		driver.findElement(By.id("email-account")).sendKeys(emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(1000);
		driver.waitForSpinImageToDisappear();
		driver.findElement(By.id("password")).sendKeys(password);
		logger.info("password entered as "+password);
		driver.findElement(By.id("the-password-again")).sendKeys(password);
		logger.info("confirm password entered as "+password);
		driver.click(By.id("next-button"));		
		logger.info("Create New Account button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	//overloaded method for email address
	public void enterNewRCDetails(String firstName,String lastName,String emailAddress,String password) throws InterruptedException{
		driver.findElement(By.id("first-Name")).sendKeys(firstName);
		logger.info("first name entered as "+firstName);
		driver.findElement(By.id("last-name")).sendKeys(lastName);
		logger.info("last name entered as "+lastName);
		driver.findElement(By.id("email-account")).sendKeys(emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(1000);
		driver.waitForSpinImageToDisappear();
		driver.findElement(By.id("password")).sendKeys(password);
		logger.info("password entered as "+password);
		driver.findElement(By.id("the-password-again")).sendKeys(password);
		logger.info("confirm password entered as "+password);
		driver.click(By.id("next-button"));  
		logger.info("Create New Account button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void enterNewPCDetails(String firstName,String lastName,String password) throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String emailAddress = firstName+randomNum+"@xyz.com";
		driver.findElement(By.id("first-Name")).sendKeys(firstName);
		logger.info("first name entered as "+firstName);
		driver.findElement(By.id("last-name")).sendKeys(lastName);
		logger.info("last name entered as "+lastName);
		driver.findElement(By.id("email-account")).sendKeys(emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(1000);
		driver.waitForSpinImageToDisappear();
		driver.findElement(By.id("password")).sendKeys(password);
		logger.info("password entered as "+password);
		driver.findElement(By.id("the-password-again")).sendKeys(password);
		logger.info("confirm password entered as "+password);
		driver.click(By.xpath("//input[@id='become-pc']/.."));
		logger.info("check box for PC user checked");
		driver.click(By.xpath("//input[@id='next-button']"));		
		logger.info("Create New Account button clicked");		
	}

	public void enterNewPCDetails(String firstName,String lastName,String password, String emailID) throws InterruptedException{
		driver.findElement(By.id("first-Name")).sendKeys(firstName);
		logger.info("first name entered as "+firstName);
		driver.findElement(By.id("last-name")).sendKeys(lastName);
		logger.info("last name entered as "+lastName);
		driver.findElement(By.id("email-account")).sendKeys(emailID+"\t");
		logger.info("email entered as "+emailID);
		driver.pauseExecutionFor(1000);
		driver.waitForSpinImageToDisappear();
		driver.findElement(By.id("password")).sendKeys(password);
		logger.info("password entered as "+password);
		driver.findElement(By.id("the-password-again")).sendKeys(password);
		logger.info("confirm password entered as "+password);
		driver.click(By.xpath("//input[@id='become-pc']/.."));
		logger.info("check box for PC user checked");
		driver.click(By.xpath("//input[@id='next-button']"));  
		logger.info("Create New Account button clicked");  
	}

	public boolean isPopUpForPCThresholdPresent() throws InterruptedException{
		boolean isPopUpForPCThresholdPresent=false;
		driver.waitForElementPresent(By.xpath("//div[@id='popup-content']//p[contains(text(),'Please add products')]"));
		isPopUpForPCThresholdPresent = driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='popup-content']//p[contains(text(),'Please add products')]")));
		if(isPopUpForPCThresholdPresent==true){
			driver.click(By.xpath("//div[@id='popup-content']//input"));
			return true;
		}
		return false;
	}

	public boolean isCheckoutPageDisplayed(){
		driver.waitForElementPresent(By.xpath("//h1[contains(text(),'Checkout')]"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//h1[contains(text(),'Checkout')]")));
	}

	public void enterMainAccountInfo(){
		driver.pauseExecutionFor(5000);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.findElement(By.id("address.line1")).sendKeys(TestConstants.ADDRESS_LINE_1_CA);
			logger.info("Address Line 1 entered is "+TestConstants.ADDRESS_LINE_1_CA);
			driver.findElement(By.id("address.townCity")).sendKeys(TestConstants.CITY_CA);
			logger.info("City entered is "+TestConstants.CITY_CA);
			driver.click(By.id("state"));
			driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
			driver.click(By.xpath("//select[@id='state']/option[2]"));
			logger.info("state selected");
			driver.findElement(By.id("address.postcode")).sendKeys(TestConstants.POSTAL_CODE_CA);
			logger.info("postal code entered is "+TestConstants.POSTAL_CODE_CA);
			driver.findElement(By.id("address.phonenumber")).sendKeys(TestConstants.PHONE_NUMBER);
			logger.info("phone number entered is "+TestConstants.PHONE_NUMBER);
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.findElement(By.id("address.line1")).sendKeys(TestConstants.ADDRESS_LINE_1_US);
			logger.info("Address line 1 entered is "+TestConstants.ADDRESS_LINE_1_US);
			driver.findElement(By.id("address.townCity")).sendKeys(TestConstants.CITY_US);
			driver.click(By.id("state"));
			driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
			driver.click(By.xpath("//select[@id='state']/option[2]"));
			logger.info("state selected");
			driver.findElement(By.id("address.postcode")).sendKeys(TestConstants.POSTAL_CODE_US);
			logger.info("postal code entered is "+TestConstants.POSTAL_CODE_US);
			driver.findElement(By.id("address.phonenumber")).sendKeys(TestConstants.PHONE_NUMBER_US);
			logger.info("phone number entered is "+TestConstants.PHONE_NUMBER_US);
		}

	}

	public void enterMainAccountInfo(String address1,String city,String province,String postalCode,String phoneNumber){
		driver.waitForElementPresent(By.id("address.line1"));
		driver.findElement(By.id("address.line1")).sendKeys(address1);
		logger.info("Address Line 1 entered is "+address1);
		driver.findElement(By.id("address.townCity")).sendKeys(city);
		logger.info("City entered is "+city);
		driver.waitForElementPresent(By.id("state"));
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[contains(text(),'"+province+"')]"));
		driver.click(By.xpath("//select[@id='state']/option[contains(text(),'"+province+"')]"));
		logger.info("state selected");
		driver.findElement(By.id("address.postcode")).sendKeys(postalCode);
		logger.info("postal code entered is "+postalCode);
		driver.findElement(By.id("address.phonenumber")).sendKeys(phoneNumber);
		logger.info("phone number entered is "+phoneNumber);
	}

	public void clickOnContinueWithoutSponsorLink() throws InterruptedException{
		driver.waitForElementPresent(By.id("continue-no-sponsor"));
		driver.click(By.id("continue-no-sponsor"));	
		logger.info("continue without sponsor link clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnNextButtonAfterSelectingSponsor() throws InterruptedException{
		driver.waitForElementPresent(By.id("saveAccountAddress"));
		driver.click(By.id("saveAccountAddress"));
		logger.info("Next button after selecting sponsor is clicked");
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.id("QAS_AcceptOriginal"));
			driver.click(By.id("QAS_AcceptOriginal"));
			logger.info("Accept as original button clicked");
			driver.waitForLoadingImageToDisappear();
		}catch(NoSuchElementException e){

		}		
	}

	public void clickOnShippingAddressNextStepBtn() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.id("saveShippingInfo"));
		action.moveToElement(driver.findElement(By.id("saveShippingInfo"))).click(driver.findElement(By.id("saveShippingInfo"))).build().perform();
		logger.info("Next button on shipping address clicked");		
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(20000);
	}

	public void enterNewBillingCardNumber(String cardNumber){
		driver.waitForElementPresent(By.id("card-nr"));
		driver.type(By.id("card-nr"), cardNumber);
		logger.info("Billing card number entered is "+cardNumber);
	}

	public void enterNewBillingNameOnCard(String nameOnCard){
		driver.waitForElementPresent(By.id("card-name"));
		driver.findElement(By.id("card-name")).clear();
		driver.findElement(By.id("card-name")).sendKeys(nameOnCard);
		logger.info("card name entered is "+nameOnCard);
	}

	public void selectNewBillingCardExpirationDate(){
		driver.click(By.id("expiryMonth"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[10]"));
		driver.click(By.xpath("//select[@id='expiryMonth']/option[10]"));
		driver.click(By.id("expiryYear"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[10]"));
		driver.click(By.xpath("//select[@id='expiryYear']/option[10]"));
		logger.info("expiration date is selected");
	}

	public void enterNewBillingSecurityCode(String securityCode){
		driver.type(By.id("security-code"), securityCode);
		logger.info("security code entered is "+securityCode);
	}

	public void selectNewBillingCardAddress() throws InterruptedException{
		driver.waitForElementPresent(By.id("addressBookdropdown"));
		driver.click(By.id("addressBookdropdown"));
		driver.click(By.xpath("//*[@id='addressBookdropdown']/option[1]"));
		logger.info("New Billing card address selected");
	}

	public void clickOnSaveBillingProfile() throws InterruptedException{
		driver.waitForElementPresent(By.id("submitButton"));
		driver.click(By.id("submitButton"));
		driver.waitForLoadingImageToDisappear();
		logger.info("Save billing profile button clicked");
	}



	public void clickOnBillingNextStepBtn() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='payment-next-button']/input"));
		driver.click(By.xpath("//div[@id='payment-next-button']/input"));
		logger.info("Next button on billing profile clicked");	
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}

	public void clickOnSetupCRPAccountBtn() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@value='Setup CRP Account']"));
		driver.click(By.xpath("//ul[@style='cursor: pointer;']/li[1]/div"));
		driver.click(By.xpath("//ul[@style='cursor: pointer;']/li[2]/div"));
		driver.click(By.xpath("//ul[@style='cursor: pointer;']/li[3]/div"));
		driver.click(By.xpath("//input[@value='Setup CRP Account']"));
		logger.info("Next button on billing profile clicked");

	}

	public void clickPlaceOrderBtn()throws InterruptedException{
		driver.waitForElementPresent(By.id("placeOrderButton"));
		driver.click(By.id("placeOrderButton"));
		logger.info("Place order button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();		
	}

	public void switchToPreviousTab(){
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		driver.close();
		driver.switchTo().window(tabs.get(0));
		driver.pauseExecutionFor(1000);
	}

	public void clickOnRodanAndFieldsLogo(){
		try{
			driver.turnOffImplicitWaits();
			driver.quickWaitForElementPresent(RODAN_AND_FIELDS_IMG_LOC);
			driver.click(RODAN_AND_FIELDS_IMG_LOC);
		}catch(NoSuchElementException e){
			try{
				driver.click(By.xpath("//img[@title='Rodan+Fields']"));
			}catch(NoSuchElementException e1){
				driver.click(By.xpath("//div[@id='header-middle-top']//a"));
			}
		}
		finally{
			driver.turnOnImplicitWaits();
		}
		logger.info("Rodan and Fields logo clicked");	
		driver.waitForPageLoad();
	}

	public boolean isOrderPlacedSuccessfully(){
		driver.waitForElementPresent(By.xpath("//h1[text()='Thank you for your order']"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//h1[text()='Thank you for your order']")));
	}

	public boolean verifyWelcomeDropdownToCheckUserRegistered(){		
		driver.waitForElementPresent(By.id("account-info-button"));
		return driver.isElementPresent(By.id("account-info-button"));
		//driver.findElement(By.xpath("//div[@id='account-info-button']/a")).getText().contains("Welcome");
	}

	public void applyPriceFilterLowToHigh() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//select[@id='sortOptions']"));
		driver.click(By.xpath("//select[@id='sortOptions']"));
		driver.click(By.xpath("//select[@id='sortOptions']/option[3]"));
		logger.info("filter done for low to high price");
	}

	public boolean verifyPCPerksTermsAndConditionsPopup() throws InterruptedException{
		boolean isPCPerksTermsAndConditionsPopup = false;
		driver.waitForElementPresent(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']"));
		isPCPerksTermsAndConditionsPopup = driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']")));
		if(isPCPerksTermsAndConditionsPopup==true){
			driver.click(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']"));
			return true;
		}
		return false;
	}

	public void clickOnPCPerksTermsAndConditionsCheckBoxes(){
		//driver.waitForElementToBeClickable(By.xpath("//form[@id='placeOrderForm1']/ul/div[@class='content'][1]/li[1]//input"), 15);
		driver.pauseExecutionFor(3000);
		driver.click(By.xpath("//form[@id='placeOrderForm1']/ul/div[@class='content'][1]/li[1]//input/.."));
		driver.click(By.xpath("//form[@id='placeOrderForm1']/ul/div[@class='content'][1]/li[2]//input/.."));
	}

	public void selectNewBillingCardExpirationDateAsExpiredDate(){
		boolean flag  = false;
		String value = null;
		driver.click(By.id("expiryMonth"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[@value='02']"));
		driver.click(By.xpath("//select[@id='expiryMonth']/option[@value='02']"));
		driver.click(By.id("expiryYear"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[2]"));
		driver.click(By.xpath("//select[@id='expiryYear']/option[1]"));
	}

	public boolean validatePasswordFieldMessage(){
		if(driver.findElement(By.xpath("//div[contains(text(),'Please enter 6')]")).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}
	public void clearPasswordField(){
		driver.findElement(By.id("new-password-account")).clear();
		logger.info("password field cleared");
	}

	public boolean recurringMonthlyChargesSection() {
		driver.quickWaitForElementPresent(By.xpath("//h3[contains(text(),'Recurring Monthly Charges')]"));
		return driver.findElement(By.xpath("//h3[contains(text(),'Recurring Monthly Charges')]")).isDisplayed();
	}

	public boolean pulseSubscriptionTextbox() {
		driver.waitForElementPresent(By.id("webSitePrefix"));
		return driver.findElement(By.id("webSitePrefix")).isEnabled();
	}

	public void clickOnAllowMySpouseOrDomesticPartnerCheckbox() {
		//driver.waitForElementToBeVisible(By.xpath("//input[@id='spouse-check']"), 15);
		boolean status=driver.findElement(By.xpath("//input[@id='spouse-check']/..")).isSelected();
		if(status==false){
			driver.click(By.xpath("//input[@id='spouse-check']/.."));
		}
	}

	public void enterSpouseFirstName(String firstName){
		driver.waitForElementPresent(By.id("spouse-first"));
		driver.findElement(By.id("spouse-first")).sendKeys(firstName);
		logger.info("Spouse first name entered as "+firstName);
	}

	public void enterSpouseLastName(String firstName){
		driver.waitForElementPresent(By.id("spouse-last"));
		driver.findElement(By.id("spouse-last")).sendKeys(firstName);
		logger.info("Spouse last name entered as "+firstName);
	}

	public void clickOnRequestASponsorBtn(){
		driver.waitForElementPresent(By.xpath("//input[@value='Request a sponsor']"));
		driver.click(By.xpath("//input[@value='Request a sponsor']"));
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOKOnSponsorInformationPopup(){
		driver.pauseExecutionFor(2000);
		//   driver.waitForElementToBeVisible(By.xpath("//div[@id='sponsorMessage']//div[@id='popup-sponsorMessage']//input[contains(@value,'OK')]"), 15);
		driver.waitForElementPresent(By.xpath("//div[@id='confirm-left-shopping']//div[@id='popup']//input[@value ='OK ']"));
		driver.click(By.xpath("//div[@id='confirm-left-shopping']//div[@id='popup']//input[@value ='OK ']"));
	}

	public void clickYesIWantToJoinPCPerksCB(){
		driver.waitForElementPresent(By.id("pc-customer2-div-order-summary"));
		driver.click(By.id("pc-customer2-div-order-summary"));
	}

	public void checkIAcknowledgePCAccountCheckBox(){
		try{
			driver.waitForElementPresent(By.xpath("//input[@id='Terms2']/.."));
			driver.click(By.xpath("//input[@id='Terms2']/.."));  
			logger.info("I Acknowledge PC Account Checkbox selected");
		}catch(Exception e){
			driver.waitForElementPresent(By.xpath("//form[@id='placeOrderForm1']/ul/div[1]/li[1]/div"));
			driver.click(By.xpath("//form[@id='placeOrderForm1']/ul/div[1]/li[1]/div"));  
			logger.info("I Acknowledge PC Account Checkbox selected");
		}
	}

	public void checkPCPerksTermsAndConditionsCheckBox(){
		try{
			driver.waitForElementPresent(By.xpath("//input[@id='Terms3']/.."));
			driver.click(By.xpath("//input[@id='Terms3']/.."));  
			logger.info("PC Perks terms and condition checkbox selected");
		}catch(Exception e){
			driver.waitForElementPresent(By.xpath("//form[@id='placeOrderForm1']/ul/div[1]/li[2]/div"));
			driver.click(By.xpath("//form[@id='placeOrderForm1']/ul/div[1]/li[2]/div"));  
			logger.info("PC Perks terms and condition checkbox selected");
		}
	}

	public boolean isWelcomePCPerksMessageDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[@id='Congrats']/h1[contains(text(),'PC')]"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='Congrats']/h1[contains(text(),'PC')]")));
	}

	public String createNewPC(String firstName,String lastName,String password) throws InterruptedException{
		String emailAddress = firstName+"@xyz.com";
		driver.findElement(By.id("first-Name")).sendKeys(firstName);
		logger.info("first name entered as "+firstName);
		driver.findElement(By.id("last-name")).sendKeys(lastName);
		logger.info("last name entered as "+lastName);
		driver.findElement(By.id("email-account")).sendKeys(emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(2000);
		driver.findElement(By.id("password")).sendKeys(password);
		logger.info("password entered as "+password);
		driver.findElement(By.id("the-password-again")).sendKeys(password);
		logger.info("confirm password entered as "+password);
		driver.click(By.xpath("//input[@id='become-pc']/.."));
		logger.info("check box for PC user checked");
		driver.click(By.xpath("//input[@id='next-button']"));  
		logger.info("Create New Account button clicked"); 
		return emailAddress;
	}

	public String createNewRC(String firstName,String lastName,String password){	
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		//		String firstName="RCUser";
		//		String lastName = "Test";
		String emailAddress = firstName+randomNum+"@xyz.com";
		driver.findElement(By.id("first-Name")).sendKeys(firstName);
		logger.info("first name entered as "+firstName);
		driver.findElement(By.id("last-name")).sendKeys(lastName);
		logger.info("last name entered as "+lastName);
		driver.findElement(By.id("email-account")).sendKeys(emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(2000);
		driver.findElement(By.id("password")).sendKeys(password);
		logger.info("password entered as "+password);
		driver.findElement(By.id("the-password-again")).sendKeys(password);
		logger.info("confirm password entered as "+password);
		driver.click(By.id("next-button"));  
		logger.info("Create New Account button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return emailAddress;
	}

	public StoreFrontCartAutoShipPage clickEditCrpLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_DD_EDIT_CRP_LINK_LOC);
		driver.click(WELCOME_DD_EDIT_CRP_LINK_LOC);
		logger.info("User has clicked on edit Crp link from welcome drop down");
		driver.waitForPageLoad();
		return new StoreFrontCartAutoShipPage(driver);
	}

	public boolean validateExistingUserPopUp(String userid,String firstName,String lastName,String password,String countryId){
		RFO_DB = driver.getDBNameRFO();
		String pcmailid=null;
		String rcmailid=null;
		String consultantmailid=null;

		List<Map<String, Object>> randomPCUserEmailIdList =  null;
		List<Map<String, Object>> randomRCUserEmailIdList =  null;
		List<Map<String, Object>> randomConsultantEmailIdList =  null;
		driver.findElement(By.id("first-Name")).sendKeys(firstName);
		logger.info("first name entered as "+firstName);
		driver.findElement(By.id("last-name")).sendKeys(lastName);
		logger.info("last name entered as "+lastName);
		if(userid.equalsIgnoreCase("pc")){
			randomPCUserEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcmailid = String.valueOf(getValueFromQueryResult(randomPCUserEmailIdList, "Username"));

			driver.findElement(By.id("email-account")).sendKeys(pcmailid+"\t");
			logger.info("email entered as "+pcmailid);
		}else if(userid.equalsIgnoreCase("rc")){
			randomRCUserEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcmailid = String.valueOf(getValueFromQueryResult(randomRCUserEmailIdList, "Username"));

			driver.findElement(By.id("email-account")).sendKeys(rcmailid+"\t");
			logger.info("email entered as "+rcmailid);
		}else{
			randomConsultantEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantmailid = String.valueOf(getValueFromQueryResult(randomConsultantEmailIdList, "Username"));
			driver.findElement(By.id("email-account")).sendKeys(consultantmailid+"\t");
			logger.info("email entered as "+consultantmailid);
		}
		driver.pauseExecutionFor(1000);
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		return driver.isElementPresent(By.xpath("//div[@class='fancybox-inner']"));
	}


	public boolean validateSendMailToResetMyPasswordFunctionalityPC(){
		driver.waitForElementPresent(By.xpath("//div[@id='notavailablePopup']//input[@class='resetPasswordEmail']"));
		JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[@id='notavailablePopup']//input[@class='resetPasswordEmail']")));
		driver.pauseExecutionFor(1000);
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		return driver.isElementPresent(By.xpath("//div[@class='fancybox-inner']"));

	}

	public boolean validateCancelEnrollmentFunctionalityPC(){
		driver.waitForElementPresent(By.xpath("//div[@id='activePCPopup']//input[@class='cancelEnrollment']"));
		driver.click(By.xpath("//div[@id='activePCPopup']//input[@class='cancelEnrollment']"));
		driver.pauseExecutionFor(1000);
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		driver.waitForPageLoad();
		return driver.isElementPresent(By.xpath("//div[@class='fancybox-inner']"));
	}

	public boolean validateSendMailToResetMyPasswordFunctionalityRC(){
		try{
			driver.waitForElementPresent(By.xpath(" //div[@id='activeRetailPopup']//input[contains(@class,'resetPasswordEmail')]"));
			JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
			js.executeScript("arguments[0].click();", driver.findElement(By.xpath(" //div[@id='activeRetailPopup']//input[contains(@class,'resetPasswordEmail')]")));
			//driver.findElement(By.xpath("//div[@id='notavailablePopup']//input[@class='resetPasswordEmail']")).click();
			driver.pauseExecutionFor(2000);
			driver.waitForLoadingImageToDisappear();
			driver.pauseExecutionFor(2000);
			return validateHomePage();			
		}catch(NoSuchElementException e){
			return false;
		}	
	}


	public boolean validateCancelEnrollmentFunctionality(){
		driver.waitForElementPresent(By.xpath("//div[@id='activeRetailPopup']//input[contains(@class,'cancelEnrollment')]"));
		driver.click(By.xpath("//div[@id='activeRetailPopup']//input[contains(@class,'cancelEnrollment')]"));
		driver.pauseExecutionFor(2000);
		driver.waitForLoadingImageToDisappear();
		return validateHomePage();
	}


	public boolean validateSendMailToResetMyPasswordFunctionalityConsultant(){
		try{
			driver.waitForElementPresent(By.xpath("//div[@id='notavailablePopup']//input[@class='resetPasswordEmail']"));
			JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
			js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[@id='notavailablePopup']//input[@class='resetPasswordEmail']")));
			//driver.findElement(By.xpath("//div[@id='notavailablePopup']//input[@class='resetPasswordEmail']")).click();
			driver.pauseExecutionFor(2000);
			driver.waitForLoadingImageToDisappear();
			driver.pauseExecutionFor(2000);
			return validateHomePage();			
		}catch(NoSuchElementException e){
			return driver.isElementPresent(By.xpath("//div[@id='notavailablePopup']//input[@class='resetPasswordEmail']"));
		}
	}

	public boolean validateCancelEnrollmentFunctionalityConsultant(){
		driver.waitForElementPresent(By.xpath("//div[@id='notavailablePopup']//input[@class='cancelEnrollment']"));
		driver.click(By.xpath("//div[@id='notavailablePopup']//input[@class='cancelEnrollment']"));
		driver.pauseExecutionFor(2000);
		driver.waitForLoadingImageToDisappear();
		return validateHomePage();
	}

	public boolean validateHomePage(){
		driver.waitForPageLoad();
		String url = driver.getURL();
		String currentURL = driver.getCurrentUrl();
		System.out.println(currentURL);
		return driver.getCurrentUrl().contains(url);
	}

	public void navigateToBackPage(){
		driver.waitForPageLoad();
		driver.navigate().back();
	}

	public void clickOnAllowMySpouseOrDomesticPartnerCheckboxForUncheck() {
		//driver.waitForElementToBeVisible(By.xpath("//input[@id='spouse-check']"), 15);
		boolean status=driver.findElement(By.xpath("//input[@id='spouse-check']/..")).isSelected();
		if(status==true){
			driver.click(By.xpath("//input[@id='spouse-check']/.."));
		}
	}

	public void cancelTheProvideAccessToSpousePopup(){
		driver.pauseExecutionFor(6000);
		if(driver.findElement(By.id("cancelSpouse")).isDisplayed()){
			driver.click(By.id("cancelSpouse"));
			driver.pauseExecutionFor(3000);
		}
	}

	public boolean verifyAllowMySpouseCheckBoxIsSelectedOrNot(){
		logger.info("Checkbox status "+driver.findElement(By.id("spouse-check")).isSelected());
		driver.waitForElementPresent(By.id("spouse-check"));
		if(driver.findElement(By.id("spouse-check")).getAttribute("class").equalsIgnoreCase("checked")){
			return true;
		}else{
			return false;
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

	public String getBizPWS(String country,String env){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomActiveSitePrefixList =  null;
		String activeSitePrefix = null;
		String PWS = null;
		String countryID =null;

		if(country.equalsIgnoreCase("ca")){
			countryID="40";
		}
		else if(country.equalsIgnoreCase("us")){
			countryID="236";
		} 
		randomActiveSitePrefixList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_SITE_PREFIX_RFO,countryID),RFO_DB);
		activeSitePrefix = (String) getValueFromQueryResult(randomActiveSitePrefixList, "SitePrefix");			
		PWS = "http://"+activeSitePrefix+".myrfo"+env+".biz/"+country.toLowerCase();
		logger.info("PWS is "+PWS);
		return PWS;
	}

	public void openPWSSite(String country,String env){
		while(true){
			driver.get(getBizPWS(country, env));
			driver.waitForPageLoad();
			if(driver.getCurrentUrl().contains("sitenotfound"))
				continue;
			else
				break;
		}	
	}

	public void switchToChildWindow(){
		driver.switchToSecondWindow();
	}

	public void clickCheckMyPulseLinkPresentOnWelcomeDropDown(){
		driver.waitForElementPresent(By.xpath("//a[text()='Check My Pulse']"));
		driver.click(By.xpath("//a[text()='Check My Pulse']"));
		driver.pauseExecutionFor(5000);
	}

	public boolean validatePulseHomePage(){
		System.out.println("current url is "+driver.getCurrentUrl());
		return driver.getCurrentUrl().contains("pulse");
	}

	public void hoverOnShopLinkAndClickAllProductsLinksAfterLogin(){
		Actions actions = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.id("our-products")); 
		WebElement shopSkinCare = driver.findElement(By.id("our-products"));
		actions.moveToElement(shopSkinCare).pause(1000).click().build().perform();
		WebElement allProducts = driver.findElement(By.xpath("//ul[@id='dropdown-menu' and @style='display: block;']//a[text()='All Products']"));
		actions.moveToElement(allProducts).pause(1000).build().perform();
		while(true){
			try{
				driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath(" //ul[@id='dropdown-menu' and @style='display: block;']//a[text()='All Products']")));

				break;
			}catch(Exception e){
				System.out.println("element not clicked..trying again");
				actions.moveToElement(shopSkinCare).pause(1000).click().build().perform();

			}
		}
		logger.info("All products link clicked "); 
		driver.waitForPageLoad();
	}

	public void hoverOnShopLinkAndClickAllProductsLinks(){
		Actions actions = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.id("our-products")); 
		WebElement shopSkinCare = driver.findElement(By.id("our-products"));
		actions.moveToElement(shopSkinCare).pause(1000).click().build().perform();
		WebElement allProducts = driver.findElement(By.xpath("//ul[@id='dropdown-menu' and @style='display: block;']//a[text()='All Products']"));
		actions.moveToElement(allProducts).pause(1000).build().perform();
		while(true){
			try{
				driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath(" //ul[@id='dropdown-menu' and @style='display: block;']//a[text()='All Products']")));

				break;
			}catch(Exception e){
				System.out.println("element not clicked..trying again");
				actions.moveToElement(shopSkinCare).pause(1000).click().build().perform();

			}
		}
		logger.info("All products link clicked "); 
		driver.waitForPageLoad();
	}

	public void clickOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_USER_DD_LOC);
		driver.pauseExecutionFor(2000);
		driver.click(WELCOME_USER_DD_LOC);
		logger.info("clicked on welcome drop down");		
	}

	public StoreFrontOrdersPage clickOrdersLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_DD_ORDERS_LINK_LOC);
		driver.click(WELCOME_DD_ORDERS_LINK_LOC);
		logger.info("User has clicked on orders link from welcome drop down");
		return new StoreFrontOrdersPage(driver);
	}

	public void clickOnYourAccountDropdown(){
		driver.click(YOUR_ACCOUNT_DROPDOWN_LOC);
		logger.info("Your accountdropdown clicked from left panel clicked "+YOUR_ACCOUNT_DROPDOWN_LOC);
	}

	public void clickOnPlaceOrderButton(){
		driver.waitForElementPresent(By.xpath("//input[@value='PLACE ORDER']"));
		driver.click(By.xpath("//input[@value='PLACE ORDER']"));
		logger.info("Place order button clicked");
		driver.waitForPageLoad();
	}

	public boolean verifyUpradingToConsulTantPopup(){
		driver.waitForPageLoad();
		if(driver.isElementPresent(By.xpath("//div[@id='activePCPopup']//h2[contains(text(),'UPGRADING TO A CONSULTANT')]"))){
			return true;
		}else
			return false;
	}

	public void enterPasswordForUpgradePcToConsultant(){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Log In to Terminate My PC Account')]/following::input[2]"));
		driver.type(By.xpath("//h3[contains(text(),'Log In to Terminate My PC Account')]/following::input[2]"), driver.getPassword());
	}

	public void clickOnLoginToTerminateToMyPCAccount(){
		//driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Log In to Terminate My PC Account')]/following::a[2]/input"));
		driver.click(By.xpath("//h3[contains(text(),'Log In to Terminate My PC Account')]/following::a[2]/input"));
		driver.waitForPageLoad();
		driver.pauseExecutionFor(3000);
	}

	public void navigateToCountry(){
		driver.waitForElementPresent(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//button"));
		String defaultSelectedCountry= driver.findElement(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//button")).getText();
		driver.click(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//button"));
		if(defaultSelectedCountry.contains("USA")){
			driver.waitForElementPresent(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//a[contains(text(),'CAN')]"));
			driver.click(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//a[contains(text(),'CAN')]"));
			logger.info("navigated to canada site successfully");
		}else{
			driver.waitForElementPresent(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//a[contains(text(),'USA')]"));
			driver.click(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//a[contains(text(),'USA')]"));
			logger.info("navigated to USA site successfully");
		}
		driver.waitForPageLoad();
	}

	public void clickAddToBagButton(){
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
		driver.click(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
		logger.info("Add To Bag button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();

	}

	public void clickAddToBagButton(String country){
		if(country.equalsIgnoreCase("CA")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div[1]/div[2]/div[1]//button"));
			if(driver.findElement(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div[1]/div[2]/div[1]//button")).isEnabled()==true)
				driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div[1]/div[2]/div[1]//button"));
			else
				driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div[2]/div[2]/div[1]//button"));
			logger.info("Add To Bag button clicked");
			driver.waitForLoadingImageToDisappear();
		}else if(country.equalsIgnoreCase("US")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div[1]/div[2]/div[1]//button"));
			if(driver.findElement(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div[1]/div[2]/div[1]//button")).isEnabled()==true)
				driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div[1]/div[2]/div[1]//button"));
			else
				driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div[2]/div[2]/div[1]//button"));
			logger.info("Add To Bag button clicked");
			driver.waitForLoadingImageToDisappear();
		}
	}

	public void clickOnEditAtAutoshipTemplate(){
		driver.waitForElementPresent(By.xpath("//input[@value='Edit']"));
		driver.click(By.xpath("//input[@value='Edit']"));
	}

	public String getQuantityOfProductFromAutoshipTemplate(){
		String quantity = driver.findElement(By.xpath("//div[@class='order-summary-left spacer'][2]/div[1]/div[2]/div[2]/div[3]")).getText();
		return quantity;
	}

	public void clickOnBillingNextStepButton() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='start-shipping-method']/div[2]/div/input"));
		driver.click(By.xpath("//*[@id='start-shipping-method']/div[2]/div/input"));
		logger.info("Next button on clicked"); 
		driver.waitForLoadingImageToDisappear();
		driver.waitForElementPresent(By.xpath("//div[@id='payment-next-button']/input"));
		driver.click(By.xpath("//div[@id='payment-next-button']/input"));
	}

	public boolean verifyConsultantCantShipToQuebecMsg(){
		String message = driver.findElement(By.xpath("//div[@id='errorQCEnrollDiv']")).getText();
		if(message.equals("Consultants cannot ship to Quebec.")){
			return true;
		}
		else{
			return false;
		}
	}

	public void clickOnAccountInfoNextButton(){
		driver.waitForElementPresent(By.id("clickNext"));
		driver.click(By.id("clickNext"));
	}	

	public boolean verifyEditPcPerksIsPresentInWelcomDropdownForUpgrade(){
		if(driver.isElementPresent(By.xpath("//a[text()='Edit PC Perks']"))){
			return true;
		}else
			return false;
	}
}