package com.techbee.seleniumtests;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TargetTest {

	private static final String TARGETIPHONES = "targetIPhones.txt";
	private static PrintWriter out; 
	
	public static void main(String[] args) throws InterruptedException {
		/** Setting up Driver for specific web-site.
		 */
		try {
			out = new PrintWriter(new FileWriter(TARGETIPHONES));
		} catch (IOException e) {
			System.out.println("Could not write to file");
		}

		//setting Chrome driver property and its file path
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\rober\\Documents\\WebDrivers\\chromedriver_win32-89\\chromedriver.exe");
		
		//Instantiating Chrome driver object for eBay
		WebDriver targetDriver = new ChromeDriver();
		targetDriver.get("https://www.target.com/");
		targetDriver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS) ;

		findTargetIphoneDetailsAndSaveList(targetDriver);

	}
	
	/** Runs a search by entering input and submitting to view result and invoke 
	 * action on them. */
	public static void findTargetIphoneDetailsAndSaveList(WebDriver targetDriver) throws InterruptedException {
		WebDriverWait wait=new WebDriverWait(targetDriver, 20);
		
		//entering input
		targetDriver.findElement(By.id("search")).sendKeys("iphone");
		Thread.sleep(1000);
		
		//submitting input
		targetDriver.findElement(By.id("search")).submit();
		Thread.sleep(5000);
		
		//Collecting specified Web Elements by CSS Selector
		List<WebElement> items = wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(
				By.cssSelector(
						"[data-test = list-entry-product-card]")));
		Thread.sleep(1000);
		
		//Scrolls down to bottom of page so page will be fully loaded
		JavascriptExecutor jse = (JavascriptExecutor) targetDriver;
		jse.executeScript("window.scrollTo(0,document.body.scrollHeight);");

		findIphoneDescriptionAndPrice(items, targetDriver);
		
	}
	
	/** Finds title description and price of given list of Web Elements*/
	public static void findIphoneDescriptionAndPrice(List<WebElement> items, 
			WebDriver targetDriver) throws InterruptedException{
		
		/* Declaring WebdriverWait class to wait if the driver is taking some 
		time to find specified elements based on specified locator */
		WebDriverWait wait=new WebDriverWait(targetDriver, 20);
		
		//print time-stamp of execution to file
		out.println(LocalDateTime.now().toString() + "\n");
		out.flush();
		
		/* Looping through given list of web elements and grabbing title 
		 * description and price */
		for (WebElement item : items) {
			String line = "";
			int index = items.indexOf(item) + 1;
			
			//getting title description
			String title = wait.until(ExpectedConditions.visibilityOf(
					item.findElement(By.cssSelector(
							"[data-test=product-title]")))).getText();
			Thread.sleep(1000);
			
			// getting price
			String price = item.findElement(By.cssSelector("[data-test=product-card-price]")).getText();
			
			line = title + " - " + price;
			
			writeToFile(line, out);
			System.out.println(line);
			if (index == items.size()) {
				break;
			}
		}
	}

	/** Writes to file specified in class */
	public static void writeToFile(String text, PrintWriter out) {
		out.println(text);
		out.flush();

	}

}
