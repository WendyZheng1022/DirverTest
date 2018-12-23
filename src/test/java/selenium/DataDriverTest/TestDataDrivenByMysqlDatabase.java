package selenium.DataDriverTest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.sql.*;


public class TestDataDrivenByMysqlDatabase {
	public WebDriver driver;
	String baseUrl= "https://www.sogo.com";
	
	//使用注解 DatProvider, 将数据集命名为 "testData"
	@DataProvider(name="testData")
	public static Object[][] words() throws IOException{
		//调用类中的静态方法getTestData,获取excel文件的测试数据
		return getTestData("testdat");
	}
	
		
	@Test(dataProvider="testData")
	public void testSearchSql(String searchWord1, String searchWord2, String searchResult) {
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
		
		//验证页面中是否包含sql"搜索结果"
		Assert.assertTrue(driver.getPageSource().contains(searchResult));
	}
	@BeforeMethod
	public void beforeMethod() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");	
		driver = new ChromeDriver();
	}	

	@AfterMethod
	public void afterMethod() {
		driver.quit();
	}
	
	/*
	 * 读取mysql
	 */
	private static Object[][] getTestData(String tableName) throws IOException {
		//声明mysql数据库驱动
		String driver = "com.mysql.cj.jdbc.Driver";
		//声明本地数据库的用户名和密码
		String url = "jdbc:mysql://localhost:3306/gloryroad?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT";
		String user = "root";
		String password = "*****";
		//声明存储测试数据的list对象
		List<Object[]> records = new ArrayList<Object[]>();
		try {
			//classLoader设定驱动
			Class.forName(driver); 
			//声明数据库的链接对象，服务器地址，用户名，密码
			Connection conn = DriverManager.getConnection(url, user, password);
			//如果数据连接可用，打印数据库连接成功的信息
			if(!conn.isClosed()) {
				System.out.println("连接数据库成功");
			}
			//创建statement对象
			Statement statement = conn.createStatement();
			//使用函数参数拼接要执行的query
			String sql = "select * from " + tableName + ";";
			//声明ResultSet对象，存取数据集
			ResultSet rs = statement.executeQuery(sql);
			//声明一个ResultSetMetaData对象
			ResultSetMetaData rsMetaData = rs.getMetaData();
			//调用ResultSetMetaData对象的getColumnCount方法获取行数
			int cols = rsMetaData.getColumnCount();
			//使用next方法遍历数据集
			while(rs.next()) {
				//声明一个字符数组
				String fields[] = new String[cols];
				int col=0;
				//遍历所有数据，并存储与字符数组中
				for(int i=0; i<cols; i++) {
					fields[col] = rs.getString(i+1);
					col++;
				}
				//将每行的数据存储到字符数组后，存储到records中
				records.add(fields);
				//输出数据集，验证数据内容是否正确
				System.out.println(rs.getString(1)+ " " + rs.getString(2) + " " + rs.getString(3));
			}
			//关闭数据结果对象
			rs.close();
			//关闭数据库连接
			conn.close();
		}catch(ClassNotFoundException e) {
			System.out.println("未能连接到数据库");
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
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
