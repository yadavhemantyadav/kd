package utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * Author : Hemant Yadav
 *
 */

public class Data {

	private static Data data;

	public static Data getInstance() {
		if (data == null) {
			data = new Data();
		}
		return data;
	}

	static final ConcurrentHashMap<String, Map<Object, Object>> cache = new ConcurrentHashMap<>();

	GoogleSheetReader reader = GoogleSheetReader.getInstance();
	public static String sheetId = "1adE6-7qxa0KwxqYAyPPuvhdoq8NxUebVbhma4JcmOOo";

	private static final Object o = new Object();

	private Map<Object, Object> getDataFromSheet(String sheetId, String sheetNameForElements) {
		if (cache.containsKey(sheetNameForElements)) {
			return cache.get(sheetNameForElements);
		}
		Map<Object, Object> elementMap = null;
		synchronized (o) {
			if (cache.containsKey(sheetNameForElements)) {
				return cache.get(sheetNameForElements);
			}
			System.out.println("Reading Data For : " + sheetNameForElements);
			elementMap = reader.getSheetMap(sheetId, sheetNameForElements);
			cache.put(sheetNameForElements, elementMap);
		}
		return elementMap;
	}

	/*
	 * Vivek's Method
	 */
	//static final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();
	//
	// private Map<Object, Object> getDataFromSheet(String sheetId, String
	// sheetNameForElements) {
	// if(cache.containsKey(sheetNameForElements)){
	// return cache.get(sheetNameForElements);
	// }
	//
	// Object lock = null;
	//
	// Object tmp = null;
	// if(locks.containsKey(sheetNameForElements)){
	// lock = locks.get(sheetNameForElements);
	// }else{
	// lock = new Object();
	// tmp = locks.putIfAbsent(sheetNameForElements, lock);
	// if(tmp != null){
	// lock = tmp;
	// }
	// }
	//
	// Map<Object, Object> elementMap = null;
	// synchronized (lock) {
	// if(cache.containsKey(sheetNameForElements)){
	// return cache.get(sheetNameForElements);
	// }
	// System.out.println("Reading Data For : " + sheetNameForElements);
	// elementMap = reader.getSheetMap(sheetId, sheetNameForElements);
	// cache.put(sheetNameForElements, elementMap);
	// }
	// return elementMap;
	// }

	private Data() {

	}

	public Map<Object, Object> getDataFromSheets(String dataType, String className) {
		Map<Object, Object> map;
		String sheetNameForElements = "";
		if (className.toLowerCase().equals("Helper".toLowerCase())) {
			sheetNameForElements = "Helper";
		} else if (className.toLowerCase().contains("DataBase".toLowerCase())) {
			sheetNameForElements = "DataBaseActions";
		} else if (className.toLowerCase().equals("TestCaseTransformer".toLowerCase())) {
			sheetNameForElements = "testCases";
		} else if (className.toLowerCase().contains("test".toLowerCase())) {
			className = className.replace("Test", "");
			sheetNameForElements = className.concat(dataType).trim();
		} else {
			sheetNameForElements = className.concat(dataType);
		}
		map = getDataFromSheet(sheetId, sheetNameForElements);
		return map;
	}
}
