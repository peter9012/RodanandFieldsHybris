
package com.rf.pages.website.storeFront;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontConsultantPage extends StoreFrontRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontConsultantPage.class.getName());

	Actions actions;
	private final By WELCOME_USER_LOC = By.id("account-info-button");	
	private final By NEXT_CRP_IMG_LOC = By.xpath("//li[@id='mini-shopping-special-button']//div[contains(text(),'Next')]");

	public StoreFrontConsultantPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public boolean verifyConsultantPage() throws InterruptedException{		
		driver.waitForElementPresent(WELCOME_USER_LOC);
		return driver.isElementPresent(WELCOME_USER_LOC);		
	}

	public boolean isLinkPresentOnWelcomeDropDown(String link){
		return driver.isElementPresent(By.linkText(link));
	}

	public String getCurrentURL() throws InterruptedException{
		driver.waitForPageLoad();
		logger.info("Current url is "+driver.getCurrentUrl());
		return driver.getCurrentUrl();
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
		driver.click(By.xpath("//a[@id='cancelPulse']"));
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.id("cancel-pulse-button"));
			driver.click(By.id("cancel-pulse-button"));
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){

		}
		driver.waitForPageLoad();
	}

	public boolean validatePulsePrefixSuggestionsAvailable(){
		driver.pauseExecutionFor(1000);
		return driver.findElement(By.xpath("//p[@id='prefix-validation']")).isDisplayed();
	}

	public String getUserNameAForVerifyLogin(String profileName){
		/*		driver.waitForElementPresent(By.xpath("//span[contains(text(),'"+profileName+"')]"));
		String userName = driver.findElement(By.xpath("//span[contains(text(),'"+profileName+"')]")).getText();
		return userName;*/
		driver.waitForElementPresent(By.xpath("//span[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'"+profileName+"')]"));
		String userName = driver.findElement(By.xpath("//span[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'"+profileName+"')]")).getText().toLowerCase();
		return userName;
	}

	public String getHomtownNamePresentOnAfterClickonPersinalizeLink(){
		driver.waitForPageLoad();
		String homeTown = driver.findElement(By.xpath("//input[@id='homeTown']")).getAttribute("value");
		return homeTown;
	}

	public String  getConsultantSinceTextPresentAfterClickonPersinalizeLink(){
		return driver.findElement(By.xpath("//span[contains(text(),'Consultant since')]")).getText();
	}

	public String getFavoriteProductNameIsPresentAfterClickonPersinalizeLink(){
		return driver.findElement(By.xpath("//span[contains(text(),'Favorite products')]")).getText();

	}

	public boolean verifyBlockOfWhyIJoinedOnMeetYourConsultantPage(){
		driver.waitForPageLoad();
		if(driver.isElementPresent(By.xpath("//div[contains(@class,'consultant-biodata-right')]/p[1]"))){
			return true;
		}else
			return false;
	}

	public boolean verifyBlockOfProductsOnMeetYourConsultantPage(){
		driver.waitForPageLoad();
		if(driver.isElementPresent(By.xpath("//div[contains(@class,'consultant-biodata-right')]/p[1]"))){
			return true;
		}else
			return false;
	}

	public boolean verifyBlockOfBusinessOnMeetYourConsultantPage(){
		driver.waitForPageLoad();
		if(driver.isElementPresent(By.xpath("//div[contains(@class,'consultant-biodata-right')]/p[1]"))){
			return true;
		}else
			return false;
	}

	public void clickOnMeetYourConsultantLink(){
		driver.pauseExecutionFor(1500);
		try{
			driver.waitForElementPresent(By.xpath("//div[@id='header-middle-top']//a"));
			driver.click(By.xpath("//div[@id='header-middle-top']//a"));
		}catch(Exception e){
			logger.info("Meet Your Consultant link is not present");
			e.printStackTrace();
		}
	}

	public boolean validateMeetYourConsultantPage(){
		driver.pauseExecutionFor(5000);
		return driver.getCurrentUrl().toLowerCase().contains("meetyourconsultant");
	}

	public boolean validateCRPCartDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[@id='bag-special']/span"));
		return driver.isElementPresent(By.xpath("//div[@id='bag-special']/span"));
	}

	public boolean validateAdhocCartIsDisplayed(){
		driver.waitForElementPresent(By.xpath("//span[@class='cart-section']"));
		return driver.isElementPresent(By.xpath("//span[@class='cart-section']"));
	}

}
