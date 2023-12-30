package gui_main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import functionality.Methods;
import lang_res.Consts;
import objects.ColumnObj;

/**
 * Klasse fuer das Hinzufuegen eines neuen Datensatzes in eine Tabelle 
 * 
 * Entwickler: Jan Schwenger
 */

public class AddNewDataFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel mainPanel;
	private ArrayList<ColumnObj> allColumns;
	private String[] columnNames;
	private JTextField[] tfValues;
	private String db, table;
	private JLabel lblColumn, lblType, lblValue;

	@SuppressWarnings("unused")
	private ArrayList<JPanel> panelHolder = new ArrayList<JPanel>();
	@SuppressWarnings("unused")
	private JTable tableView;

	private JButton btnSave;
	private Main_Frame frame;

	private ResourceBundle bundle;

	public AddNewDataFrame(Main_Frame frame) {
		bundle = frame.getBundle();
		this.frame = frame;
		this.db = frame.getNorthPanel().getLabelDBPathInfo().getText();
		this.table = frame.getNorthPanel().getLabelDBPathInfo2().getText();
		this.setTitle(bundle.getString(Consts.NEW_DATA_FRAME_TITLE));

		initComponents();
		setComponents();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setSize(new Dimension(800, 650));
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initComponents() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		lblColumn = new JLabel(bundle.getString(Consts.COLUMN));
		lblType = new JLabel(bundle.getString(Consts.TYPE));
		lblValue = new JLabel(bundle.getString(Consts.VALUE));

		btnSave = new JButton(bundle.getString(Consts.BUTTON_SAVE));
		try {
			allColumns = Methods.getAllColumnsFromSelectedTable(db, table);
		} catch (Exception error) {
			error.printStackTrace();
		}

		columnNames = new String[allColumns.size()];
		tfValues = new JTextField[allColumns.size()];

		JPanel panelRow = new JPanel();
		Dimension buttonSize = new Dimension(80, 40);

		for (int r = 0; r < allColumns.size(); r++) {
			ColumnObj column = allColumns.get(r);

			columnNames[r] = column.getName();

			panelRow.setLayout(new GridLayout(column.getColumnsSize(), column.getColumnsSize()));

			JLabel lblColumnName = new JLabel(column.getName());
			JLabel lblColumnType = new JLabel(column.getType());

			JTextField tf = new JTextField();
			tfValues[r] = tf;

			lblColumnName.setPreferredSize(buttonSize);
			lblColumnType.setPreferredSize(buttonSize);

			tf.setPreferredSize(buttonSize);

			panelRow.add(lblColumnName);
			panelRow.add(lblColumnType);
			panelRow.add(tf);

			lblColumnName.setBorder(BorderFactory.createLineBorder(Color.black));
			lblColumnType.setBorder(BorderFactory.createLineBorder(Color.black));
			tf.setBorder(BorderFactory.createLineBorder(Color.black));

			mainPanel.add(panelRow, BorderLayout.CENTER);

		}

		JPanel panelRowHeader = new JPanel();

		panelRowHeader.setLayout(new GridLayout(1, allColumns.size()));

		lblColumn.setPreferredSize(buttonSize);

		lblType.setPreferredSize(buttonSize);

		lblValue.setPreferredSize(buttonSize);

		panelRowHeader.add(lblColumn);
		panelRowHeader.add(lblType);
		panelRowHeader.add(lblValue);

		getContentPane().add(panelRowHeader, BorderLayout.NORTH);
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Use FlowLayout to right-align the button

		northPanel.add(btnSave);

		getContentPane().add(northPanel, BorderLayout.AFTER_LAST_LINE);

		JScrollPane scrollView = new JScrollPane(mainPanel);
		scrollView.getVerticalScrollBar().setUnitIncrement(16);

		getContentPane().add(scrollView);
		
	}

	private void setComponents() {

		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] values = new String[tfValues.length];
				for (int i = 0; i < tfValues.length; i++) {
					String s = tfValues[i].getText().trim();
					values[i] = s;
					if (s.equals(""))
						values[i] = null;
				}
				try {
					Methods.insertNewData(db, table, columnNames, values);
					AddNewDataFrame.this.dispose();
					frame.refreshTable();
				} catch (Exception error) {
					error.printStackTrace();
					JOptionPane.showMessageDialog(null, error.getMessage(), bundle.getString(Consts.ERROR_TITEL),
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
	}
}
