package gui_main;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import customizing.MyRenderer;
import customizing.MyTreeCellEditor;
import enums.NodeType;
import functionality.Methods;
import objects.AddJButton;
import objects.DbListItemDB;

//TODO: Working on it
/**
 * Klasse fuer das West-Main-Panel inklusive Verhalten und Aussehen
 * 
 * Entwickler: Jan Schwenger
 */
public class West_Main_Panel extends JPanel implements TreeSelectionListener {

	private static final long serialVersionUID = 1L;

	private Main_Frame frame;

	private JTree tree;

	private JScrollPane treeView;

	private ArrayList<String> dbNames;

	protected String dbName, tableName;

	public West_Main_Panel(Main_Frame frame) {
		this.frame = frame;

		initComponents();

		setComponents();

		resizePanel();

	}

	private void initComponents() {
		try {
			dbNames = Methods.getAllDBNames();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Datenbanken");
		createNodes(top, dbNames);
		tree = new JTree(top);

		tree.setToggleClickCount(1);
		tree.setEditable(true);

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		tree.setCellEditor(new MyTreeCellEditor(tree, (DefaultTreeCellRenderer) tree.getCellRenderer(), frame));

		// TODO:to edit
//		  tree.addMouseListener(new MouseAdapter() {
//	            @Override
//	            public void mouseClicked(MouseEvent e) {
//	            	Object node = (((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()))!=null? ((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getUserObject():null;
//	            	if(node != null && node instanceof DbListItemDB) {
//	            		String dbName = ((DbListItemDB) node).getDbName();
//	            		String tableName = ((DbListItemDB) node).getTableName();
//	            		
//	            		 if (dbName != null && tableName != null && SwingUtilities.isRightMouseButton(e)) {
//	 	                    int row = tree.getClosestRowForLocation(e.getX(), e.getY());
//	 	                    tree.setSelectionRow(row);
//
//	 	                    JPopupMenu popupMenu = createPopupMenu(tree.getSelectionPath());
//	 	                    popupMenu.show(tree, e.getX(), e.getY());
//	 	                }
//	            	}
//	               
//	            }
//	        });

		ImageIcon iconDb = Methods.loadImage("datenbank.png", 15, 15);

		ImageIcon iconTable = Methods.loadImage("tabelle.png", 15, 15);
		if (iconDb != null && iconTable != null) {
			tree.setCellRenderer(new MyRenderer(iconDb, iconTable));
		}

		tree.addTreeSelectionListener(this);
		treeView = new JScrollPane(tree);
		treeView.getHorizontalScrollBar().setUnitIncrement(40);

	}

	/*
	 * private static JPopupMenu createPopupMenu(TreePath selectedPath) { JPopupMenu
	 * popupMenu = new JPopupMenu(); JMenuItem menuItem1 = new
	 * JMenuItem("Menu Item 1"); JMenuItem menuItem2 = new JMenuItem("Menu Item 2");
	 * 
	 * menuItem1.addActionListener(new ActionListener() {
	 * 
	 * @Override public void actionPerformed(ActionEvent e) { // Check if the
	 * selected path is the right one if (selectedPath != null &&
	 * selectedPath.getLastPathComponent() != null) { String selectedNode =
	 * selectedPath.getLastPathComponent().toString();
	 * 
	 * // Perform an action for Menu Item 1 for Node 1
	 * JOptionPane.showMessageDialog(null, selectedPath.toString());
	 * 
	 * // Perform an action for Menu Item 1 for Node 2
	 * //JOptionPane.showMessageDialog(null, "Menu Item 1 selected for Node 2.");
	 * 
	 * } } });
	 * 
	 * menuItem2.addActionListener(new ActionListener() {
	 * 
	 * @Override public void actionPerformed(ActionEvent e) { // Check if the
	 * selected path is the right one if (selectedPath != null &&
	 * selectedPath.getLastPathComponent() != null) { String selectedNode =
	 * selectedPath.getLastPathComponent().toString();
	 * 
	 * // Perform an action for Menu Item 2 for Node 1
	 * JOptionPane.showMessageDialog(null, selectedNode);
	 * 
	 * // Perform an action for Menu Item 2 for Node 2 //
	 * JOptionPane.showMessageDialog(null, "Menu Item 2 selected for Node 2.");
	 * 
	 * } } });
	 * 
	 * popupMenu.add(menuItem1); popupMenu.add(menuItem2);
	 * 
	 * return popupMenu; }
	 */

	private void setComponents() {

		JButton btn = new JButton();
		btn.setBorderPainted(false);
		btn.setBorder(null);
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setContentAreaFilled(false);
		btn.setIcon(Methods.loadImage("close.png", 30, 30));

		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				toggleVisibility(isVisible());
				frame.getWestSplitPane().setDividerSize(0);
			}
		});
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(treeView).addComponent(btn)

		);
		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(treeView).addComponent(btn))

		);

		// add(treeView);

		// add(btn);
	}

	protected void resizePanel() {

		// setPreferredSize(new Dimension((int) (frame.getWidth() - frame.getWidth() /
		// 1.15), 0));
		setPreferredSize(new Dimension((int) (frame.getWidth() - frame.getWidth() / 1.5),
				(int) (frame.getHeight() - frame.getHeight() / 2.5)));
		/*
		 * tree.setPreferredSize(new Dimension((int) (getPreferredSize().getWidth() -
		 * getPreferredSize().getWidth() / 1.5), (int) (frame.getHeight() -
		 * frame.getHeight() / 2.5))); treeView.setPreferredSize(new Dimension((int)
		 * (getPreferredSize().getWidth() - getPreferredSize().getWidth() / 1.5), (int)
		 * (frame.getHeight() - frame.getHeight() / 2.5)));
		 */
	}

	protected void toggleVisibility(boolean visible) {
		setVisible(!visible);
		Center_Main_Panel.btnShowHideWestPanel.setVisible(visible);
	}

	protected void createNodes(DefaultMutableTreeNode top, ArrayList<String> dbNames) {
		DefaultMutableTreeNode dbName = null;
		DefaultMutableTreeNode tableName = null;

		top.add(new DefaultMutableTreeNode(new AddJButton(NodeType.DATABASE)));
		for (String name : dbNames) {
			dbName = new DefaultMutableTreeNode(new DbListItemDB(name));
			top.add(dbName);
			ArrayList<String> tables = null;
			try {
				tables = Methods.getAllTables(name);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (tables != null) {
				dbName.add(new DefaultMutableTreeNode(new AddJButton(NodeType.TABLE, name)));
				for (String table : tables) {
					tableName = new DefaultMutableTreeNode(new DbListItemDB(name, table));
					dbName.add(tableName);
				}
			}

		}
	}

	boolean treeSelectionListenerEnabled = true;

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		if (node == null)
			return;

		Object nodeInfo = node.getUserObject();

		if (node.isRoot()) {
			frame.getNorthPanel().setPathInfo("");
			frame.getNorthPanel().setIconDB();
			frame.getNorthPanel().setPathInfo2("");
			frame.getNorthPanel().setIconTable();
			Center_Main_Panel.btnShowHideEastPanel.setVisible(false);
			frame.getEastSplitPane().setDividerSize(0);
			frame.getCenterPanel().getHolder().setVisible(false);
			if (frame.getEastPanel().isVisible())
				frame.getEastPanel().setVisible(false);
			try {
				frame.getCenterPanel().getTable().setModel(frame.getCenterPanel().loadData(null, null));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (nodeInfo instanceof DbListItemDB) {
			DbListItemDB db = (DbListItemDB) nodeInfo;
			dbName = db.getDbName();
			tableName = db.getTableName();
			if (tableName == null) {
				frame.getNorthPanel().setPathInfo(dbName);
				frame.getNorthPanel().setIconDB();
				frame.getNorthPanel().setPathInfo2("");
				frame.getNorthPanel().setIconTable();
				Center_Main_Panel.btnShowHideEastPanel.setVisible(false);
				frame.getEastSplitPane().setDividerSize(0);
				if (frame.getEastPanel().isVisible())
					frame.getEastPanel().setVisible(false);
				frame.getCenterPanel().getHolder().setVisible(false);
				try {
					frame.getCenterPanel().getTable().setModel(frame.getCenterPanel().loadData(dbName, null));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if (node.isLeaf()) {
				/*
				 * DbListItemDB db = (DbListItemDB) nodeInfo; dbName = db.getDbName(); tableName
				 * = db.getTableName();
				 */
				frame.getNorthPanel().setPathInfo(dbName);
				frame.getNorthPanel().setIconDB();
				frame.getNorthPanel().setPathInfo2(tableName);
				frame.getNorthPanel().setIconTable();
				frame.getEastSplitPane().setDividerSize(0);
				Center_Main_Panel.btnShowHideEastPanel.setVisible(true);
				frame.getCenterPanel().getHolder().setVisible(true);
				if (frame.getEastPanel().isVisible())
					frame.getEastPanel().setVisible(false);
				try {
					frame.getCenterPanel().getTable().setModel(frame.getCenterPanel().loadData(dbName, tableName));

					TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(
							frame.getCenterPanel().getTable().getModel());

					frame.getCenterPanel().setRowSorter(rowSorter);

					frame.getCenterPanel().getTable().setRowSorter(rowSorter);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			return;
		}

	}

	public void reloadJTree() {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren();
		model.reload();
		try {
			createNodes((DefaultMutableTreeNode) tree.getModel().getRoot(), Methods.getAllDBNames());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		model.reload(root);
	}

}
