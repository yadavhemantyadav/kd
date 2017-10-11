package heman.kd_timetable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import utils.DataBaseActions;

public class BrowserStack{

	public static DataBaseActions dataBaseAction = DataBaseActions.getInstance();

	public static String todayDate() {
		String PATTERN = "dd-MMM-yy";
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern(PATTERN);
		String date1 = dateFormat.format(Calendar.getInstance().getTime());
		return date1;
	}

	public static String getPDFURL(String date) {
		String query = "Select url from TimeTableInfo where dates = '" + date + "';";
		System.out.println(query);

		String url = dataBaseAction.getDataFromDB(query, 1, DBURL, DBUSER, DBPASS);
		return url;
	}

	public static final String USERNAME = "hemant162";
	public static final String AUTOMATE_KEY = "RZ7dY1dtwSz1YZzHguA8";
	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

	public static final String DBUSER = "hemant";
	public static final String DBPASS = "abc@abc";
	public static final String DBURL = "jdbc:mysql://ec2-13-59-131-1.us-east-2.compute.amazonaws.com/kd";

	//public static void main(String[] args) throws Exception {

	@Test
	public static void testThis() throws IOException{
	
		WebDriver driver = new RemoteWebDriver(new URL(URL), DesiredCapabilities.firefox());
		driver.get("http://www.kdcampus.org/uttamnagar/course-routines");
		List<WebElement> abc = driver.findElements(By.xpath("html/body/div[3]/div[2]/div/div[2]/div/div/div/div/a"));

		DataBaseActions dataBaseAction = DataBaseActions.getInstance();

		for (WebElement x : abc) {
			String date = x.getText().replaceAll("_UTTAM_NAGAR_TIME_TABLE.pdf", "").split("_UTTAM_NAGAR_")[0];
			
			String query = "INSERT INTO TimeTableInfo (dates, url) " + "VALUES ('" + date + "','"
					+ x.getAttribute("href") + "');";

			System.out.println(query);
			dataBaseAction.insertIntoDB(query, DBURL, DBUSER, DBPASS);
		}

		driver.quit();

		String date = todayDate();

		String url = getPDFURL(date);

		List<String> pdfData = ReadPdf.readPDF(url);

		for (String s : pdfData) {
			Map<String, String> dataMap = parseStringToMap(s);
			putTimeTableDetailsInDB(dataMap);
		}
		deleteURLDetails();
	}

	public static void putTimeTableDetailsInDB(Map<String, String> abc) {
		String query = "INSERT INTO TimeTableDetails (teacher_name, dates, batch_code, duration1, duration2, duration, hall_number) "
				+ "VALUES ( ' ' , '" + abc.get("dates") + "','" + abc.get("batchcode") + "','" + abc.get("duration1") + "','"
				+ abc.get("duration2") + "','" + abc.get("duration") + "','" + abc.get("hallno")+ "');";
		System.out.println(query);
		dataBaseAction.insertIntoDB(query, DBURL, DBUSER, DBPASS);
	}

	public static Map<String, String> parseStringToMap(String s) {
		Map<String, String> abc = new HashMap<String, String>();
		String[] dataArray = s.split(" ");
		abc.put("hallno", dataArray[dataArray.length - 1]);
		abc.put("duration", dataArray[dataArray.length - 2]);
		abc.put("duration2", dataArray[dataArray.length - 3]);
		abc.put("duration1", dataArray[dataArray.length - 4]);
		abc.put("batchcode", dataArray[dataArray.length - 5]);
		abc.put("dates", dataArray[dataArray.length - 6]);
		return abc;
	}

	public static void deleteURLDetails() {
		String query = "Truncate table TimeTableInfo;";
		String url = dataBaseAction.getDataFromDB(query, 1, DBURL, DBUSER, DBPASS);
	}
}
