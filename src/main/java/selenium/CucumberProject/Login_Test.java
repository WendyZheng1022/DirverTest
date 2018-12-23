package selenium.CucumberProject;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Login_Test {
	
	public static WebDriver driver;
	public static String url = "https://mail.163.com";
	
	
	@Given("^User is on EmailTypeList Page$")
	public void user_is_on_EmailTypeList_Page() throws Throwable{
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");	
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(url);
		driver.manage().window().maximize();
		
	}
	
	@When("^User Navigate to (\\d+) Mail Login Page$")
	public void user_Naviagte_to_Mail_LogIn_Page(int arg1) throws Throwable{
		Thread.sleep(5000);
		driver.get(url);
	}
	
	@Then("^user enters UserName and Password$")
	public void user_enters_UserName_and_Password() throws Throwable{
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[contains(@id,'x-URS-iframe')]")));
		
		driver.findElement(By.xpath("//*[@name='email']")).sendKeys("yulu_1022");
		driver.findElement(By.xpath("//*[@name='password']")).sendKeys("*****");
		driver.findElement(By.xpath("//*[@id='dologin']")).click();
		
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
	}
	
	@Then("^Message displayed Login Successfully")
	public void message_displayed_Login_Successfully() throws Throwable{
		Assert.assertTrue(driver.getPageSource().contains("未读邮件"));
		System.out.println("登录成功");
		driver.quit();
	}
	
}
