package com.rf.pages.website.nscore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;

public class NSCore4OrdersTabPage extends NSCore4RFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(NSCore4OrdersTabPage.class.getName());

	public NSCore4OrdersTabPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static String productsInfoTableLoc = "//td[contains(text(),'%s')]/following-sibling::td";
	private static String FirstNameLoc = "//table[@id='orders']//tr[@class='GridColHead']/th[2]//following::tbody/tr[%s]//td[2]";
	private static String LastNameLoc = "//table[@id='orders']//tr[@class='GridColHead']/th[2]//following::tbody/tr[%s]//td[3]";

	private final static By CANCEL_ORDER_LINK_LOC = By.xpath("//a[@id='cancelOrder']");
	private final static By ORDER_STATUS_LOC = By.xpath("//div[@class='Content']//td[contains(text(),' Status')]/following::b[1]");
	private final static By ORDER_ID_INPUT_FIELD_LOC = By.xpath("//input[@id='txtSearch']");
	private static final By GO_SEARCH_BTN = By.xpath("//a[@id='btnGo']/img[@alt='Go']");
	private static final By DROP_DOWN_LOC = By.xpath("//select[@id='cboSearchCol']");

	private static final By ADVANCED_SEARCH_INPUT_FIELD_LOC = By.xpath("//input[@id='txtAdvancedSearch']");
	private static final By ADVANCED_SEARCH_GO_BTN = By.xpath("//a[@id='btnAdvancedGo']");
	private static final By TOTAL_ROWS_SEARCH_RESULT_LOC = By.xpath("//table[@id='orders']/tbody/tr");
	private static final By START_A_NEW_ORDER_LINK_LOC = By.xpath("//span[text()='Start a New Order']");
	private static final By TXT_FIELD_START_ORDER_LOC = By.id("txtCustomerSuggest");
	private static final By SUGGESTION_START_ORDER_LOC = By.xpath("//div[@class='resultItem odd hover']");
	private static final By START_ORDER_BTN_LOC = By.xpath("//a[@id='btnStartOrder']");

	public void clickCancelOrderBtn(){
		driver.waitForElementPresent(CANCEL_ORDER_LINK_LOC);
		driver.click(CANCEL_ORDER_LINK_LOC);
		logger.info("cancel order link is clicked");
		driver.pauseExecutionFor(3000);
		driver.waitForPageLoad();
	}

	public boolean validateOrderStatus() {
		String status = driver.findElement(ORDER_STATUS_LOC).getText();
		if(status.equalsIgnoreCase("Cancelled")){
			return true;
		}return false;
	}

	public void enterOrderIDInInputField(String orderId) {
		driver.quickWaitForElementPresent(ORDER_ID_INPUT_FIELD_LOC);
		driver.clear(ORDER_ID_INPUT_FIELD_LOC);
		driver.type(ORDER_ID_INPUT_FIELD_LOC, orderId);
		logger.info("orderId field enterd as: "+orderId);
		driver.click(GO_SEARCH_BTN);
		logger.info("Search Go button clicked");
		driver.waitForPageLoad();
	}

	public boolean isOrderDetailPagePresent() {
		String url = driver.getCurrentUrl();
		return url.contains("Orders/Details");

	}

	public boolean isOrderInformationPresent(String InfoSection) {
		return driver.isElementPresent(By.xpath(String.format(productsInfoTableLoc,InfoSection)));
	}

	public void selectDropDownAdvancedSearch(String text) {
		Select dropDown = new Select(driver.findElement(DROP_DOWN_LOC));
		dropDown.selectByVisibleText(text);
		logger.info("drop down selected value is: "+text);
	}

	public void enterValueInAdvancedSearchInputField(String value) {
		driver.quickWaitForElementPresent(ADVANCED_SEARCH_INPUT_FIELD_LOC);
		driver.clear(ADVANCED_SEARCH_INPUT_FIELD_LOC);
		driver.type(ADVANCED_SEARCH_INPUT_FIELD_LOC, value);
		logger.info("advanced search field enterd as: "+value);
		driver.click(ADVANCED_SEARCH_GO_BTN);
		logger.info("Advanced Search Go button clicked");
		driver.waitForPageLoad();
	}

	public boolean isSearchResultFirstName(String firstName){
		boolean status = false;
		driver.waitForElementPresent(TOTAL_ROWS_SEARCH_RESULT_LOC);
		int totalSearchResult = driver.findElements(TOTAL_ROWS_SEARCH_RESULT_LOC).size();
		for(int i=1;i<=totalSearchResult;i++){
			String firstNameUI = driver.findElement(By.xpath(String.format(FirstNameLoc,i))).getText();
			if(firstNameUI.equalsIgnoreCase(firstName)){
				status = true;
				continue;
			}
			else{
				status = false;
				break;
			}

		}
		return status;
	}

	public boolean isSearchResultLastName(String lastName){
		boolean status = false;
		driver.waitForElementPresent(TOTAL_ROWS_SEARCH_RESULT_LOC);
		int totalSearchResult = driver.findElements(TOTAL_ROWS_SEARCH_RESULT_LOC).size();
		for(int i=1;i<=totalSearchResult;i++){
			String lastNameUI = driver.findElement(By.xpath(String.format(LastNameLoc,i))).getText();
			if(lastNameUI.equalsIgnoreCase(lastName)){
				status = true;
				continue;
			}
			else{
				status = false;
				break;
			}
		}
		return status;
	}

	public void clickStartANewOrderLink() {
		driver.quickWaitForElementPresent(START_A_NEW_ORDER_LINK_LOC);
		driver.click(START_A_NEW_ORDER_LINK_LOC);
		logger.info("start a new order link in order tab clicked");

	}

	public void enterAccountNameAndClickStartOrder(String accountName) {
		driver.quickWaitForElementPresent(TXT_FIELD_START_ORDER_LOC);
		driver.type(TXT_FIELD_START_ORDER_LOC, accountName);
		logger.info("start order text field entered by: "+accountName);
		driver.waitForElementPresent(SUGGESTION_START_ORDER_LOC);
		driver.click(START_ORDER_BTN_LOC);
		logger.info("start button is clicked after entered account name");
	}

}