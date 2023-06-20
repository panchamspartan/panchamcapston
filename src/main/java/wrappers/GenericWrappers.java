package wrappers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.asserts.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.Reporter;

public class GenericWrappers extends Reporter {

	public GenericWrappers(RemoteWebDriver driver, ExtentTest test) {
		this.driver = driver;
		this.test=test;
	}

	public RemoteWebDriver driver;
	protected static Properties prop;
	public String sUrl,primaryWindowHandle,sHubUrl,sHubPort;

	public GenericWrappers() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./src/main/resources/config.properties")));
			sHubUrl = prop.getProperty("HUB");
			sHubPort = prop.getProperty("PORT");
			sUrl = prop.getProperty("URL");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will launch the browser in local machine and maximise the browser and set the
	 * wait for 30 seconds and load the url
	 * @author Pancham
	 * @param url - The url with http or https
	 * @return 
	 * 
	 */
	public RemoteWebDriver invokeApp(String browser) {
		return invokeApp(browser,false);
	}

	/**
	 * This method will launch the browser in grid node (if remote) and maximise the browser and set the
	 * wait for 30 seconds and load the url 
	 * @author Pancham
	 * @param url - The url with http or https
	 * @return 
	 * 
	 */
	public RemoteWebDriver invokeApp(String browser, boolean bRemote) {
		try {

			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setBrowserName(browser);
			dc.setPlatform(Platform.MAC);
			// this is for grid run
			if (bRemote)
				driver = new RemoteWebDriver(new URL("http://" + sHubUrl + ":" + sHubPort + "/wd/hub"), dc);
			else { // this is for local run
				if (browser.equalsIgnoreCase("chrome")) {
					//WebDriverManager.chromedriver().setup();
					System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver");
					driver = new ChromeDriver();
				} 
				else if(browser.equalsIgnoreCase("edge")){
					//WebDriverManager.edgedriver().setup();
					System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")+"/drivers/msedgedriver");
					driver = new EdgeDriver();
				}
				else {
					System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/drivers/geckodriver");
					FirefoxDriver driver = new FirefoxDriver();
				}
			}

			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.get(sUrl);

			primaryWindowHandle = driver.getWindowHandle();
			reportStep("The browser:" + browser + " launched successfully","PASS");

		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The browser:" + browser + " could not be launched", "FAIL");
		}

		return driver;
	}

	/**
	 * This method will enter the value to the text field using id attribute to locate
	 * 
	 * @param idValue - id of the webelement
	 * @param data - The data to be sent to the webelement
	 * @author Pancham
	 * @throws IOException 
	 * @throws COSVisitorException 
	 */
	//
	public void enterValue(By locator, String data) {
		try {
			driver.findElement(locator).clear();
			driver.findElement(locator).sendKeys(data);	
			reportStep("The data: "+data+" entered successfully in field :"+locator, "PASS");
		} catch (NoSuchElementException e) {
			reportStep("The data: "+data+" could not be entered in the field :"+locator, "FAIL");
		}
		catch (WebDriverException e) {
			reportStep("Unknown exception occured while entering "+data+" in the field :"+locator, "FAIL");
		}
		catch (Exception e) {
			reportStep("Unknown exception occured while entering "+data+" in the field :"+locator, "FAIL");
		}
	}

