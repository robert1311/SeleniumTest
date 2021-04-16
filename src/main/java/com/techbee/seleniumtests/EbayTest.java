package com.techbee.seleniumtests;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class EbayTest {
	private static final String EBAYIPHONES = "ebayIPhones.txt";
	private static PrintWriter out;

	public static void main(String[] args) throws InterruptedException {
		try {
			out = new PrintWriter(new FileWriter(EBAYIPHONES));
		} catch (IOException e) {
			System.out.println("Could not write to file");
		}

		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\rober\\Documents\\WebDrivers\\chromedriver_win32-89\\chromedriver.exe");

		WebDriver ebayDriver = new ChromeDriver();
		ebayDriver.get("https://www.ebay.com/");

		findEbayIphoneDetailsAndSaveList(ebayDriver);

	}

	public static void findEbayIphoneDetailsAndSaveList(WebDriver ebayDriver) throws InterruptedException {
		ebayDriver.findElement(By.id("gh-ac")).sendKeys("iphone");
		ebayDriver.findElement(By.id("gh-btn")).click();
		List<WebElement> items = ebayDriver.findElements(By.className("s-item"));
		// System.out.println(items.size());
		findIphoneDescriptionAndPrice(items);
		
	}
	
	public static void findIphoneDescriptionAndPrice(List<WebElement> items) throws InterruptedException{
		for (WebElement item : items) {
			int index = items.indexOf(item) + 1;
			String itemPath = "//*[@id=\"srp-river-results\"]/ul/li[" 
					+ index + "]/div/div[2]";
			String line = "";
			String title = item
					.findElement(By.xpath(itemPath + "/a/h3"))
					.getText();
			//Thread.sleep(1000);
			String price = "";//item.findElement(By.className("s-item_price")).getText();
			System.out.println(price);
			line = title + " - " + price;
			writeToFile(line, out);
			System.out.println(title);
			if (index == items.size() - 2) {
				break;
			}
		}
	}

	public static void writeToFile(String text, PrintWriter out) {
		out.println(text);
		out.flush();

	}

}
