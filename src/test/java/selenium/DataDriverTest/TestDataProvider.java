package selenium.DataDriverTest;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.DataProvider;

public class TestDataProvider {
	private static WebDriver driver;
	
	@DataProvider(name = "searchWords")
	
	public static Object[][] words(){
		return new Object[][] {{"蝙蝠侠","主演","迈克尔"},{"超人","导演","唐纳"},{"生化危机","编剧","安德森"}};	
	}
	
	
	@Test(dataProvider = "searchWords")
	public void test(String searchWorld1, String searchWorld2, String SearchResult) {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		
		driver = new ChromeDriver();
		
		//wait 10 seconds
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//got to sogo homepage
		driver.get("https://www.sogo.com");
		
		//searching
		driver.findElement(By.id("query")).sendKeys(searchWorld1 + "" + searchWorld2);
		
		//click searching button
		driver.findElement(By.id("stb")).click();
		
		//wait results
		try {
			Thread.sleep(3000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		//check results
		AssertJUnit.assertTrue(driver.getPageSource().contains(SearchResult));
		driver.close();
	}
}
