package utils;

import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * 
 * Author : Hemant Yadav
 * 
 */

public class BrowserFactory {

	protected Data data = Data.getInstance();
	public static RemoteWebDriver driver;
	protected Helper helper = new Helper();

	@BeforeClass
	public RemoteWebDriver initializeBrowser() throws IOException {
		if (System.getProperty("os.name").contains("Mac")) {
			System.setProperty("webdriver.chrome.driver", "src/resources/mac/chromedriver");

		} else {
			System.setProperty("webdriver.chrome.driver", "src/resources/linux/chromedriver");
		}
		
		try{
			driver = new ChromeDriver();
		}
		catch(UnreachableBrowserException e){
			driver = null;
			driver = new ChromeDriver();
		}
		
		driver.manage().window().maximize();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		return driver;
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

}
