package functionality;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
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

import javax.swing.ImageIcon;

import lang_res.Consts;
import objects.ColumnObj;


//TODO: create good new classes for the separation of this class
/**
 * Klasse fuer alle benoetigten Methoden
 * 
 * Entwickler: Jan Schwenger
 */
public class Methods {

	//TODO: Perhaps deleting 
	// Methode fuer die erste Verbindung mit der Datenbank
	public static Connection connectionToDbFirst() throws Exception {
		HashMap<String, String> props = loadPropertiesForDBConnection(Methods.class);

		Class.forName(props.get(Consts.DRIVER_NAME));
		return DriverManager.getConnection(
				"jdbc:mysql://localhost:" + props.get(Consts.PORT) + "/" + props.get(Consts.DB)
						+ "?zeroDateTimeBehavior=convertToNull",
				props.get(Consts.USER), props.get(Consts.PASSWORD));
	}

	// Allgemeine Verbindung zu der Datenbank
	public static Connection connectionToDb(String db) throws Exception {
		HashMap<String, String> props = loadPropertiesForDBConnection(Methods.class);

		Class.forName(props.get(Consts.DRIVER_NAME));
		return DriverManager.getConnection(
				"jdbc:mysql://localhost:" + props.get(Consts.PORT) + "/" + db
						+ "?zeroDateTimeBehavior=convertToNull",
				props.get(Consts.USER), props.get(Consts.PASSWORD));
	}

