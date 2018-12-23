package selenium.PageObjects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

//使用public方法提供对外服务
public class LoginPage extends LoadableComponent<LoginPage>{
	public WebDriver driver;
	private String url = "https://mail.163.com";
	private String title = "网易免费邮";
	
	
	@FindBy(xpath="//iframe[contains(@id,'x-URS-iframe')]")
	public WebElement iframe1;
	@FindBy(xpath="//*[@name='email']")
	public WebElement userName;
	@FindBy(xpath="//*[@name='password']")
	public WebElement password;
	@FindBy(xpath="//*[@id='dologin']")
	public WebElement loginButton;
	
	//构造函数，生成浏览器对象，初始化PageFactory对象
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	//增加了需要覆盖的方法load
	//不要暴露PageObject中的内部逻辑
	@Override
	protected void load() {
		this.driver.get(url);
		this.driver.manage().window().maximize();
	}
	
	//相同动作会产生多个不同的结果，需要定义多个操作方法
	//例如login和LoginExpectingFailure属于两种不同的结果
	
	//登录操作的封装方法
	public HomePage login() {
		//调用load方法
		load();
				
		//页面判断是否显示了邮件的输入框
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//iframe[contains(@id,'x-URS-iframe')]")));
		
		//163邮箱有iframe
		driver.switchTo().frame(iframe1);
				
		//保证输入时为空
		userName.clear();
		//输入用户名
		userName.sendKeys("yulu_1022");
		//输入密码
		password.sendKeys("******");
		//登录按钮
		loginButton.click();
		
		//跳出iframe
		driver.switchTo().defaultContent();
		
		//登录成功后返回一个HomePage对象
		return new HomePage(driver);
	}
	
	
	//登录失败的封装方法，函数返回一个LoginPage页面对象
	public LoginPage LoginExpectingFailure() throws InterruptedException{
		load();
						
		//页面判断是否显示了name为 email的输入框
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//iframe[contains(@id,'x-URS-iframe')]")));
		
		//163邮箱有iframe
		driver.switchTo().frame(iframe1);
		//保证输入时为空
		userName.clear();
		//输入用户名
		userName.sendKeys("yulu_1022");
		//输入密码
		password.sendKeys("123456");
		//登录按钮
		loginButton.click();
		
		//跳出iframe
		driver.switchTo().defaultContent();
		
		//登录失败后，当前测试页面网址不会发生跳转，函数返回一个LoginPage对象
		return new LoginPage(driver);

	}
	
	//获取页面源码的封装方法
	public String getPageSource() {
		return driver.getPageSource();
	}
		
	//增加了需要覆盖的方法isLoaded
	//不要暴露PageObject中的断言操作
	@Override
	protected void isLoaded() throws Error{
		Assert.assertTrue(driver.getTitle().contains(title));
	}
	
	//退出网页
	public void close() {
		this.driver.close();
	}
}
