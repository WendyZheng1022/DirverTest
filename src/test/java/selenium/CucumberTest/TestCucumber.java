package selenium.CucumberTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestCucumber {
	
	public static WebDriver driver;
	public static String url = "https://mail.163.com";
	
	@Test
	public void testLoginAndLogout() throws InterruptedException{
		driver.get(url);
		
		Thread.sleep(5000);
				
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[contains(@id,'x-URS-iframe')]")));
		
		driver.findElement(By.xpath("//*[@name='email']")).sendKeys("yulu_1022");
		driver.findElement(By.xpath("//*[@name='password']")).sendKeys("*****");
		driver.findElement(By.xpath("//*[@id='dologin']")).click();
		
		driver.switchTo().defaultContent();
		
		Thread.sleep(5000);
		Assert.assertTrue(driver.getPageSource().contains("未读邮件"));
		System.out.println("登录成功");
		
		Thread.sleep(5000);
				
	}
	
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");	
		driver = new ChromeDriver();
	}
	
	@After
	public void tearDown() {
		driver.quit();
	}
	

}
