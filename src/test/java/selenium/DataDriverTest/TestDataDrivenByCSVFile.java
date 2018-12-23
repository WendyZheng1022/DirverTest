package selenium.DataDriverTest;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;


public class TestDataDrivenByCSVFile {
	public WebDriver driver;
	String baseUrl= "https://www.sogo.com";
	
	//使用注解 DatProvider, 将数据集命名为 "testData"
	@DataProvider(name = "testData")
	public static Object[][] words() throws IOException{
		//调用类中的静态方法getTestData,获取CSV文件的测试数据
		return getTestData("E:\\eclipse-workspace\\WebDirverTest\\test-data\\testData.csv");
	}
	
	@BeforeMethod
	public void beforeMethod() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");	
		driver = new ChromeDriver();
	}	

	@Test(dataProvider="testData")
	public void testSearch(String searchWord1, String searchWord2, String searchResult) {
		//打开sogo主页
		driver.get(baseUrl + "/");
		//使用CSV文件中的每个数据行的前两个词作为搜索词
		//在两个搜索词中加一个空格
		driver.findElement(By.id("query")).sendKeys(searchWord1 + " " + searchWord2);
		driver.findElement(By.id("stb")).click();
		//使用显示等待的方式，确认页面已经加载完成，页面底部的关键字"意见反馈及投诉"已经显示在页面上
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver d) {
				return d.findElement(By.id("s_footer")).getText().contains("意见反馈及投诉");
			}
		});
		
		//验证页面中是否包含CSV文件中"搜索结果"
		AssertJUnit.assertTrue(driver.getPageSource().contains(searchResult));
	}
	
	
	
	@AfterMethod
	public void afterMethod() {
		driver.quit();
	}
	
	/*
	 * 读取CSV文件的静态方法，使用csv文件的绝对文件路径作为函数参数
	 */
	private static Object[][] getTestData(String fileName) throws IOException {
		
		List<Object[]> records = new ArrayList<Object[]>();
		String record;
		
		//设定UTF-8字符集，使用带缓冲区的字符输入流bufferedReader读取文件内容
		BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
		//忽略读第一行（标题行）
		file.readLine();
		//遍历读取文件中除第一行以外的其他所有内容，并存储在records的ArrayList中
		//每个records中存储的对象为一个String数组		
		while ((record=file.readLine())!=null) {
			String fields[] = record.split(",");
			records.add(fields);
		}
		file.close();
		
		//定义函数的返回值，即object[][]
		//将存储测试数据的list转换为一个object的二维数组
		Object[][] results= new Object[records.size()][];
		//设置二维数组每行的值，每行是一个object对象
		for (int i = 0; i < records.size(); i++) {
			results[i] = records.get(i);
		}
		return results;
	}
	
}
