package com.aflac.it.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SeleniumTestCases {
	private WebDriver driver;
		
	@Before
	public void setupMethod() {
		WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://publish-p60469-e738775.adobeaemcloud.com/content/dam/formsanddocuments/aflacapps/casebuildertool-form/jcr:content?wcmmode=disabled");
	}
	
	@After
	public void closeSetup() {
		driver.close();
	}

	@Test
	public void TestEnrollmentRecord() {
		
		assertEquals("casebuildertool-Form", driver.getTitle());
		
		WebElement groupNumber = driver.findElement(By.id("guideContainer-rootPanel-panel_628055537_copy-panel-guidetextbox___widget"));
		groupNumber.sendKeys("AGC0000317811");
		
		WebElement date = driver.findElement(By.id("guideContainer-rootPanel-panel_628055537_copy-panel-guidedatepicker___widget"));
		date.sendKeys("10/19/2022");
		
		WebElement fetch = driver.findElement(By.id("guideContainer-rootPanel-panel_628055537_copy-panel-guidebutton___widget"));
		fetch.click();
		
		WebElement enrollmentRecord = driver.findElement(By.id("guideContainer-rootPanel-panel_628055537_copy-panel-guidedropdownlist___widget"));
		String record = enrollmentRecord.getAttribute("value");
		
		assertNotNull(enrollmentRecord);
		assertEquals("Kram Endeavors Inc- 7/1/2022", record);
		
		WebElement platformDropdown = driver.findElement(By.id("guideContainer-rootPanel-panel_628055537_copy-panel-guidedropdownlist_1283356152___widget"));
		assertNotNull(platformDropdown);
		Select dropDown = new Select(platformDropdown);
		List<WebElement> options = dropDown.getOptions();
		
		assertNotNull(options);
		assertEquals("Ease", options.get(1).getAttribute("value"));
		dropDown.selectByValue("Ease");
		
		WebElement next = driver.findElement(By.id("guideContainer-rootPanel-panel_628055537_copy-toolbar-nextitemnav___widget"));
		next.click();
		
		WebElement situs = driver.findElement(By.id("guideContainer-rootPanel-panel_628055537_copy-panel_1625559233-panel_1008106012-panel-guidetextbox_copy_56___widget"));
		assertNotNull(situs);
		assertEquals("CA-California", situs.getAttribute("value"));
	}
	
}
