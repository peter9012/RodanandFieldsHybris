package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;

public class CRMLoginPage extends RFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(CRMLoginPage.class.getName());
	private final By LOGIN_BOX_LOCATION = By.id("login_form");
	private final By USERNAME_TXTFLD_LOC = By.id("username");
	private final By PASSWORD_TXTFLD_LOC = By.id("password");
	private final By LOGIN_BTN_LOC = By.id("Login");


	public CRMLoginPage(RFWebsiteDriver driver) {
		super(driver);
	}
	public CRMHomePage loginUser(String username, String password){
		driver.waitForElementPresent(LOGIN_BOX_LOCATION);
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);		
		logger.info("login username is: "+username);
		logger.info("login password is: "+password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		driver.waitForLoadingImageBoxToDisappear();
		return new CRMHomePage(driver);
	}

}
