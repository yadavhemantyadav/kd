package heman.kd_timetable;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import utils.BrowserFactory;
import utils.GoogleSheetReader;

public class KDTime extends BrowserFactory {
	
	@BeforeMethod
	public void openURL() {
		//helper.openURL();
	}
	
	@Test
	public void abc() throws IOException{
		helper.selectDropDownById("stte", "Delhi");
		helper.sleep(2000);
		helper.selectDropDownById("brnch", "UTTAM NAGAR");
		helper.sleep(3000);
		List<WebElement> abc = helper.findElementsByXpath(".//*[@id='alinkdiv']/a");
				
		GoogleSheetReader writer = GoogleSheetReader.getInstance();
		
		Map<Object, Object> map = new HashMap<Object,Object>();;
				
		 map = writer.getSheetMap("1adE6-7qxa0KwxqYAyPPuvhdoq8NxUebVbhma4JcmOOo", "kd");
		
		for(WebElement x : abc){
			String date = x.getText().replaceAll("_UTTAM_NAGAR_TIME_TABLE.pdf","").split("_UTTAM_NAGAR_")[0];
			map.put(date, x.getAttribute("href"));
		}
		writer.writeToSheet("1adE6-7qxa0KwxqYAyPPuvhdoq8NxUebVbhma4JcmOOo", "kd", map);
		System.out.println(map);
	}
}