	//TODO: Delete this method (For test purposes) 
	public static void connectionTest() {

		try {
			Connection con = connectionToDbFirst();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from `" + Consts.TABLE + "`");
			while (rs.next())
				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	//Laedt die DB Properties aus der Property File
	private static HashMap<String, String> loadPropertiesForDBConnection(Class<?> mainObj) {
		URL keyFileURL = mainObj.getClassLoader().getResource("data/prop.properties");
		String rootPath = new File(keyFileURL.getFile()).getAbsolutePath();
		String appConfigPath = rootPath;
		Properties appProps = new Properties();

		try {
			appProps.load(new FileInputStream(appConfigPath));
		} catch (IOException error ) {
			error.printStackTrace();
		}
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put(Consts.DRIVER_NAME, appProps.getProperty(Consts.DRIVER_NAME));
		properties.put(Consts.PORT, appProps.getProperty(Consts.PORT));
		properties.put(Consts.DB, appProps.getProperty(Consts.DB));
		properties.put(Consts.USER, appProps.getProperty(Consts.USER));
		properties.put(Consts.PASSWORD, appProps.getProperty(Consts.PASSWORD));
		return properties;
	}

	// Look up whether the method is be used in any context
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

	// Methode, die fuer das Updaten von Datensaetzen in den Tabellen innerhalb einer Datenbank zustaendig ist
	public static void updateItem(String db, String table, Object[] newValues, String[] columns, Object[] oldValues)
			throws Exception {
		Connection connection = connectionToDb(db);

		Statement statement = connection.createStatement();

		//TODO: Delete this line
		//System.out.println("SELECT * from `" + table + "`");

		ResultSet result = statement.executeQuery("SELECT * from `" + table + "`");

		ResultSetMetaData rsMetaData = result.getMetaData();

		int count = rsMetaData.getColumnCount();

		String[] types = new String[count];

		for (int i = 1; i <= count; i++) {

			types[i - 1] = rsMetaData.getColumnTypeName(i);
			//System.out.println(types[i - 1]);
		}

		statement.close();

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

		//System.out.println(sqlQuery);

		preparedStmt = connection.prepareStatement(sqlQuery);

		for (int o = 0; o < oldValues.length; o++) {

			preparedStmt.setObject(o + 1, oldValues[o]);

		}

		preparedStmt.execute();
		connection.close();

	}

	// Gibt alle Datenbanken als String Array-List zurueck
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

	// Gibt alle Tabellen als String Array-List zurueck
	public static ArrayList<String> getAllTables(String db) throws Exception {
		ArrayList<String> names = new ArrayList<>();
		Connection connection = connectionToDb(db);

		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet result = metaData.getTables(db, null, "%", null);
		while (result.next()) {
			names.add(result.getString(3));
		}

		return names;
	}

	// Gibt alle Spalten von der selektierten Tabelle in Form einer ColumnObject Array-List zurueck
	public static ArrayList<ColumnObj> getAllColumnsFromSelectedTable(String db, String table) throws Exception {
		ArrayList<ColumnObj> columns = new ArrayList<>();

		Connection connection = connectionToDb(db);

		Statement statement = connection.createStatement();

		ResultSet result = statement.executeQuery("SELECT * from `" + table + "`");

		ResultSetMetaData rsMetaData = result.getMetaData();

		int count = rsMetaData.getColumnCount();

		for (int i = 1; i <= count; i++) {
			String name = null, type = null;
			name = rsMetaData.getColumnName(i);
			type = rsMetaData.getColumnTypeName(i) + "(" + rsMetaData.getColumnDisplaySize(i) + ")";
			columns.add(new ColumnObj(name, type, count));
		}

		statement.close();
		connection.close();

		return columns;
	}

	
	// Gibt alle Spalten mit den dazugehoerigen Daten als ColumnObject in einer Array-List zurueck
	public static ArrayList<ColumnObj> getAllColumnsWithDataFromSelectedTable(String db, String table,
			String[] columnsA, Object[] rowValues) throws Exception {

		Connection connection = Methods.connectionToDb(db);

		Statement statement = connection.createStatement();

		//System.out.println("SELECT * from `" + table + "`");

		ResultSet result = statement.executeQuery("SELECT * from `" + table + "`");

		ResultSetMetaData rsMetaData = result.getMetaData();

		int count = rsMetaData.getColumnCount();

		String[] types = new String[count];

		for (int i = 1; i <= count; i++) {

			types[i - 1] = rsMetaData.getColumnTypeName(i);
			//System.out.println(types[i - 1]);
		}

		statement.close();

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

		//System.out.println(sqlQuery);

		ArrayList<ColumnObj> columns = new ArrayList<>();

		//Connection con = connectionToDb(db);

		Statement statment2 = connection.createStatement();

		result = statment2.executeQuery(sqlQuery);

		rsMetaData = result.getMetaData();

		count = rsMetaData.getColumnCount();

		Object data = null;

		while (result.next()) {
			String name = null, type = null;
			for (int i = 1; i <= count; i++) {

				name = rsMetaData.getColumnName(i);
				type = rsMetaData.getColumnTypeName(i) + "(" + rsMetaData.getColumnDisplaySize(i) + ")";
				data = result.getObject(i);

				columns.add(new ColumnObj(name, type, count, data));
			}
			break;

		}

		statement.close();
		connection.close();

		return columns;
	}
	
	
	// Methode zum Bearbeiten der PropertyFile mit den Datenbankconfigurationsparametern
	public static void editProperties(Class<?> mainObj, String[] properties, String[] values) {
		URL keyFileURL = mainObj.getClassLoader().getResource("data/prop.properties");
		String rootPath = new File(keyFileURL.getFile()).getAbsolutePath();
		String appConfigPath = rootPath;
		Properties appProps = new Properties();

		try (FileInputStream inputStream = new FileInputStream(appConfigPath)) {
			appProps.load(inputStream);
			inputStream.close();
		} catch (IOException error) {
			error.printStackTrace();
		}

		for (int i = 0; i < properties.length; i++) {
			appProps.setProperty(properties[i], values[i]);
		}

		try (FileOutputStream outputStream = new FileOutputStream(appConfigPath)) {
			appProps.store(outputStream, null);
			outputStream.close();
		} catch (IOException error) {
			error.printStackTrace();
		}

	}

	// Methode fuer das Erstellen der Konfigurationsdatei fuer die Datenbank
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
				//System.out.println("Erfolgreich esrtellt!");
			}
			String rootPath = dataDirectory.getAbsolutePath();
			String appConfigPath = rootPath + File.separator + "prop.properties";

		
			Properties properties = new Properties();

			
			FileOutputStream fileOutputStream = new FileOutputStream(appConfigPath);

			properties.setProperty(Consts.DRIVER_NAME, Consts.STD_DRIVER_NAME);
			properties.setProperty(Consts.PORT, port);
			properties.setProperty(Consts.DB, db);
			properties.setProperty(Consts.USER, user);
			properties.setProperty(Consts.PASSWORD, password);

			properties.store(fileOutputStream, null);

			fileOutputStream.close();

		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	// Methode fuer das Bekommen des Primaerschluesselsspalten einer Tabelle
	public static ArrayList<String> getPrimaryKeyColumnsForTable(Connection connection, String tableName)
			throws SQLException {
		try (ResultSet pkColumns = connection.getMetaData().getPrimaryKeys(null, null, tableName)) {
			ArrayList<String> pkColumnSet = new ArrayList<>();
			while (pkColumns.next()) {
				String pkColumnName = pkColumns.getString("COLUMN_NAME");
				@SuppressWarnings("unused")
				Integer pkPosition = pkColumns.getInt("KEY_SEQ");
				//System.out.println("" + pkColumnName + " is the " + pkPosition
				//		+ ". column of the primary key of the table " + tableName);
				pkColumnSet.add(pkColumnName);
			}
			return pkColumnSet;
		}
	}

	// Methode fuer das Bekommen der Primaerschluessels inklusive Daten
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

		String sqlQuery = "SELECT " + columns + " FROM `" + table + "` WHERE `" + primaryColumns.get(0) + "`="
				+ rowValues[0];

		for (int counter = 1; counter <= primaryColumns.size(); counter++) {
			if (counter >= 1 && counter < primaryColumns.size()) {
				sqlQuery += " AND `" + primaryColumns.get(counter) + "`=" + rowValues[counter];
			}
		}
		//System.out.println(sqlQuery);
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
			//System.out.println("TEST");

			data[z] = resultSet.getObject(z + 1);
			//System.out.println("REERERERRE: " + (z) + "   " + resultSet.getObject(z + 1).toString());
			z++;

		}

		
		statement.close();
		connection.close();

