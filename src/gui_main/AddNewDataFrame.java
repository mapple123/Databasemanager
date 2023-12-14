package gui_main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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
import objects.ColumnObj;

public class AddNewDataFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel mainPanel; // Change this to JPanel
    private ArrayList<ColumnObj> allColumns;
    private String[] columnNames;
    private JTextField[] tfValues;
    private String db, table;
    private JLabel lblColumn, lblType, lblValue;

    private ArrayList<JPanel> panelHolder = new ArrayList<JPanel>();
    private JTable tableView;
    
    private JButton btnSave;
    private Main_Frame frame;

    public AddNewDataFrame(Main_Frame frame) {
        super("Neuer Datensatz");
        this.frame = frame;
        this.db = frame.getNorthPanel().getLabelDBPathInfo().getText();
        this.table = frame.getNorthPanel().getLabelDBPathInfo2().getText();
        
        initComponents();
        setComponents();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setSize(new Dimension(800, 650));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel(); // Create a JPanel for GroupLayout
        mainPanel.setLayout(new BorderLayout());
         // Add the JPanel to the content pane
        lblColumn = new JLabel("Spalte");
        lblType = new JLabel("Typ");
        lblValue = new JLabel("Wert");
        
        btnSave = new JButton("Speichern");
        try {
            allColumns = Methods.getAllColumnsFromSelectedTable(db, table);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        columnNames = new String[allColumns.size()];
        tfValues = new JTextField[allColumns.size()];
        
        JPanel panelRow = new JPanel();
        //JPanel rowHolder = new JPanel();
        
        //rowHolder.setLayout(new GridLayout(2,1));
        Dimension buttonSize = new Dimension(80, 40);
        for(int r = 0; r < allColumns.size(); r++) {
        	
        	
        	
        	
        	ColumnObj column = allColumns.get(r);
        	
        	columnNames[r] = column.getName();
        	
        	panelRow.setLayout(new GridLayout(column.getColumnsSize(),column.getColumnsSize() ));

        	//Object[][] data = column.getData();
        	
        		
        			
        			//System.out.print(data[r][c]);
        			JLabel lblColumnName = new JLabel(column.getName());
                	JLabel lblColumnType = new JLabel(column.getType());
                	
                	
            		JTextField tf = new JTextField();
            		tfValues[r] = tf;
                    

                    // Add buttons to the panel
            		
            		
            		
            		
            		lblColumnName.setPreferredSize(buttonSize);
            		lblColumnType.setPreferredSize(buttonSize);
           		 
            		tf.setPreferredSize(buttonSize);
            		
//            		JPanel p4 = new JPanel();
//            		JPanel p5 = new JPanel();
//            		JPanel p6 = new JPanel();
            		
//            		p4.add(lblColumnName);
//            		p5.add(lblColumnType);
//            		p6.add(tf);
            		
                    panelRow.add(lblColumnName);
                    panelRow.add(lblColumnType);
                    panelRow.add(tf);
                    
                    lblColumnName.setBorder(BorderFactory.createLineBorder(Color.black));
                    lblColumnType.setBorder(BorderFactory.createLineBorder(Color.black));
                    tf.setBorder(BorderFactory.createLineBorder(Color.black));

                    mainPanel.add(panelRow, BorderLayout.CENTER);
                    
        		
        		//System.out.println(1);
        		
        		  // Add the panel to the frame
               // mainPanel.add(scrollView, BorderLayout.CENTER);
        	
        }
        
        
    		JPanel panelRowHeader = new JPanel();
//    		JPanel p1 = new JPanel();
//    		JPanel p2 = new JPanel();
//    		JPanel p3 = new JPanel();
    		panelRowHeader.setLayout(new GridLayout(1,allColumns.size() ));
    		 
    		 lblColumn.setPreferredSize(buttonSize);
    		 
    		 lblType.setPreferredSize(buttonSize);
    		 
    		 lblValue.setPreferredSize(buttonSize);
    		 
//    		 p1.add(lblColumn);
//    		 p2.add(lblType);
//    		 p1.add(lblValue);
    		 panelRowHeader.add(lblColumn);
    		 panelRowHeader.add(lblType);
    		 panelRowHeader.add(lblValue);

    		

    		
    		//panelRow.add(panelRowHeader);
    	
        
//        for(JPanel row : panelHolder) {
//        	System.out.println(1);
//        	rowHolder.add(row);
//        }
        
    	getContentPane().add(panelRowHeader, BorderLayout.NORTH);
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Use FlowLayout to right-align the button
        
        northPanel.add(btnSave);
        
        getContentPane().add(northPanel, BorderLayout.AFTER_LAST_LINE);
        
        JScrollPane scrollView = new JScrollPane(mainPanel);
        scrollView.getVerticalScrollBar().setUnitIncrement(16);
      

        getContentPane().add(scrollView);
        /*GroupLayout layout = new GroupLayout(mainPanel); // Apply GroupLayout to the JPanel
        mainPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);*/
        
        /*for(JPanel row : panelHolder) {
        	layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
	                layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblColumn).addComponent(lblType).addComponent(lblValue)

        	        ).addComponent(row));
        	        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
        	                layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblColumn).addComponent(lblType).addComponent(lblValue)).addComponent(row)

        	        );
        }*/
      
       /* for (JPanel row : panelHolder) {
          
            row.setLayout(new GridLayout(1, 1)); // 1 row, 2 columns
            
            mainPanel.add(row);
           
        }*/

    
       /* layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(lblColumn).addComponent(lblType).addComponent(lblValue)

        );
        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblColumn).addComponent(lblType).addComponent(lblValue))

        );*/

        // tableView = new JTable(null, getComponentListeners())
    }

    private void setComponents() {
        // Add your components (e.g., JTable) to mainPanel here
    	
    	btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] values = new String[tfValues.length];
				for(int i = 0; i < tfValues.length; i++) {
					String s = tfValues[i].getText().trim();
					values[i] = s;
					if(s.equals(""))
						values[i] = null;
					
				}
				try {
					Methods.insertNewData(db, table, columnNames, values);
					
					AddNewDataFrame.this.dispose();
					frame.refreshTable();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					//System.exit(ERROR);
				}
				
			}
		});
    }
}
