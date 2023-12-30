package gui_main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import functionality.Methods;
import lang_res.Consts;

/**
 * Klasse fuer das East-Main-Panel inklusive Verhalten und Aussehen
 * 
 * Entwickler: Jan Schwenger
 */
public class East_Main_Panel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Main_Frame frame;
	@SuppressWarnings("unused")
	private String db, table;

	private ResourceBundle bundle;

	public East_Main_Panel(Main_Frame frame) {
		this.frame = frame;
		bundle = frame.getBundle();
		initComponents();
		setComponents();
		resizePanel();

	}

	private void initComponents() {

	}

	private void setComponents() {

		JPanel view = new JPanel();
		JButton btn = new JButton();
		JButton btnInsertNewData = new JButton(bundle.getString(Consts.BUTTON_INSERT));
		JButton btnUpdateData = new JButton(bundle.getString(Consts.BUTTON_EDIT));
		JButton btnDeleteData = new JButton(bundle.getString(Consts.BUTTON_DELETE));

		setButtonSize(btnInsertNewData);
		setButtonSize(btnUpdateData);
		setButtonSize(btnDeleteData);
		setButtonIcon(btn);

		btnInsertNewData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new AddNewDataFrame(frame);
			}
		});

		btnUpdateData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (frame.getCenterPanel().getTable().getSelectedRow() != -1)
					new EditDataFrame(frame);
				else
					JOptionPane.showMessageDialog(null, bundle.getString(Consts.ERROR_NO_DATA),
							bundle.getString(Consts.ERROR_TITEL), JOptionPane.ERROR_MESSAGE);

			}
		});

		btnDeleteData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (frame.getCenterPanel().getTable().getSelectedRow() != -1) {
					String db = frame.getNorthPanel().getLabelDBPathInfo().getText();
					String table = frame.getNorthPanel().getLabelDBPathInfo2().getText();
					JTable jTable = frame.getCenterPanel().getTable();
					TableModel model = jTable.getModel();
					int size = model.getColumnCount() - 1;
					String[] columns = new String[size];
					Object[] values = new Object[size];
					int selectedRowIndex = jTable.getSelectedRow();
					if (selectedRowIndex != -1) {
						int modelRowIndex = jTable.convertRowIndexToModel(selectedRowIndex);
						Object[] rowData = new Object[model.getColumnCount() - 1];
						for (int i = 0; i < model.getColumnCount() - 1; i++) {
							rowData[i] = model.getValueAt(modelRowIndex, i);
						}
						values = rowData;
						// TODO: language support
						JOptionPane.showMessageDialog(null, "Selected Row Data: " + Arrays.toString(rowData));
					} else {
						// TODO: language support
						JOptionPane.showMessageDialog(null, "No row selected.");
					}

					for (int i = 0; i < size; i++) {
						columns[i] = model.getColumnName(i);
					}

					try {
						Methods.deleteOldData(db, table, columns, values);
						frame.refreshTable();
					} catch (Exception e2) {
						e2.printStackTrace();
					}

				} else
					JOptionPane.showMessageDialog(null, bundle.getString(Consts.ERROR_NO_DATA),
							bundle.getString(Consts.ERROR_TITEL), JOptionPane.ERROR_MESSAGE);

			}
		});

		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				toggleVisibility(isVisible());
				frame.getEastSplitPane().setDividerSize(0);
			}
		});

		view.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;

		c.gridy = 1;

		c.insets = new Insets(10, 10, 10, 10);
		view.add(btnInsertNewData, c);

		c.gridy = 2;
		c.insets = new Insets(0, 10, 10, 10);

		view.add(btnUpdateData, c);

		c.gridy = 3;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(0, 10, 0, 10);
		view.add(btnDeleteData, c);

		JScrollPane scrollView = new JScrollPane(view);
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(btn).addComponent(scrollView)

		);
		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btn).addComponent(scrollView))

		);

	}

	protected void resizePanel() {

		setPreferredSize(new Dimension((int) (frame.getWidth() - frame.getWidth() / 1.15),
				(int) (frame.getHeight() - frame.getHeight() / 2.5)));
	}

	protected void toggleVisibility(boolean visible) {
		setVisible(!visible);
		Center_Main_Panel.btnShowHideEastPanel.setVisible(visible);
	}

	private void setButtonIcon(JButton btn) {
		btn.setBorderPainted(false);
		btn.setBorder(null);
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setContentAreaFilled(false);
		btn.setIcon(Methods.loadImage("close.png", 30, 30));
	}

	private void setButtonSize(JButton btn) {
		btn.setPreferredSize(new Dimension(180, 25));
	}

}