	/**
	 * This method will verify the title of the browser 
	 * @param title - The expected title of the browser
	 * @author Pancham
	 */
	public boolean verifyTitle(String title){
		boolean bReturn = false;
		try{
			if (driver.getTitle().equalsIgnoreCase(title)){
				reportStep("The title of the page matches with the value :"+title, "PASS");
				bReturn = true;
			}else
				reportStep("The title of the page:"+driver.getTitle()+" did not match with the value :"+title, "SUCCESS");

		}catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title", "FAIL");
		}
		return bReturn;
	}

	/**
	 * This method will verify the given text matches in the element text
	 * @param xpath - The locator of the object in xpath
	 * @param text  - The text to be verified
	 * @author Pancham
	 */
	public void verifyText(By locator, String text){
		try {
			String sText = driver.findElement(locator).getText();
			Assert.assertTrue(sText.equalsIgnoreCase(text), "The text: "+sText+" matches with the value :"+text);
			/*
			 * if (sText.equalsIgnoreCase(text)){
			 * reportStep("The text: "+sText+" matches with the value :"+text, "PASS");
			 * }else{ reportStep("The text: "+sText+" did not match with the value :"+text,
			 * "FAIL"); }
			 */
		}catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title", "FAIL");
		}
	}

	/**
	 * This method will close all the browsers
	 * @author Pancham
	 */
	public void quitBrowser() {
		try {
			driver.quit();
		} catch (Exception e) {
			reportStep("The browser:"+driver.getCapabilities().getBrowserName()+" could not be closed.", "FAIL");
		}

	}

	/**
	 * This method will click the element using id as locator
	 * @param id  The id (locator) of the element to be clicked
	 * @author Pancham
	 */
	public void clickByElement(By locator) {
		try{
			driver.findElement(locator).click();
			reportStep("The element with locator: "+locator+" is clicked.", "PASS");

		}catch (NoSuchElementException e) {
			reportStep("The element with locator: "+locator+" could not be clicked.", "FAIL");
		}


		catch (Exception e) {
			reportStep("The element with locator: "+locator+" could not be clicked.", "FAIL");
		}
	}

	public void clickByXpathNoSnap(String xpathVal) {
		try{
			Thread.sleep(2000);
			driver.findElement(By.xpath(xpathVal)).click();
			reportStep("The element : "+xpathVal+" is clicked.", "PASS");
		} catch (Exception e) {
			reportStep("The element with xpath: "+xpathVal+" could not be clicked.", "FAIL");
		}
	}

	/**
	 * This method will mouse over on the element using xpath as locator
	 * @param xpathVal  The xpath (locator) of the element to be moused over
	 * @author Pancham
	 */
	public void mouseOverByXpath(String xpathVal) {
		try{
			new Actions(driver).moveToElement(driver.findElement(By.xpath(xpathVal))).build().perform();
			reportStep("The mouse over by xpath : "+xpathVal+" is performed.", "PASS");
		} catch (Exception e) {
			reportStep("The mouse over by xpath : "+xpathVal+" could not be performed.", "FAIL");
		}
	}

	/**
	 * This method will mouse over on the element using link name as locator
	 * @param xpathVal  The link name (locator) of the element to be moused over
	 * @author Pancham
	 */
	public void mouseOverByLinkText(String linkName) {
		try{
			new Actions(driver).moveToElement(driver.findElement(By.linkText(linkName))).build().perform();
			reportStep("The mouse over by link : "+linkName+" is performed.", "PASS");
		} catch (Exception e) {
			reportStep("The mouse over by link : "+linkName+" could not be performed.", "FAIL");
		}
	}

	/**
	 * This method will return the text of the element using xpath as locator
	 * @param xpathVal  The xpath (locator) of the element
	 * @author Pancham
	 */
	public String getTextByXpath(String xpathVal){
		String bReturn = "";
		try{
			return driver.findElement(By.xpath(xpathVal)).getText();
		} catch (Exception e) {
			reportStep("The element with xpath: "+xpathVal+" could not be found.", "FAIL");
		}
		return bReturn; 
	}

	/**
	 * This method will return the text of the element using id as locator
	 * @param locator  The (locator) of the element
	 * @author Pancham
	 */
	public String getTextByElement(By locator) {
		String bReturn = "";
		try{
			return driver.findElement(locator).getText();
		} catch (Exception e) {
			reportStep("The element with id: "+locator+" could not be found.", "FAIL");
		}
		return bReturn; 
	}


	/**
	 * This method will select the drop down value using id as locator
	 * @param locator The locator of the drop down element
	 * @param value The value to be selected (visibletext) from the dropdown 
	 * @author Pancham
	 */
	public void selectVisibileText(By locator, String value) {
		try{
			new Select(driver.findElement(locator)).selectByVisibleText(value);;
			reportStep("The element with locator: "+locator+" is selected with value :"+value, "PASS");
		} catch (Exception e) {
			reportStep("The value: "+value+" could not be selected.", "FAIL");
		}
	}

	public void selectIndex(By locator, String value) {
		try{
			new Select(driver.findElement(locator)).selectByIndex(Integer.parseInt(value));;
			reportStep("The element with locator: "+locator+" is selected with index :"+value, "PASS");
		} catch (Exception e) {
			reportStep("The index: "+value+" could not be selected.", "FAIL");
		}
	}

	public void switchToParentWindow() {
		try {
			Set<String> winHandles = driver.getWindowHandles();
			for (String wHandle : winHandles) {
				driver.switchTo().window(wHandle);
				break;
			}
		} catch (Exception e) {
			reportStep("The window could not be switched to the first window.", "FAIL");
		}
	}

	public void switchToLastWindow() {
		try {
			Set<String> winHandles = driver.getWindowHandles();
			for (String wHandle : winHandles) {
				driver.switchTo().window(wHandle);
			}
		} catch (Exception e) {
			reportStep("The window could not be switched to the last window.", "FAIL");
		}
	}

	public void acceptAlert() {
		try {
			driver.switchTo().alert().accept();
		} catch (NoAlertPresentException e) {
			reportStep("The alert could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The alert could not be accepted.", "FAIL");
		}

	}


	public String getAlertText() {		
		String text = null;
		try {
			driver.switchTo().alert().getText();
		} catch (NoAlertPresentException e) {
			reportStep("The alert could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The alert could not be accepted.", "FAIL");
		}
		return text;

	}

	public void dismissAlert() {
		try {
			driver.switchTo().alert().dismiss();
		} catch (NoAlertPresentException e) {
			reportStep("The alert could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The alert could not be accepted.", "FAIL");
		}

	}

	public long takeSnap(){
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L; 
		try {
			FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE) , new File("./reports/images/"+number+".jpg"));
		} catch (WebDriverException e) {
			reportStep("The browser has been closed.", "FAIL");
		} catch (IOException e) {
			reportStep("The snapshot could not be taken", "WARN");
		}
		return number;
	}

	public void verifyElementPresent(By locator) {

		if (driver.findElement(locator).isDisplayed()) {
			reportStep("Element is visible", "PASS");
		} else {
			reportStep("Element is not visible", "FAIL");
		}
	}

	public  void scrollIntoView(By Element)
	{
		try {
			Thread.sleep(2000);
			JavascriptExecutor je = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(Element);
			je.executeScript("arguments[0].scrollIntoView(true);",element);
			Thread.sleep(2000);
			reportStep("Element Viewed Successfully", "PASS");
		} catch (NoSuchElementException e) {
			reportStep(Element+"-Element was not found in DOM "+Element+ e.getStackTrace(), "FAIL");
		} catch (Exception e) {
			reportStep(Element+"-Unable to click on element "+Element+ e.getStackTrace(), "FAIL");
		}
	}

	public void switchToFrame(By Element) {
		try{
			driver.switchTo().frame(driver.findElement(Element));
			reportStep("Switched to frame - "+Element+" correctly", "PASS");
		} catch (NoSuchElementException e) {
			reportStep(Element+"-Element was not found in DOM "+Element+ e.getStackTrace(), "FAIL");
		} catch (Exception e) {
			reportStep(Element+"-Unable to click on element "+Element+ e.getStackTrace(), "FAIL");
		}
	}

	public void switchToDefault() {
		try{
			driver.switchTo().defaultContent();
			reportStep("Switched out from the frame", "PASS");
		} 
		catch (Exception e) {
			reportStep("Error occured while switching out from the frame"+ e.getStackTrace(), "FAIL");
		}
	}

	public void refreshPage() {
		driver.navigate().refresh();
	}

	public void backPage() {
		driver.navigate().back();
	}
	
	public void clickIfExists(By locator) {
		try {
			if(driver.findElements(locator).size()>0) {
				driver.findElement(locator).click();
				reportStep("The element with locator: "+locator+" is clicked.", "PASS");
			}
		}catch (NoSuchElementException e) {
			reportStep("The element with locator: "+locator+" could not be clicked.", "FAIL");
		}
		catch (Exception e) {
			reportStep("The element with locator: "+locator+" could not be clicked.", "FAIL");
		}

	}
	
	public int getSizeOfElement(By locator) {
		int size = driver.findElements(locator).size();
		return size;
	}
	
	public void verifyElementDoesNotExist(By Element){
		try {
			Thread.sleep(3000);
			if (driver.findElements(Element).size()== 0){
				reportStep("Element does not exists - "+Element+" -as expected", "PASS");
			}
			else
			{
				reportStep("Element exists - "+Element+" -as unexpected", "FAIL");
			}
		} catch (NoSuchElementException e) {
			reportStep(Element+"-Element was not found in DOM "+Element+ e.getStackTrace(), "FAIL");
		} catch (Exception e) {
			reportStep(Element+"-Unable to click on element "+Element+ e.getStackTrace(), "FAIL");
		}
	}
	

}