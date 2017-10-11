package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author AkashAggarwal
 * @description this class will be used for DBqueries
 *
 */
public class DataBaseActions {

	private static DataBaseActions dataBaseActions;

	Map<Object, Object> elementMap;
	String URL = "";
	String PASSWORD = "";
	String USER = "";

	Data data = Data.getInstance();

	private DataBaseActions() {

	}

	public static DataBaseActions getInstance() {
		if (dataBaseActions == null) {
			dataBaseActions = new DataBaseActions();
		}
		return dataBaseActions;
	}

	private void getConnectionDetails(String className) {
		elementMap = data.getDataFromSheets("", className);
	}
	
	public void insertIntoDB(String query, String connection_url, String db_user,
			String db_password){
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(connection_url, db_user, db_password);
			System.out.println("connected");
			Statement st = con.createStatement();
			System.out.println("going to execute");
			st.executeUpdate(query);
			System.out.println("executed");
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	

	public String getDataFromDB(String query, int columnIndex, String connection_url, String db_user,
			String db_password) {

		String data = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(connection_url, db_user, db_password);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				data = rs.getString(columnIndex);
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println(data);
		return data;
	}

	public void deleteFromDB(List<String> queries, String className, String connection_url, String db_user,
			String db_password) {

		getConnectionDetails(className);

		URL = elementMap.get(connection_url).toString();
		USER = elementMap.get(db_user).toString();
		PASSWORD = elementMap.get(db_password).toString();

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt = con.createStatement();
			for (int i = 0; i < queries.size(); i++) {
				stmt.addBatch(queries.get(i));
			}
			int[] executeBatch = stmt.executeBatch();
			System.out.println(executeBatch);
			System.out.println("done");
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// public static void main(String args[]){
	// DataBaseActions data = DataBaseActions.getInstance();
	// data.getDataFromDB("SELECT * from cscart_otp_details", 1);
	// }

}
