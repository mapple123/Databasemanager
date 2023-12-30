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
/**
 * Klasse fuer das customized Verhalten für die JTree-Komponente
 * 
 * Entwickler: Jan Schwenger
 */

public class MyRenderer implements TreeCellRenderer {
	
	private Icon iconDb, iconTable;

	//Konstruktor mit den Icon Parametern
	public MyRenderer(Icon iconDb, Icon iconTable) {
		this.iconDb = iconDb;
		this.iconTable = iconTable;
	}
	
	//Unterscheidung und Generierung der verschiedenen Ansichten in der JTree-Komponente (Tabelle & DB)
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {

		JPanel view = new JPanel();
		JLabel label = new JLabel();
		Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);
		label.setBorder(border);
		view.setLayout(new BorderLayout());
		view.setOpaque(hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if (!(node.getUserObject() instanceof String) && !(node.getUserObject() instanceof JButton)) {

			Object item = node.getUserObject();
			JButton button = createAddButton(NodeType.TABLE, ((DbListItemDB) item).getDbName());

			if (((DbListItemDB) item).getTableName() == null) {
				label.setIcon(iconDb);
				label.setText(((DbListItemDB) item).getDbName());

				if (node.getChildCount() == 0) {
					node.setUserObject(
							new DbListItemDB(((DbListItemDB) item).getDbName(), null, value.toString(), button)); 
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							DbListItemDB nodeData = (DbListItemDB) node.getUserObject();
							@SuppressWarnings("unused")
							JButton button = nodeData.getButton();
						}
					});
					view.add(button, BorderLayout.WEST);
				}
			} else {
				label.setIcon(iconTable);
				label.setText(((DbListItemDB) item).getTableName());
			}
			view.add(label, BorderLayout.CENTER);
		} else if (node.getUserObject() instanceof JButton) {
			view.add(createAddButton(NodeType.DATABASE, null));
		}

		else if (node.getUserObject() instanceof String) {
			label.setIcon(Methods.loadImage("home.png", 15, 15));
			label.setText(value.toString());
			view.add(label, BorderLayout.CENTER);

		}
		return view;
	}

	//Methode, die für das Anpassen der Textgroesse innerhalb der Buttons zustaendig ist
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

	// Methode zum Erstellen der selbsterstellten JButtons
	private AddJButton createAddButton(NodeType nodeType, String dbName) {
		AddJButton button = new AddJButton(nodeType, dbName);
		button.setBorderPainted(false);
		button.setBorder(null);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setContentAreaFilled(false);
		button.setIcon(Methods.loadImage("add.png", 15, 15));

		return button;
	}
}
