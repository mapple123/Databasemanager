package gui_setup;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import functionality.Methods;
import mainpackage.ALL_CONST;

public class MainSetupFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField tfPort, tfDb, tfUser;
	private JPasswordField tfPassword;

	private LinkedHashMap<String, JTextField> fields = new LinkedHashMap<>();
	private JButton btnConfirm, btnCheck;
	
	private Container mainContainer;
	
	public MainSetupFrame() {
		super("Setup");
		
		mainContainer = this.getContentPane();
		JPanel center = new JPanel(new GridBagLayout());
		 JPanel bottomPanel = new JPanel(new BorderLayout());
		 
		
		tfPort = new JTextField();
		tfDb = new JTextField();
		tfUser = new JTextField();
		tfPassword = new JPasswordField();
		
		
		fields.put(ALL_CONST.DB, tfDb);
		fields.put(ALL_CONST.PORT, tfPort);
		fields.put(ALL_CONST.USER, tfUser);
		fields.put(ALL_CONST.PASSWORD, tfPassword);
		int iy = 0;
	
		for (Map.Entry<String, JTextField> entry : fields.entrySet()) {
		    String key = entry.getKey();
		    JTextField value = entry.getValue();
		    
		    value.setPreferredSize(new Dimension(200,20));
		    
		    JLabel lbl = new JLabel(key);
		    
			GridBagConstraints c = new GridBagConstraints();
			
			c.insets = new Insets(10, 10, 10, 10);
			 c.anchor=GridBagConstraints.WEST;
		    
		    c.gridx = 0;//set the x location of the grid for the next component
	        c.gridy = iy;//set the y location of the grid for the next component
	        center.add(lbl,c);

		
		
	        c.gridx = 1;//set the x location of the grid for the next component
	        c.gridy = iy;//set the y location of the grid for the next component
	        center.add(value,c);

	      
			
			iy++;
		}
		
		btnConfirm = new JButton("Speichern & Starten");
		btnCheck = new JButton("Teste Verbindung");
		
		btnCheck.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Methods.testConn(tfDb.getText(), tfPort.getText(), tfUser.getText(), tfPassword.getText());
					JOptionPane.showMessageDialog(null, "Erfolgreich verbunden", "Message", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				//
			}
		});
		
		
		mainContainer.setLayout(new BorderLayout());
		
		mainContainer.add(center, BorderLayout.CENTER);
		
		
	      bottomPanel.add(btnConfirm, BorderLayout.LINE_END);
	     bottomPanel.add(btnCheck, BorderLayout.LINE_START);

	    
	      mainContainer.add(bottomPanel, BorderLayout.PAGE_END);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setSize(new Dimension(800, 650));
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