		return data;
	}

	// Methode fuer das Laden und Skalieren eines Bildes
	public static ImageIcon loadImage(String imgName, int width, int height) {
		URL urlDb = ClassLoader.getSystemResource("res" + File.separator + imgName);
		Image resultingImage = Toolkit.getDefaultToolkit().getImage(urlDb).getScaledInstance(width, height,
				Image.SCALE_SMOOTH);
		return new ImageIcon(resultingImage);
	}

	// Methode fuer das neue Erstellen einer Datenbank
	public static void createNewDB(String dbName) throws Exception {
		Connection connection = connectionToDbFirst();
		Statement statement = connection.createStatement();
		statement.executeUpdate("CREATE DATABASE `" + dbName + "`");
	}

	// Methode fuer das Einfuegen neuere Datensaetze in die Tabelle
	public static void insertNewData(String db, String table, String[] columns, String... values) throws Exception {
		Connection connection = connectionToDb(db);

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

		String sqlQuery = "INSERT INTO `" + table + "`(" + columnsPart + ") VALUES(" + valuesPart + ")";

		//System.out.println(sqlQuery);

		PreparedStatement preparedStmt = connection.prepareStatement(sqlQuery);

		int c = 1;
		for (String value : values) {
			//System.out.println(c + "    " + value);
			preparedStmt.setString(c, value);
			c++;
		}
		preparedStmt.execute();
		connection.close();

	}

	// TODO: Delete only for testing
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

	// Methode zum Loeschen alter Datensaetze innerhalb einer Tabelle
	public static void deleteOldData(String db, String table, String[] columns, Object[] rowValues) throws Exception {
		Connection connection = Methods.connectionToDb(db);

		Statement statement = connection.createStatement();

		//System.out.println("SELECT * from `" + table + "`");

		ResultSet result = statement.executeQuery("SELECT * from `" + table + "`");

		ResultSetMetaData rsMetaData = result.getMetaData();

		int count = rsMetaData.getColumnCount();

		String[] types = new String[count];

		for (int i = 1; i <= count; i++) {

			types[i - 1] = rsMetaData.getColumnTypeName(i);
			System.out.println(types[i - 1]);
		}

		statement.close();

		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		
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
		//System.out.println(sqlQuery);
		statement.executeUpdate(sqlQuery);

	
		statement.close();
		connection.close();

	}

	// Methode zum Nachschauen, ob der Datensatztyp Text ist
	private static boolean isTextType(String columnType) {
		return columnType != null && (columnType.equalsIgnoreCase("VARCHAR") || columnType.equalsIgnoreCase("TEXT"));
	}

}
