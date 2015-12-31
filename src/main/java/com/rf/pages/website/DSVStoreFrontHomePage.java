package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class DSVStoreFrontHomePage extends DSVRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(DSVStoreFrontHomePage.class.getName());

	private static final By LOGIN_LINK = By.xpath("//li[@id='log-in-button']/a");
	private static final By USERNAME_TXTFIELD = By.id("username");
	private static final By PASSWORD_TXTFIELD = By.id("password");
	private static final By LOGIN_BTN = By.xpath("//input[@value='Log in']");
	private static final By WELCOME_TXT = By.xpath("//li[@id='account-info-button']/a");
	private static final By CRP_CART_IMG = By.id("bag-special");
	private static final By NXT_CRP_TXT = By.xpath("//div[@id='bag-special']/following-sibling::div[1]");
	private static final By WELCOME_DROP_DOWN = By.xpath("//li[@id='account-info-button']/a"); 
	private static final By SHIPPING_INFO_LINK_WELCOME_DROP_DOWN = By.xpath("//div[@id='account-info']//a[text()='Shipping Info']");
	private static final By BILLING_INFO_LINK_WELCOME_DROP_DOWN = By.xpath("//div[@id='account-info']//a[text()='Billing Info']");
	private static final By ACCOUNT_INFO_LINK_WELCOME_DROP_DOWN = By.xpath("//div[@id='account-info']//a[text()='Account Info']");

	public DSVStoreFrontHomePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public void clickLoginLink(){
		driver.quickWaitForElementPresent(LOGIN_LINK);
		driver.click(LOGIN_LINK);
	}

	public boolean isLoginLinkPresent(){
		return driver.isElementPresent(LOGIN_LINK);
	}

	public void enterUsername(String username){
		driver.quickWaitForElementPresent(USERNAME_TXTFIELD);
		driver.type(USERNAME_TXTFIELD, username);
	}

	public void enterPassword(String password){
		driver.type(PASSWORD_TXTFIELD, password);
	}

	public void clickLoginBtn(){
		driver.click(LOGIN_BTN);
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public String getWelcomeText(){
		return driver.findElement(WELCOME_TXT).getText();
	}

	public boolean isCRPCartImagePresent(){
		return driver.isElementPresent(CRP_CART_IMG);
	}

	public String getNextCRPText(){
		return driver.findElement(NXT_CRP_TXT).getText();
	}

	public DSVStoreFrontAutoshipCartPage clickOnCRPCartImg(){
		driver.quickWaitForElementPresent(CRP_CART_IMG);
		driver.click(CRP_CART_IMG);
		driver.pauseExecutionFor(2000);
		driver.waitForPageLoad();
		return new DSVStoreFrontAutoshipCartPage(driver);
	}

	public void clickWelcomeDropDown(){
		driver.quickWaitForElementPresent(WELCOME_DROP_DOWN);
		driver.click(WELCOME_DROP_DOWN);		
	}

	public DSVStoreFrontShippingInfoPage clickShippingInfoLinkFromWelcomeDropDown(){
		driver.quickWaitForElementPresent(SHIPPING_INFO_LINK_WELCOME_DROP_DOWN);
		driver.click(SHIPPING_INFO_LINK_WELCOME_DROP_DOWN);
		return new DSVStoreFrontShippingInfoPage(driver);
	}

	public DSVStoreFrontBillingInfoPage clickBillingInfoLinkFromWelcomeDropDown(){
		driver.quickWaitForElementPresent(BILLING_INFO_LINK_WELCOME_DROP_DOWN);
		driver.click(BILLING_INFO_LINK_WELCOME_DROP_DOWN);
		return new DSVStoreFrontBillingInfoPage(driver);
	}

	public String convertComToBizOrBizToComURL(String pws){
		if(pws.contains("com"))
			return pws = pws.replaceAll("com", "biz");
		else
			return pws = pws.replaceAll("biz", "com");
	}

	public DSVStoreFrontAccountInfoPage clickAccountInfoLinkFromWelcomeDropDown(){
		driver.quickWaitForElementPresent(ACCOUNT_INFO_LINK_WELCOME_DROP_DOWN);
		driver.click(ACCOUNT_INFO_LINK_WELCOME_DROP_DOWN);
		return new DSVStoreFrontAccountInfoPage(driver);
	}


}
