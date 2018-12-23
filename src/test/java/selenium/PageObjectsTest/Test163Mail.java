package selenium.PageObjectsTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import selenium.PageObjects.HomePage;
import selenium.PageObjects.LoginPage;

public class Test163Mail {
	public WebDriver driver;
	
	@BeforeMethod
	public void beforeMethod() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");	
		driver = new ChromeDriver();
	}
	
	@AfterMethod
	public void afterMethod() {
		driver.quit();
	}
	
	@Test
	public void testLoginFail() throws InterruptedException{
		LoginPage loginPage = new LoginPage(driver);
		loginPage.get();
		//调用LoginPage中登录失败的方法
		loginPage.LoginExpectingFailure();
		Thread.sleep(3000);
		//断言登录后是否包含"无法登录邮箱"
		Assert.assertTrue(loginPage.getPageSource().contains("无法登录邮箱"));
		loginPage.close();
		
	}
	
	@Test
	public void testLoginSuccess() throws InterruptedException{
		//生成一个LoginPage对象实例
		LoginPage loginPage = new LoginPage(driver);
		//继承LoadableComponent类后，只实现了load方法
		//没有定义get方法，也可以调用get方法
		//get方法默认调用页面对象类中的load方法
		loginPage.get();
		//调用LoginPage中的login方法，登录成功后会跳转到邮箱主页
		//login返回一个HomePage对象，实现页面跳转到登录后的主页
		//以便实现在HomePage对象中进行相关的方法调用
		HomePage homePage = loginPage.login();
		Thread.sleep(5000);
		//断言登录后是否包含"未读邮件"
		Assert.assertTrue(loginPage.getPageSource().contains("未读邮件"));
		homePage.close();
	}
	
	@Test
	public void testWriteMail() throws InterruptedException{
		LoginPage loginPage = new LoginPage(driver);
		loginPage.get();
		HomePage homePage = loginPage.login();
		Thread.sleep(5000);
		homePage.writeMail();
		Thread.sleep(5000);
		Assert.assertTrue(loginPage.getPageSource().contains("发送成功"));
		homePage.close();
	}
	
}
