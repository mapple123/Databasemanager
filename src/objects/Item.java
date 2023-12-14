package objects;

public class Item {
	
	private String[] columns;
	private Object[][] data;
	
	public Item(String[] columns, Object[][] data) {
		this.setColumns(columns);
		this.setData(data);
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	public Object[][] getData() {
		return data;
	}

	public void setData(Object[][] data) {
		this.data = data;
	}

}
