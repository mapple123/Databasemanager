package objects;

import javax.swing.JButton;

import enums.NodeType;

public class AddJButton extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private NodeType nodeType;
	private String dbName;
	
	public AddJButton(NodeType nodeType) {
		super();
		this.setNodeType(nodeType);
	}
	
	public AddJButton(NodeType nodeType, String dbName) {
		super();
		this.setNodeType(nodeType);
		this.setDbName(dbName);
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

}
