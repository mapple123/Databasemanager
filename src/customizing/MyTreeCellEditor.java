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
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import enums.NodeType;
import functionality.Methods;
import gui_main.Main_Frame;
import objects.AddJButton;

public class MyTreeCellEditor extends DefaultTreeCellEditor {
	private JTextField tf;
	private JPanel view;
	private JButton btn;
	private AddJButton item;
	
	private Main_Frame frame;

	public MyTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer, Main_Frame frame) {
		super(tree, renderer);
		this.frame = frame;
		view = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		// my_button = new JButton("x");
		// JFormattedTextField my_textfield = new JFormattedTextField("Nr.");

		Border lowered_bevelborder = BorderFactory.createLoweredBevelBorder();
		view.setBorder(lowered_bevelborder);

		btn = new JButton("OK");
		tf = new JTextField();
		btn.setPreferredSize(new Dimension(50,20));
		btn.setFont(new Font("Arial", Font.PLAIN, 10));
		tf.setMaximumSize(new Dimension(90, 20));
		tf.setPreferredSize(new Dimension(90, 20));
		tf.setBorder(BorderFactory.createEmptyBorder());
		
		
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!tf.getText().trim().isEmpty()) {
					if (item.getNodeType() == NodeType.DATABASE) {
						System.out.println("Neue Datenbank");
						try {
							Methods.createNewDB(tf.getText().toString().trim());
							tf.setText("");
							frame.getWestPanel().reloadJTree();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
						}else if(item.getNodeType() == NodeType.TABLE) {
							System.out.println("Neue Tabelle");
							/*AddJButton item = ((AddJButton)(((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getUserObject()));
							System.out.println(item.getDbName());
							frame.getNorthPanel().setPathInfo(item.getDbName());
							frame.getWestPanel().reloadJTree();
							*/
						}
							
						
						
						
						
				}else
					JOptionPane.showMessageDialog(null, "Der Name darf nicht leer sein!", "Error", JOptionPane.ERROR_MESSAGE);
				
			}
		});

	view.addMouseMotionListener(new MouseMotionListener() {

		public void mouseDragged(MouseEvent e) {
			// System.out.println("Dragged.");
		}

		public void mouseMoved(MouseEvent e) {
			// System.out.println("Moving: Button: " + button.getBounds());
			// System.out.println("Moving: e point: " + e.getPoint());
			// System.out.println("---------------");
		}

	});
	view.addMouseListener(new MouseListener() {

		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			// System.out.println("B bounds: " + button.getBounds());
			// System.out.println("e point: " + e.getPoint());
			// System.out.println("---------------");
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			// System.out.println("Entered");
		}

		public void mouseExited(MouseEvent e) {
			// System.out.println("Exited");
		}
	});
	

		view.add(tf);
		view.add(btn);
	}

	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
			boolean leaf, int row) {
		// return renderer.getTreeCellRendererComponent(tree, value, true, expanded,
		// leaf, row, true);
		// button.setText(value.toString());
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		if (node.getUserObject() instanceof JButton)
			item = (AddJButton) node.getUserObject();
		if (item.getNodeType() == NodeType.DATABASE) {
			tf.setToolTipText("Erstelle eine neue Datenbank");
		} else
			tf.setToolTipText("Erstelle eine neue Tabelle");

		return view;
	}

	@Override
	public boolean isCellEditable(EventObject event) {
		if (event instanceof MouseEvent) {

			MouseEvent mouseEvent = (MouseEvent) event;
			TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());

			int c = tree.getRowForLocation(mouseEvent.getX(), mouseEvent.getY());
			System.out.println(tree.getPathForRow(c));
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
