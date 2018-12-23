package selenium.DataDriverTest;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.AfterMethod;

public class TestDataDrivenByExcelFile {
	public WebDriver driver;
	String baseUrl= "https://www.sogo.com";
	
	//使用注解 DatProvider, 将数据集命名为 "testData"
	@DataProvider(name="testData")
	public static Object[][] words() throws IOException{
		//调用类中的静态方法getTestData,获取excel文件的测试数据
		return getTestData("E:\\eclipse-workspace\\WebDirverTest\\test-data","testData.xlsx","Sheet1");
	}
		
	@Test(dataProvider="testData")
	public void testSearchExcel(String searchWord1, String searchWord2, String searchResult) {
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
		
		//验证页面中是否包含excel文件中"搜索结果"
		AssertJUnit.assertTrue(driver.getPageSource().contains(searchResult));
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
	 * 读取excel文件的静态方法，使用excel文件的绝对文件路径作为函数参数
	 */
	private static Object[][] getTestData(String filePath, String fileName, String sheetName) throws IOException {
		//根据参数传入的数据文件路径和文件名称，组合出Excel文件的绝对路径
		//声明一个File文件对象
		File file = new File(filePath + "\\" + fileName);
		//创建FileInputStream对象用于读取Excel文件
		FileInputStream inputStream = new FileInputStream(file);
		//声明一个Workbook文件对象
		Workbook workbook = null;
		//获取文件的扩展名，判断是.xlsx还是.xls文件
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		//若是.xlsx，则用XSSFWorkbook对象实例化
		//若是.xls，则用HSSFWorkbook对象实例化
		if(fileExtensionName.equals(".xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		}else if(fileExtensionName.equals(".xls")) {
			workbook = new HSSFWorkbook(inputStream);
		}
		
		//通过个sheetName参数，生成sheet对象
		Sheet sheet = workbook.getSheet(sheetName);
		//获取Excel数据文件Sheet1中数据的行数
		//getLastRowNum方法获取数据的最后一行行号
		//getFirstRowNum方法获取数据的第一行行号
		//相减之后算出数据的行数
		//注意Excel文件的行号和列号都是从0开始
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		//创建名为records的list对象来存储从Excel中读取的数据
		List<Object[]> records = new ArrayList<Object[]>();
		//使用两个for循环遍历excel，除了第一行数据列名称
		//i从第二行开始，即为1
		for (int i = 1; i < rowCount + 1; i++) {
			//使用getRow方法获取行对象
			Row row = sheet.getRow(i);
			//声明一个数组，用来存储excel
			//数据长度用getLastCellNum来动态声明
			String fields[] = new String[row.getLastCellNum()];
			for (int j = 0; j < row.getLastCellNum(); j++) {
				//Excel数据Cell有不同的类型，当我们试图从一个数字类型的Cell读取出一个字符串并写入数据库时，就会出现Cannot get a text value from a numeric cell的异常错误
				//解决办法：先设置Cell的类型，然后就可以把纯数字作为String类型读进来了
				//调用getCell和getStringCellValue获取单元格值
				if(row.getCell(j)!=null) {
					row.getCell(j).setCellType(CellType.STRING);
					fields[j]= row.getCell(j).getStringCellValue();	
				}		
			}
			//将fields的数据对象存储到records的list中
			records.add(fields);
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
