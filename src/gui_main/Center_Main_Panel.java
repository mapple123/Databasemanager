package gui_main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import functionality.Methods;

public class Center_Main_Panel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable dbTable;

	private JScrollPane scrollPane;

	private Main_Frame frame;

	protected static JButton btnShowHideWestPanel, btnShowHideEastPanel;

	private TableRowSorter<TableModel> rowSorter;

	public void setRowSorter(TableRowSorter<TableModel> rowSorter) {
		this.rowSorter = rowSorter;
	}

	private JTextField jtfFilter = new JTextField();
	private JButton jbtFilter = new JButton("Filter");

	private JPanel holder;

	public JPanel getHolder() {
		return holder;
	}

	public void setHolder(JPanel holder) {
		this.holder = holder;
	}

	public Center_Main_Panel(Main_Frame frame) {
		this.frame = frame;

		initComponents();
		setComponents();
	}

	private void initComponents() {

		holder = new JPanel();
		holder.setLayout(new BorderLayout());
		holder.setVisible(false);
		try {
			dbTable = new JTable(loadData(null, null)) {
				/**
				 * 
				 */

				// TODO: Look After resizing
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return column == this.getColumnCount() - 1 ? false : true;
				}

				DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();

				{ // initializer block
					renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
				}

				@Override
				public TableCellRenderer getCellRenderer(int arg0, int arg1) {
					return renderLeft;
				}

				private boolean inLayout;

				@Override
				public boolean getScrollableTracksViewportWidth() {
					return hasExcessWidth();

				}

				@Override
				public void doLayout() {
					if (hasExcessWidth()) {
						// fool super
						autoResizeMode = AUTO_RESIZE_SUBSEQUENT_COLUMNS;
					}
					inLayout = true;
					super.doLayout();
					inLayout = false;
					autoResizeMode = AUTO_RESIZE_OFF;
				}

				protected boolean hasExcessWidth() {
					return getPreferredSize().width < getParent().getWidth();
				}

				@Override
				public void columnMarginChanged(ChangeEvent e) {
					if (isEditing()) {
						// JW: darn - cleanup to terminate editing ...
						removeEditor();
					}
					TableColumn resizingColumn = getTableHeader().getResizingColumn();
					// Need to do this here, before the parent's
					// layout manager calls getPreferredSize().
					if (resizingColumn != null && autoResizeMode == AUTO_RESIZE_OFF && !inLayout) {

						int width = 100;
						width = Math.max(width, resizingColumn.getPreferredWidth());
						resizingColumn.setMinWidth(100);
						resizingColumn.setPreferredWidth(width);
						resizingColumn.setMaxWidth(width);
						resizingColumn.setResizable(false);

					}
					resizeAndRepaint();
				}

				/*
				 * @Override public void doLayout() {
				 * 
				 * 
				 * 
				 * 
				 * int width = getWidth(); int columnCount = getColumnCount(); if(columnCount !=
				 * 0) {
				 * 
				 * int columnSize = width / columnCount; for (int index = 0; index <
				 * columnCount; index++) { TableColumn column =
				 * getColumnModel().getColumn(index); column.setResizable(true);
				 * column.setPreferredWidth(width); column.setMinWidth(100); } }
				 * 
				 * 
				 * super.doLayout();
				 * 
				 * }
				 */

			};
			// dbTable.setModel(loadData(null, null));

			JPanel panel = new JPanel(new BorderLayout());
			ImageIcon icon = Methods.loadImage("lupe.png", 25, 25);
			JLabel label = new JLabel(icon);
			panel.add(label, BorderLayout.WEST);
			panel.add(jtfFilter, BorderLayout.CENTER);

			// setLayout(new BorderLayout());
			holder.add(panel, BorderLayout.NORTH);
			// add(new JScrollPane(jTable), BorderLayout.CENTER);

			jtfFilter.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void insertUpdate(DocumentEvent e) {
					String text = jtfFilter.getText();

					if (text.trim().length() == 0) {
						rowSorter.setRowFilter(null);
					} else {
						rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
						System.out.println(text);
					}
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					String text = jtfFilter.getText();

					if (text.trim().length() == 0) {
						rowSorter.setRowFilter(null);
					} else {
						rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
					}
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
																					// methods, choose Tools |
																					// Templates.
				}

			});

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(ERROR);
		}

		scrollPane = new JScrollPane(dbTable);
		dbTable.setFillsViewportHeight(true);
		dbTable.getTableHeader().setReorderingAllowed(false);
		dbTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane.setMaximumSize(new Dimension(frame.getMaximumSize()));

		btnShowHideWestPanel = new JButton();
		setIcon(btnShowHideWestPanel);
		btnShowHideWestPanel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnShowHideWestPanel.isVisible()) {
					frame.getWestPanel().setVisible(true);
					frame.getWestSplitPane().setDividerLocation(290);
					frame.getWestSplitPane().setDividerSize(10);
					btnShowHideWestPanel.setVisible(false);
				} else {
					frame.getWestPanel().setVisible(false);
					btnShowHideWestPanel.setVisible(true);
				}

			}
		});
		btnShowHideWestPanel.setVisible(false);

		btnShowHideEastPanel = new JButton();
		btnShowHideEastPanel.setVisible(false);
		setIcon(btnShowHideEastPanel);
		btnShowHideEastPanel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnShowHideEastPanel.isVisible()) {
					frame.getEastPanel().setVisible(true);
					frame.getEastSplitPane().setDividerLocation(frame.getWidth() - 290);
					frame.getEastSplitPane().setDividerSize(10);
					btnShowHideEastPanel.setVisible(false);
				} else {
					frame.getEastPanel().setVisible(false);
					btnShowHideEastPanel.setVisible(true);
				}

			}
		});
		btnShowHideWestPanel.setVisible(false);
	}

	private void setComponents() {
		scrollPane.add(dbTable.getTableHeader());
		holder.add(scrollPane, BorderLayout.CENTER);

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(btnShowHideWestPanel).addComponent(holder)
				.addComponent(btnShowHideEastPanel)

		);
		layout.setVerticalGroup(
				layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(btnShowHideWestPanel).addComponent(holder).addComponent(btnShowHideEastPanel))

		);

		// add(scrollPane);
		// add(btnShowHideWestPanel);
	}

	private Object[][] getAllData(String db, String table) throws Exception {
		Object[][] results = null;
		Connection connection;

		if (db == null)
			connection = Methods.connectionToDbFirst();
		else {
			if (table != null) {
				connection = Methods.connectionToDb(db);

				Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				// Query
				ResultSet resultSet = statement.executeQuery("SELECT * FROM `" + table + "`");

				ResultSetMetaData rsmd = resultSet.getMetaData();

				// Get number of columns returned
				int numOfData = rsmd.getColumnCount();

				int size = 0;
				if (resultSet != null) {
					resultSet.beforeFirst();
					resultSet.last();
					size = resultSet.getRow();
					resultSet.beforeFirst();
				}

				results = new Object[size][numOfData];

				while (resultSet.next()) {
					for (int i = 1; i <= numOfData; i++) {
						results[resultSet.getRow() - 1][i - 1] = resultSet.getObject(i);
					}
				}

				// Close DB connection
				statement.close();
				connection.close();

			}
		}
		return results;
	}

	private String[] getColumnnames(String db, String table) {
		ResultSet rs;
		Connection conn = null;
		String[] columns = null;
		try {
			if (db == null)
				conn = Methods.connectionToDbFirst();
			else {
				if (table != null) {
					conn = Methods.connectionToDb(db);

					rs = conn.createStatement().executeQuery("SELECT * FROM `" + table + "` LIMIT 1");
					ResultSetMetaData rsmd = rs.getMetaData();
					columns = new String[rsmd.getColumnCount() + 1];
					for (int i = 0; i < columns.length - 1; i++) {
						columns[i] = rsmd.getColumnName(i + 1);
					}
					columns[columns.length - 1] = "Operationen";

				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return columns;

	}

	protected JTable getTable() {
		return dbTable;
	}

	protected TableModel loadData(String db, String table) throws Exception {
		return new DefaultTableModel(getAllData(db, table), getColumnnames(db, table)) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void setValueAt(Object value, int row, int col) {

				Object[] o = null;

				ArrayList<String> primaryColumns = null;
				try {
					primaryColumns = Methods.getPrimaryKeyColumnsForTable(Methods.connectionToDb(db), table);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (primaryColumns != null) {
					o = new Object[primaryColumns.size()];
					for (int i = 0; i < primaryColumns.size(); i++) {
						o[i] = this.getValueAt(row, this.findColumn(primaryColumns.get(i)));
					}
					try {
						Methods.updateItem(db, table, value.toString(), this.getColumnName(col), primaryColumns, o);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else
					System.out.println("Kein PrimaryKey!");

				try {
					/*
					 * Methods.updateItem(db, table, value.toString(), this.getColumnName(col),
					 * Integer.parseInt("" + this.getValueAt(row, 0)));
					 */
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					dbTable.setModel(loadData(db, table));

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				;
				fireTableCellUpdated(row, col);

			}

			@Override
			public Class<?> getColumnClass(int column) {
				for (int row = 0; row < getRowCount(); row++) {
					Object o = getValueAt(row, column);

					if (o != null) {
						return o.getClass();
					}
				}

				return Object.class;
			}
		};
	}

	protected void resizeTable() {
		scrollPane.setPreferredSize(new Dimension(frame.getWidth() - frame.getWidth() / 3,
				(int) (frame.getHeight() - frame.getHeight() / 2.5)));

	}

	private void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 15; // Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);

			}
			// width = Math.max(width,
			// table.getColumnModel().getColumn(column).getPreferredWidth());
			if (width > 300)
				width = 300;

			columnModel.getColumn(column).setPreferredWidth(width);
		}

	}

	private void setIcon(JButton btn) {
		btn.setBorderPainted(false);
		btn.setBorder(null);
		// button.setFocusable(false);
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setContentAreaFilled(false);
		btn.setIcon(Methods.loadImage("menu.png", 30, 30));
	}

}
