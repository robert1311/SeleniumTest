package com.techbee.seleniumtests;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EbayTest {
	private static final String EBAYIPHONES = "ebayIPhones.txt";
	private static PrintWriter out;

	public static void main(String[] args) throws InterruptedException {
		
		/** Setting up Driver for specific web-site.
		 */
		try {
			out = new PrintWriter(new FileWriter(EBAYIPHONES));
		} catch (IOException e) {
			System.out.println("Could not write to file");
		}

		//setting Chrome driver property and its file path
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\rober\\Documents\\WebDrivers\\chromedriver_win32-89"
				+ "\\chromedriver.exe");

		//Instantiating Chrome driver object for eBay
		WebDriver ebayDriver = new ChromeDriver();
		ebayDriver.get("https://www.ebay.com/");

		findEbayIphoneDetailsAndSaveList(ebayDriver);

	}

	/** Runs a search by entering input and submitting to view result and invoke 
	 * action on them. */
	public static void findEbayIphoneDetailsAndSaveList(WebDriver ebayDriver) 
			throws InterruptedException {
		WebDriverWait wait=new WebDriverWait(ebayDriver, 20);

		//entering input
		ebayDriver.findElement(By.id("gh-ac")).sendKeys("iphone");
		Thread.sleep(1000);
		
		//submitting input
		ebayDriver.findElement(By.id("gh-btn")).click();
		Thread.sleep(5000);
		
		//Collecting specified Web Elements by xpath
		List<WebElement> items = wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(
				By.xpath("//*[@id=\"srp-river-results\"]/ul/li")));
		Thread.sleep(1000);
		
		//Scrolls down to bottom of page so page will be fully loaded
		JavascriptExecutor jse = (JavascriptExecutor) ebayDriver;
		jse.executeScript("window.scrollTo(0,document.body.scrollHeight);");
		
		findIphoneDescriptionAndPrice(items, ebayDriver);
		
	}
	
	/** Finds title description and price of given list of Web Elements*/
	public static void findIphoneDescriptionAndPrice(List<WebElement> items, 
			WebDriver ebayDriver) throws InterruptedException{
		
		/* Declaring WebdriverWait class to wait if the driver is taking some 
			time to find specified elements based on specified locator */
		WebDriverWait wait=new WebDriverWait(ebayDriver, 20);
		
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
					item.findElement(
							By.xpath(
									"//*[@id=\"srp-river-results\"]/ul/li[" 
							+ index + "]/div/div[2]/a/h3")))).getText();
			Thread.sleep(1000);
			
			// getting price
			String price = item.findElement
					(By.cssSelector(
							"[class=s-item__price]")).getText();
			line = title + " - " + price;
			System.out.println(line);
			
			writeToFile(line, out);
			if (index == items.size()) {// breaks at end of list
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
