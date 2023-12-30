package objects;

/**
 * Klasse fuer ein  ColumnObject 
 * 
 * Entwickler: Jan Schwenger
 */
public class ColumnObj {
	
	private String  type, name;
	private Object data;
	private int columnsSize;
	
	public ColumnObj(String name, String type, int columnSize) {
		setName(name);
		setType(type);
		setColumnsSize(columnSize);
	}
	
	public ColumnObj(String name, String type, int columnSize, Object data) {
		setName(name);
		setType(type);
		setColumnsSize(columnSize);
		setData(data);
	}
	
	


	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getColumnsSize() {
		return columnsSize;
	}


	public void setColumnsSize(int columnsSize) {
		this.columnsSize = columnsSize;
	}
	

}
