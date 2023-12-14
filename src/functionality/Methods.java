package functionality;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.ImageIcon;

import mainpackage.ALL_CONST;
import objects.ColumnObj;

public class Methods {

	public static Connection connectionToDbFirst() throws Exception {
		HashMap<String, String> props = loadPropertiesForDBConnection(Methods.class);

		Class.forName(props.get(ALL_CONST.DRIVER_NAME));
		return DriverManager.getConnection(
				"jdbc:mysql://localhost:" + props.get(ALL_CONST.PORT) + "/" + props.get(ALL_CONST.DB)+ "?zeroDateTimeBehavior=convertToNull",
				props.get(ALL_CONST.USER), props.get(ALL_CONST.PASSWORD));
	}

	public static Connection connectionToDb(String db) throws Exception {
		HashMap<String, String> props = loadPropertiesForDBConnection(Methods.class);

		Class.forName(props.get(ALL_CONST.DRIVER_NAME));
		return DriverManager.getConnection("jdbc:mysql://localhost:" + props.get(ALL_CONST.PORT) + "/" + db + "?zeroDateTimeBehavior=convertToNull",
				props.get(ALL_CONST.USER), props.get(ALL_CONST.PASSWORD));
	}

	public static void connectionTest() {

		try {
			Connection con = connectionToDbFirst();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from `" + ALL_CONST.TABLE + "`");
			while (rs.next())
				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static HashMap<String, String> loadPropertiesForDBConnection(Class<?> mainObj) {
		URL keyFileURL = mainObj.getClassLoader().getResource("data/prop.properties");
		String rootPath = new File(keyFileURL.getFile()).getAbsolutePath();
		String appConfigPath = rootPath;
		Properties appProps = new Properties();

		try {
			appProps.load(new FileInputStream(appConfigPath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put(ALL_CONST.DRIVER_NAME, appProps.getProperty(ALL_CONST.DRIVER_NAME));
		properties.put(ALL_CONST.PORT, appProps.getProperty(ALL_CONST.PORT));
		properties.put(ALL_CONST.DB, appProps.getProperty(ALL_CONST.DB));
		properties.put(ALL_CONST.USER, appProps.getProperty(ALL_CONST.USER));
		properties.put(ALL_CONST.PASSWORD, appProps.getProperty(ALL_CONST.PASSWORD));
		return properties;
	}

	// TODO: flexible data -> now its only for primary columns with INT Type
	public static void updateItem(String db, String table, String newText, String column,
			ArrayList<String> primaryColumns, Object[] keys) throws Exception {
		Connection conn = connectionToDb(db);

		String sqlQuery = "UPDATE " + table + " SET `" + column + "`=? WHERE `" + primaryColumns.get(0) + "`=?";

		for (int counter = 1; counter <= primaryColumns.size(); counter++) {
			if (counter >= 1 && counter < primaryColumns.size()) {
				sqlQuery += " AND `" + primaryColumns.get(counter) + "`=?";
			}
		}

		PreparedStatement updateEXP = conn.prepareStatement(sqlQuery);

		for (int i = 2; i < primaryColumns.size() + 2; i++) {
			updateEXP.setString(i, keys[i - 2].toString());
			System.out.println(keys[i - 2].toString());
		}

		updateEXP.setString(1, newText);
		updateEXP.executeUpdate();

	}

	public static void updateItem(String db, String table, Object[] newValues, String[] columns, Object[] oldValues)
			throws Exception {
		Connection conn = connectionToDb(db);

		Statement st = conn.createStatement();

		System.out.println("SELECT * from `" + table + "`");

		ResultSet rs2 = st.executeQuery("SELECT * from `" + table + "`");

		ResultSetMetaData rsMetaData2 = rs2.getMetaData();

		int count2 = rsMetaData2.getColumnCount();

		String[] types = new String[count2];

		for (int i = 1; i <= count2; i++) {

			types[i - 1] = rsMetaData2.getColumnTypeName(i);
			System.out.println(types[i - 1]);
		}

		st.close();

		PreparedStatement preparedStmt;
		int i = 0;

		String columnPart = "";

		int c = 0;
		for (String column : columns) {
			String singleQuotes = "";
			if (isTextType(types[c])) {
				singleQuotes = "'";
			}
			columnPart += "`" + column + "`=" + singleQuotes + newValues[c] + singleQuotes;
			if (c >= 0 && c < columns.length - 1)
				columnPart += ",";

			c++;
		}

		String sqlQuery = "UPDATE `" + table + "` SET " + columnPart + " WHERE `" + columns[i] + "`=?";
		for (int counter = 1; counter <= columns.length; counter++) {
			if (counter >= 1 && counter < columns.length) {
				sqlQuery += " AND `" + columns[counter] + "`=?";
			}
			i++;
		}

		System.out.println(sqlQuery);

		preparedStmt = conn.prepareStatement(sqlQuery);

		for (int o = 0; o < oldValues.length; o++) {

			preparedStmt.setObject(o + 1, oldValues[o]);

		}

		preparedStmt.execute();
		conn.close();

	}

	public static ArrayList<String> getAllDBNames() throws Exception {
		ArrayList<String> names = new ArrayList<>();
		Connection connection = connectionToDbFirst();

		DatabaseMetaData metadata = connection.getMetaData();
		ResultSet result = metadata.getCatalogs();

		while (result.next()) {
			String aDBName = result.getString(1);
			names.add(aDBName);
		}

		connection.close();

		return names;
	}

	public static ArrayList<String> getAllTables(String db) throws Exception {
		ArrayList<String> names = new ArrayList<>();
		Connection connection = connectionToDb(db);

		DatabaseMetaData md = connection.getMetaData();
		ResultSet rs = md.getTables(db, null, "%", null);
		while (rs.next()) {
			names.add(rs.getString(3));
		}

		return names;
	}

	public static ArrayList<ColumnObj> getAllColumnsFromSelectedTable(String db, String table) throws Exception {
		ArrayList<ColumnObj> columns = new ArrayList<>();

		Connection con = connectionToDb(db);

		Statement stmt = con.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT * from `" + table + "`");

		ResultSetMetaData rsMetaData = rs.getMetaData();

		int count = rsMetaData.getColumnCount();

		for (int i = 1; i <= count; i++) {
			String name = null, type = null;
			name = rsMetaData.getColumnName(i);
			type = rsMetaData.getColumnTypeName(i) + "(" + rsMetaData.getColumnDisplaySize(i) + ")";
			columns.add(new ColumnObj(name, type, count));
		}

		stmt.close();
		con.close();

		return columns;
	}

	public static ArrayList<ColumnObj> getAllColumnsWithDataFromSelectedTable(String db, String table,
			String[] columnsA, Object[] rowValues) throws Exception {

		Connection connection = Methods.connectionToDb(db);

		Statement st = connection.createStatement();

		System.out.println("SELECT * from `" + table + "`");

		ResultSet rs2 = st.executeQuery("SELECT * from `" + table + "`");

		ResultSetMetaData rsMetaData2 = rs2.getMetaData();

		int count2 = rsMetaData2.getColumnCount();

		String[] types = new String[count2];

		for (int i = 1; i <= count2; i++) {

			types[i - 1] = rsMetaData2.getColumnTypeName(i);
			System.out.println(types[i - 1]);
		}

		st.close();

		// Query
		String sqlQuery = "";

		for (int counter = 1; counter <= columnsA.length; counter++) {
			String singleQuotes = "";
			if (isTextType(types[counter - 1])) {
				singleQuotes = "'";
			}
			if (counter - 1 == 0) {
				sqlQuery += "SELECT * FROM `" + table + "` WHERE `" + columnsA[counter - 1] + "`=" + singleQuotes
						+ rowValues[counter - 1] + singleQuotes;
			} else if (counter >= 1 && counter <= columnsA.length) {
				sqlQuery += " AND `" + columnsA[counter - 1] + "`=" + singleQuotes + rowValues[counter - 1]
						+ singleQuotes;
			}

		}

		System.out.println(sqlQuery);

		ArrayList<ColumnObj> columns = new ArrayList<>();

		Connection con = connectionToDb(db);

		Statement stmt = con.createStatement();

		ResultSet rs = stmt.executeQuery(sqlQuery);

		ResultSetMetaData rsMetaData = rs.getMetaData();

		int count = rsMetaData.getColumnCount();

		Object data = null;

		while (rs.next()) {
			String name = null, type = null;
			for (int i = 1; i <= count; i++) {

				name = rsMetaData.getColumnName(i);
				type = rsMetaData.getColumnTypeName(i) + "(" + rsMetaData.getColumnDisplaySize(i) + ")";
				data = rs.getObject(i);

				columns.add(new ColumnObj(name, type, count, data));
			}
			break;

		}

		stmt.close();
		con.close();

		return columns;
	}

	public static void editProperties(Class<?> mainObj, String[] properties, String[] values) {
		URL keyFileURL = mainObj.getClassLoader().getResource("data/prop.properties");
		String rootPath = new File(keyFileURL.getFile()).getAbsolutePath();
		String appConfigPath = rootPath;
		Properties appProps = new Properties();

		try (FileInputStream in = new FileInputStream(appConfigPath)) {
			appProps.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < properties.length; i++) {
			appProps.setProperty(properties[i], values[i]);
		}

		try (FileOutputStream out = new FileOutputStream(appConfigPath)) {
			appProps.store(out, null);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void createPropertyFile(Class<?> mainObj, final String port, final String db, final String user,
			final String password) {

		try {
			URL keyFileURL = mainObj.getClassLoader().getResource("data");
			File dataDirectory = null;
			if (keyFileURL == null) {
				dataDirectory = new File(mainObj.getClassLoader().getResource("").getPath() + File.separator + "data");
			} else
				dataDirectory = new File(keyFileURL.getFile());

			if (dataDirectory != null && !dataDirectory.exists()) {
				dataDirectory.mkdirs();
				System.out.println("Erfolgreich esrtellt!");
			}
			String rootPath = dataDirectory.getAbsolutePath();
			String appConfigPath = rootPath + File.separator + "prop.properties";

			// Creating properties files from Java program
			Properties properties = new Properties();

			// In the name of userCreated.properties, in the
			// current directory location, the file is created
			FileOutputStream fileOutputStream = new FileOutputStream(appConfigPath);

			properties.setProperty(ALL_CONST.DRIVER_NAME, ALL_CONST.STD_DRIVER_NAME);
			properties.setProperty(ALL_CONST.PORT, port);
			properties.setProperty(ALL_CONST.DB, db);
			properties.setProperty(ALL_CONST.USER, user);
			properties.setProperty(ALL_CONST.PASSWORD, password);

			// writing properties into properties file
			// from Java As we are writing text format,
			// store() method is used
			properties.store(fileOutputStream, null);

			fileOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getPrimaryKeyColumnsForTable(Connection connection, String tableName)
			throws SQLException {
		try (ResultSet pkColumns = connection.getMetaData().getPrimaryKeys(null, null, tableName)) {
			ArrayList<String> pkColumnSet = new ArrayList<>();
			while (pkColumns.next()) {
				String pkColumnName = pkColumns.getString("COLUMN_NAME");
				Integer pkPosition = pkColumns.getInt("KEY_SEQ");
				System.out.println("" + pkColumnName + " is the " + pkPosition
						+ ". column of the primary key of the table " + tableName);
				pkColumnSet.add(pkColumnName);
			}
			return pkColumnSet;
		}
	}

	public static Object[] getPrimaryKeyColumnsData(String db, String table, ArrayList<String> primaryColumns,
			Object[] rowValues) throws Exception {
		Connection connection = Methods.connectionToDb(db);
		Object[] data = null;

		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String columns = "";

		for (int i = 0; i < primaryColumns.size(); i++) {
			columns += "`" + primaryColumns.get(i) + "`";
			if (i >= 0 && i < primaryColumns.size() - 1)
				columns += ",";
		}

		// Query
		String sqlQuery = "SELECT " + columns + " FROM `" + table + "` WHERE `" + primaryColumns.get(0) + "`="
				+ rowValues[0];

		for (int counter = 1; counter <= primaryColumns.size(); counter++) {
			if (counter >= 1 && counter < primaryColumns.size()) {
				sqlQuery += " AND `" + primaryColumns.get(counter) + "`=" + rowValues[counter];
			}
		}
		System.out.println(sqlQuery);
		ResultSet resultSet = statement.executeQuery(sqlQuery);

		int size = 0;
		if (resultSet != null) {
			resultSet.beforeFirst();
			resultSet.last();
			size = resultSet.getRow();
			resultSet.beforeFirst();
		}

		data = new Object[size];
		int z = 0;
		while (resultSet.next()) {
			System.out.println("TEST");

			data[z] = resultSet.getObject(z + 1);
			System.out.println("REERERERRE: " + (z) + "   " + resultSet.getObject(z + 1).toString());
			z++;

		}

		// Close DB connection
		statement.close();
		connection.close();

		return data;
	}

	/*
	 * Load imgIcon with sizing
	 */

	public static ImageIcon loadImage(String imgName, int width, int height) {
		URL urlDb = ClassLoader.getSystemResource("res" + File.separator + imgName);
		Image resultingImage = Toolkit.getDefaultToolkit().getImage(urlDb).getScaledInstance(width, height,
				Image.SCALE_SMOOTH);
		return new ImageIcon(resultingImage);
	}

	public static void createNewDB(String dbName) throws Exception {
		Connection conn = connectionToDbFirst();
		Statement s = conn.createStatement();
		s.executeUpdate("CREATE DATABASE `" + dbName + "`");
	}

	public static void insertNewData(String db, String table, String[] columns, String... values) throws Exception {
		Connection conn = connectionToDb(db);

		String columnsPart = "";
		String valuesPart = "";
		for (int i = 0; i < columns.length; i++) {
			columnsPart += "`" + columns[i] + "`";
			valuesPart += "?";
			if (i >= 0 && i < columns.length - 1) {
				columnsPart += ",";
				valuesPart += ",";
			}
		}

		String sql = "INSERT INTO `" + table + "`(" + columnsPart + ") VALUES(" + valuesPart + ")";

		System.out.println(sql);

		PreparedStatement preparedStmt = conn.prepareStatement(sql);

		int c = 1;
		for (String value : values) {
			System.out.println(c + "    " + value);
			preparedStmt.setString(c, value);
			c++;
		}
		preparedStmt.execute();
		conn.close();

	}

	public static void testConn(String db, String port, String username, String password) throws Exception {

		System.out.println("Connecting to server...");
		String url = "jdbc:mysql://localhost:" + port + "/" + db;
		Connection connection = DriverManager.getConnection(url, username, password);
		System.out.println("Server connected!");
		Statement stmt = null;
		ResultSet resultset = null;

		stmt = connection.createStatement();
		resultset = stmt.executeQuery("SHOW DATABASES;");

		if (stmt.execute("SHOW DATABASES;")) {
			resultset = stmt.getResultSet();
		}

		while (resultset.next()) {
			System.out.println(resultset.getString("Database"));
		}

		// release resources
		if (resultset != null) {

			resultset.close();
		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException sqlEx) {
			}
			stmt = null;
		}

		if (connection != null) {
			connection.close();
		}
	}

	public static void deleteOldData(String db, String table, String[] columns, Object[] rowValues) throws Exception {
		Connection connection = Methods.connectionToDb(db);

		Statement stmt = connection.createStatement();

		System.out.println("SELECT * from `" + table + "`");

		ResultSet rs = stmt.executeQuery("SELECT * from `" + table + "`");

		ResultSetMetaData rsMetaData = rs.getMetaData();

		int count = rsMetaData.getColumnCount();

		String[] types = new String[count];

		for (int i = 1; i <= count; i++) {

			types[i - 1] = rsMetaData.getColumnTypeName(i);
			System.out.println(types[i - 1]);
		}

		stmt.close();

		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		// Query
		String sqlQuery = "";

		for (int counter = 1; counter <= columns.length; counter++) {
			String singleQuotes = "";
			if (isTextType(types[counter - 1])) {
				singleQuotes = "'";
			}
			if (counter - 1 == 0) {
				sqlQuery += "DELETE FROM `" + table + "` WHERE `" + columns[counter - 1] + "`=" + singleQuotes
						+ rowValues[counter - 1] + singleQuotes;
			} else if (counter >= 1 && counter <= columns.length) {
				sqlQuery += " AND `" + columns[counter - 1] + "`=" + singleQuotes + rowValues[counter - 1]
						+ singleQuotes;
			}

		}
		System.out.println(sqlQuery);
		statement.executeUpdate(sqlQuery);

		// Close DB connection
		statement.close();
		connection.close();

	}

	private static boolean isTextType(String columnType) {
		return columnType != null && (columnType.equalsIgnoreCase("VARCHAR") || columnType.equalsIgnoreCase("TEXT"));
	}

}
