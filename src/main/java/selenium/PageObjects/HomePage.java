package selenium.PageObjects;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
	public WebDriver driver;
	//邮箱页面元素是变化的id，所以用xpath模糊匹配
	@FindBy (xpath ="//*[contains(@id,'_mail_component_')]/span[contains(.,'写 信')]")
	public WebElement writeMailLink;
	
	@FindBy (xpath ="//*[contains(@id,'_mail_button_')]/span[contains(.,'发送')]")
	public WebElement sendMailButton;
	
	@FindBy(xpath = "//*[contains(@class,'nui-editableAddr-ipt')]")
	public WebElement mailAddress;
			
	
	//构造函数，函数参数赋值给类成员变量driver，初始化PageFactory
	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	//写信的封装方法
	public void writeMail() throws InterruptedException{
		//点击写信连接
		writeMailLink.click();
		Thread.sleep(5000);
		
		//使用黏贴函数，在正文里输入"邮件正文"
		setAndctrlClipBoardData("邮件正文-test-test");
		
		//不会把写邮件页面作为一个新的页面
		mailAddress.sendKeys("yulu_1022@163.com");
		
		//使用Tab键，焦点自动转入主题输入框
		pressTabKey();
		setAndctrlClipBoardData("邮件标题-test-test");
		
		//单击发送按钮
		sendMailButton.click();
		
	}
	
	//获取页面源码的封装方法
	public String getPageSource() {
		return driver.getPageSource();
	}
	
	//增加了需要覆盖的close方法
	public void close() {
		this.driver.close();
	}
	
	//设定剪贴板字符串粘贴的封装方法
	public static void setAndctrlClipBoardData(String string) {
		//模拟Ctrl+V，进行站台操作
		StringSelection stringSelection = new StringSelection(string);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		Robot robot = null;
		
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//按Tab键的封装方法
	public static void pressTabKey() {
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
	}

}
