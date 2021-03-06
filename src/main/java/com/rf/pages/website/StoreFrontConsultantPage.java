package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import com.rf.core.driver.website.RFWebsiteDriver;


public class StoreFrontConsultantPage extends RFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontConsultantPage.class.getName());

	Actions actions;
	private final By WELCOME_USER_LOC = By.xpath("//a[contains(text(),'Welcome')]");
	private final By WELCOME_USER_DD_LOC = By.cssSelector("li[id='account-info-button']"); 
	private final By WELCOME_DD_SHIPPING_INFO_LINK_LOC = By.linkText("Shipping Info");
	private final By WELCOME_DD_ORDERS_LINK_LOC = By.xpath("//div[@id='account-info']//a[text()='Orders']");
	private final By WELCOME_DD_BILLING_INFO_LINK_LOC = By.linkText("Billing Info");
	private final By WELCOME_DD_ACCOUNT_INFO_LOC = By.xpath("//a[text()='Account Info']");
	private final By NEXT_CRP_IMG_LOC = By.xpath("//li[@id='mini-shopping-special-button']//div[contains(text(),'Next')]");

	public StoreFrontConsultantPage(RFWebsiteDriver driver) {
		super(driver);		
	}


	public boolean verifyConsultantPage() throws InterruptedException{		
		driver.waitForElementPresent(WELCOME_USER_LOC);
		return driver.isElementPresent(WELCOME_USER_LOC);		
	}
	
	public void clickOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_USER_DD_LOC);
		driver.pauseExecutionFor(2000);
		driver.click(WELCOME_USER_DD_LOC);
		logger.info("clicked on welcome drop down");		
	}

	public boolean isLinkPresentOnWelcomeDropDown(String link){
		return driver.isElementPresent(By.linkText(link));
	}

	public StoreFrontShippingInfoPage clickShippingLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_DD_SHIPPING_INFO_LINK_LOC);
		driver.click(WELCOME_DD_SHIPPING_INFO_LINK_LOC);		
		logger.info("User has clicked on shipping link from welcome drop down");
		return new StoreFrontShippingInfoPage(driver);
	}

	public StoreFrontOrdersPage clickOrdersLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_DD_ORDERS_LINK_LOC);
		driver.click(WELCOME_DD_ORDERS_LINK_LOC);
		logger.info("User has clicked on orders link from welcome drop down");
		return new StoreFrontOrdersPage(driver);
	}

	public StoreFrontBillingInfoPage clickBillingInfoLinkPresentOnWelcomeDropDown(){
		driver.waitForElementPresent(WELCOME_DD_BILLING_INFO_LINK_LOC);
		driver.click(WELCOME_DD_BILLING_INFO_LINK_LOC);
		logger.info("User has clicked on billing link from welcome drop down");
		driver.waitForPageLoad();
		return new StoreFrontBillingInfoPage(driver);
	}

	public String getCurrentURL() throws InterruptedException{
		driver.waitForPageLoad();
		logger.info("Current url is "+driver.getCurrentUrl());
		return driver.getCurrentUrl();
	}

	public StoreFrontAccountInfoPage clickAccountInfoLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_DD_ACCOUNT_INFO_LOC);
		driver.click(WELCOME_DD_ACCOUNT_INFO_LOC);		
		logger.info("User has clicked on account link from welcome drop down");
		driver.pauseExecutionFor(3000);
		return new StoreFrontAccountInfoPage(driver);
	}

	public StoreFrontCartAutoShipPage clickNextCRP(){
		driver.waitForElementPresent(NEXT_CRP_IMG_LOC);
		driver.click(NEXT_CRP_IMG_LOC);
		logger.info("Next CRP link clicked");
		driver.waitForPageLoad();
		return new StoreFrontCartAutoShipPage(driver);
	}

	public StoreFrontCartAutoShipPage addProductToCRP(){
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[2]/div[@class='quick-product-wrapper'][1]/div[3]//input[@value='Add to crp']"));
		driver.click(By.xpath("//div[@id='main-content']/div[2]/div[@class='quick-product-wrapper'][1]/div[3]//input[@value='Add to crp']"));
		logger.info("Add product to CRP button clicked");
		return new StoreFrontCartAutoShipPage(driver);
	} 

	public void clickOnAutoshipStatusLink(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Autoship Status')]"));
		driver.click(By.xpath("//a[contains(text(),'Autoship Status')]"));
		logger.info("Autoship status link clicked");
		driver.waitForPageLoad();
	}

	public void subscribeToPulse(){
		if(driver.isElementPresent(By.xpath("//a[text()='Cancel my Pulse subscription �']"))){
			driver.click(By.xpath("//a[text()='Cancel my Pulse subscription �']"));
			driver.click(By.xpath("//input[@id='cancel-pulse-button']"));
			driver.waitForLoadingImageToDisappear();
			driver.waitForPageLoad();
		}
		driver.click(By.xpath("//input[@id='subscribe_pulse_button_new']"));
		driver.waitForLoadingImageToDisappear();
	}

	public boolean validateErrorMessageWithSpclCharsOnPulseSubscription(){
		driver.findElement(By.xpath("//input[@id='webSitePrefix']")).sendKeys("!@");
		driver.click(By.id("pulse-enroll"));
		return (driver.findElement(By.xpath("//img[@id='prefixIsAvailableImage']")).isDisplayed()||driver.findElement(By.xpath("//span[@class='prefix unavailable']")).isDisplayed());
	}

	public boolean validateNextCRPMiniCart() {
		actions=new Actions(RFWebsiteDriver.driver);
		//actions.moveToElement(driver.findElement(By.xpath("//li[@id='mini-shopping-special-button']"))).build().perform();
		//driver.waitForElementPresent(By.xpath("//h2[contains(text(),'YOUR CRP CART')]"));
		return driver.findElement(By.xpath("//li[@id='mini-shopping-special-button']")).isDisplayed();
	}

	public void cancelPulseSubscription(){
		driver.waitForElementPresent(By.xpath("//a[text()='Cancel my Pulse subscription �']"));
		driver.click(By.xpath("//a[text()='Cancel my Pulse subscription �']"));
		driver.pauseExecutionFor(2000);
		driver.click(By.xpath("//input[@id='cancel-pulse-button']"));
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public boolean validatePulsePrefixSuggestionsAvailable(){
		driver.pauseExecutionFor(1000);
		return driver.findElement(By.xpath("//p[@id='prefix-validation']")).isDisplayed();
	}

}

