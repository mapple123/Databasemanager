package mainpackage;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class test extends JFrame {
	private JTable table;
	private JTextField searchField;
	private JButton searchButton;
	private DefaultTableModel model;
	private TableRowSorter<TableModel> rowSorter;

	public test() {
		setTitle("Table Search Example");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);

		// Create the table model and set it to the JTable
		model = new DefaultTableModel();
		table = new JTable(model);
		table.setAutoCreateRowSorter(true); // Enable row sorting
		rowSorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rowSorter);

		// Add some sample data to the table
		model.addColumn("Name");
		model.addColumn("Age");
		model.addRow(new Object[] { "Alice", 25 });
		model.addRow(new Object[] { "Bob", 30 });
		model.addRow(new Object[] { "Charlie", 35 });

		// Create a text field for searching
		searchField = new JTextField(20);

		// Create a search button
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String searchText = searchField.getText().toLowerCase(); // Convert search text to lowercase for
																			// case-insensitive search
				if (searchText.isEmpty()) {
					rowSorter.setRowFilter(null); // Reset the filter
				} else {
					// Create a row filter that matches the search text in any column
					RowFilter<TableModel, Integer> rowFilter = RowFilter.regexFilter("(?i)" + searchText); // Use regex
																											// for
																											// case-insensitive
																											// search
					rowSorter.setRowFilter(rowFilter);
				}
			}
		});

		// Create a panel to hold the search components
		JPanel searchPanel = new JPanel();
		searchPanel.add(new JLabel("Search: "));
		searchPanel.add(searchField);
		searchPanel.add(searchButton);

		// Add components to the frame
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(searchPanel, BorderLayout.NORTH);
		getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				test example = new test();
				example.setVisible(true);
			}
		});
	}
}
