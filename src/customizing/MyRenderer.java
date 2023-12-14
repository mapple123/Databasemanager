package customizing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import enums.NodeType;
import functionality.Methods;
import objects.AddJButton;
import objects.DbListItemDB;

public class MyRenderer implements TreeCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Icon iconDb, iconTable;

	public MyRenderer(Icon iconDb, Icon iconTable) {
		this.iconDb = iconDb;
		this.iconTable = iconTable;
	}
	
	/* @Override
	    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
	                                                  boolean leaf, int row, boolean hasFocus) {

	        JPanel panel = new JPanel(new BorderLayout());
	        JLabel label = new JLabel(value.toString());

	        panel.add(label, BorderLayout.CENTER);

	        if (leaf) {
	            JButton button = new JButton("+");
	            button.setToolTipText("Add Something");
	            
	            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
	            node.setUserObject(new DbListItemDB(value.toString(), button)); // Store the button with the node data

	            button.addActionListener(new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                	DbListItemDB nodeData = (DbListItemDB) node.getUserObject();
	                    JButton button = nodeData.getButton();
	                    System.out.println("Button Clicked in Row: " + row + ", Text: " + button.getText());
	                }
	            });

	            panel.add(button, BorderLayout.EAST);
	        }

	        return panel;
	    }
	    */

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {

		//super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		JPanel view = new JPanel();
		JLabel lbl = new JLabel();
		Border border = BorderFactory.createEmptyBorder ( 4, 4, 4, 4 );
		lbl.setBorder(border);
		view.setLayout(new BorderLayout());
		view.setOpaque(hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if (!(node.getUserObject() instanceof String) && !(node.getUserObject() instanceof JButton) ) {

			Object item = node.getUserObject();
			JButton button = createAddButton(NodeType.TABLE, ((DbListItemDB) item).getDbName());
			
			if (((DbListItemDB) item).getTableName() == null) {
				lbl.setIcon(iconDb);
				lbl.setText(((DbListItemDB) item).getDbName());
				
				if(node.getChildCount() == 0) {
					 
			           
			            //DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) value;
			            node.setUserObject(new DbListItemDB(((DbListItemDB) item).getDbName(),null,value.toString(), button)); // Store the button with the node data
System.out.println(value.toString());
			            button.addActionListener(new ActionListener() {
			                @Override
			                public void actionPerformed(ActionEvent e) {
			                	DbListItemDB nodeData = (DbListItemDB) node.getUserObject();
			                    JButton button = nodeData.getButton();
			                    System.out.println("Button Clicked in Row: " + row + ", Text: " + button.getText());
			                }
			            });
				
				//btnAdd.requestFocusInWindow();
				view.add(button, BorderLayout.WEST);
				}
			} else {
				
				lbl.setIcon(iconTable);
				lbl.setText(((DbListItemDB) item).getTableName());
				 //node.setUserObject(new DbListItemDB(((DbListItemDB) item).getDbName(),((DbListItemDB) item).getTableName(),value.toString(), button));
			}
			view.add(lbl, BorderLayout.CENTER);
		} else if(node.getUserObject() instanceof JButton) {
			view.add(createAddButton(NodeType.DATABASE,null));
		}
			
			else if(node.getUserObject() instanceof String){
			System.out.println("ROOT2");
			lbl.setIcon(Methods.loadImage("home.png", 15, 15));
			lbl.setText(value.toString());
			view.add(lbl, BorderLayout.CENTER);
			
		}
		
		//view.requestFocusInWindow();
		
	
		return view;
	}
	
	public static void fitText(JButton button, String text, int maxWidth, int maxHeight) {
        Font currentFont = button.getFont();
        FontMetrics fontMetrics = button.getFontMetrics(currentFont);
        int textWidth = fontMetrics.stringWidth(text);
        int textHeight = fontMetrics.getHeight();

        int fontSize = currentFont.getSize();
        while (textWidth > maxWidth || textHeight > maxHeight) {
            fontSize--;
            currentFont = currentFont.deriveFont((float) fontSize);
            fontMetrics = button.getFontMetrics(currentFont);
            textWidth = fontMetrics.stringWidth(text);
            textHeight = fontMetrics.getHeight();
        }

        button.setFont(currentFont);
    }
	
	private AddJButton createAddButton(NodeType nodeType, String dbName) {
		AddJButton button = new AddJButton(nodeType, dbName);
		button.setBorderPainted(false);
		button.setBorder(null);
		//button.setFocusable(false);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setContentAreaFilled(false);
		button.setIcon(Methods.loadImage("add.png", 15, 15));
		
		
		return button;
	}

	/*
	 * protected boolean isTutorialBook(Object value) { DefaultMutableTreeNode node
	 * = (DefaultMutableTreeNode) value; DbListItemDB nodeInfo = (DbListItemDB)
	 * (node.getUserObject()); String title = nodeInfo.bookName; if
	 * (title.indexOf("Tutorial") >= 0) { return true; }
	 * 
	 * return false; }
	 */
}
