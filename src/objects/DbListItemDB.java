package objects;

import javax.swing.JButton;

public class DbListItemDB {

	private String dbName, tableName, text;

	private JButton button;

	public DbListItemDB(String dbName, String tableName, String text, JButton button) {
		this.dbName = dbName;
		this.tableName = tableName;
		this.setText(text);
		this.setButton(button);
	}

	public DbListItemDB(String dbName) {
		this.setDbName(dbName);
	}

	public DbListItemDB(String dbName, String tableName) {
		this.setDbName(dbName);
		this.setTableName(tableName);
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	@Override
	public String toString() {
		if (tableName != null)
			return tableName;
		return dbName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public JButton getButton() {
		return button;
	}

	public void setButton(JButton button) {
		this.button = button;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
