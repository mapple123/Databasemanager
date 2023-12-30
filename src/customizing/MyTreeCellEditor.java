package customizing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventObject;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import enums.NodeType;
import functionality.Methods;
import gui_main.Main_Frame;
import lang_res.Consts;
import objects.AddJButton;

/**
 * Klasse fuer das customized Verhalten/Aussehen für die JTree-Komponente
 * 
 * Entwickler: Jan Schwenger
 */
public class MyTreeCellEditor extends DefaultTreeCellEditor {
	private JTextField textFieldInput;
	private JPanel view;
	private JButton btn;
	private AddJButton item;
	
	private ResourceBundle bundle;

	//TODO:Delete
	//private Main_Frame frame;

	
	//Konstruktor der Klasse
	public MyTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer, Main_Frame frame) {
		super(tree, renderer);
		//TODO:Delete
		//this.frame = frame;
		bundle = frame.getBundle();
		view = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		Border lowered_bevelborder = BorderFactory.createLoweredBevelBorder();
		view.setBorder(lowered_bevelborder);

		btn = new JButton(bundle.getString(Consts.BUTTON_OK));
		textFieldInput = new JTextField();
		btn.setPreferredSize(new Dimension(50, 20));
		btn.setFont(new Font("Arial", Font.PLAIN, 10));
		textFieldInput.setMaximumSize(new Dimension(90, 20));
		textFieldInput.setPreferredSize(new Dimension(90, 20));
		textFieldInput.setBorder(BorderFactory.createEmptyBorder());

		// Actionlistener, der fuer das Erstellen von neuen Datenbanken beziehungsweise für Tabellen zustaendig ist
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!textFieldInput.getText().trim().isEmpty()) {
					if (item.getNodeType() == NodeType.DATABASE) {
						//TODO:Remove line
						//System.out.println("Neue Datenbank");
						try {
							Methods.createNewDB(textFieldInput.getText().toString().trim());
							textFieldInput.setText("");
							frame.getWestPanel().reloadJTree();
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					} else if (item.getNodeType() == NodeType.TABLE) {
						//TODO: Make the functionality for creating a new Table
						//System.out.println("Neue Tabelle");
						/*
						 * AddJButton item =
						 * ((AddJButton)(((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).
						 * getUserObject())); System.out.println(item.getDbName());
						 * frame.getNorthPanel().setPathInfo(item.getDbName());
						 * frame.getWestPanel().reloadJTree();
						 */
					}

				} else
					JOptionPane.showMessageDialog(null, bundle.getString(Consts.EMPTY_TEXT_FIELD_ERROR), bundle.getString(Consts.ERROR_TITEL),
							JOptionPane.ERROR_MESSAGE);

			}
		});

		view.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
			}

			public void mouseMoved(MouseEvent e) {
		
			}

		});
		view.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
				
			}

			public void mouseExited(MouseEvent e) {
			}
		});

		view.add(textFieldInput);
		view.add(btn);
	}

	
	
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
			boolean leaf, int row) {
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		//Setzt den Tooltip fuer Datenbank und Tabelle
		if (node.getUserObject() instanceof JButton)
			item = (AddJButton) node.getUserObject();
		if (item.getNodeType() == NodeType.DATABASE) {
			textFieldInput.setToolTipText(bundle.getString(Consts.TOOL_TIP_CREATE_DB));
		} else
			textFieldInput.setToolTipText(bundle.getString(Consts.TOOL_TIP_CREATE_TABLE));

		return view;
	}

	//Methode die schaut, ob eine Zelle bearbeitbar ist
	@Override
	public boolean isCellEditable(EventObject event) {
		if (event instanceof MouseEvent) {

			MouseEvent mouseEvent = (MouseEvent) event;
			TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());

			int row = tree.getRowForLocation(mouseEvent.getX(), mouseEvent.getY());
			System.out.println(tree.getPathForRow(row));
			if (path != null) {

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (node.getUserObject() instanceof JButton) {
					return true;
				}

			}
		}
		return false;
	}
}
