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
		try {
			out = new PrintWriter(new FileWriter(TARGETIPHONES));
		} catch (IOException e) {
			System.out.println("Could not write to file");
		}

		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\rober\\Documents\\WebDrivers\\chromedriver_win32-89\\chromedriver.exe");

		WebDriver targetDriver = new ChromeDriver();
		targetDriver.get("https://www.target.com/");
		targetDriver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS) ;

		findTargetIphoneDetailsAndSaveList(targetDriver);

	}
	
	public static void findTargetIphoneDetailsAndSaveList(WebDriver targetDriver) throws InterruptedException {
		WebDriverWait wait=new WebDriverWait(targetDriver, 20);
		targetDriver.findElement(By.id("search")).sendKeys("iphone");
		Thread.sleep(1000);
		targetDriver.findElement(By.id("search")).submit();
		Thread.sleep(5000);
		List<WebElement> items = wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(
				By.cssSelector(
						"[data-test = list-entry-product-card]")));
		//targetDriver.findElements(By.cssSelector("[data-test = list-entry-product-card]"));
		Thread.sleep(1000);
		JavascriptExecutor jse = (JavascriptExecutor) targetDriver;
		jse.executeScript("window.scrollTo(0,document.body.scrollHeight);");

		//System.out.println(items.size());
		findIphoneDescriptionAndPrice(items, targetDriver);
		
	}
	
	public static void findIphoneDescriptionAndPrice(List<WebElement> items, WebDriver targetDriver) throws InterruptedException{
		WebDriverWait wait=new WebDriverWait(targetDriver, 20);
		//System.out.println("loop method");
		System.out.println(items.size());
		out.println(LocalDateTime.now().toString() + "\n");
		out.flush();
		for (WebElement item : items) {
			//System.out.println(item.toString());
			int index = items.indexOf(item) + 1;
			String itemPath = "";
			String line = "";
			String title = wait.until(ExpectedConditions.visibilityOf(
					item.findElement(By.cssSelector(
							"[data-test=product-title]")))).getText();
			//		By.cssSelector("[data-test = product-title]")).getText();
			Thread.sleep(1000);
			String price = item.findElement(By.cssSelector("[data-test=product-card-price]")).getText();//item.findElement(By.className("s-item_price")).getText();
			System.out.println(price);
			line = title + " - " + price;
			writeToFile(line, out);
			System.out.println(title);
			//System.out.println(item.toString());
			if (index == items.size()) {
				break;
			}
		}
	}

	public static void writeToFile(String text, PrintWriter out) {
		out.println(text);
		out.flush();

	}

}
