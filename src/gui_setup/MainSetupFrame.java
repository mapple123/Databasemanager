package gui_setup;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import functionality.Methods;
import lang_res.Consts;

/**
 * Klasse fuer das Setup-Fenster inklusive Verhalten und Aussehen
 * 
 * Entwickler: Jan Schwenger
 */
public class MainSetupFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField tfPort, tfDb, tfUser;
	private JPasswordField tfPassword;

	private LinkedHashMap<String, JTextField> fields = new LinkedHashMap<>();
	private JButton btnConfirm, btnCheck;

	private Container mainContainer;

	//private ResourceBundle bundle;

	public MainSetupFrame(ResourceBundle bundle) {

		//this.bundle = bundle;
		this.setTitle(bundle.getString(Consts.SETUP_TITEL));
		// super("Setup");

		mainContainer = this.getContentPane();
		JPanel center = new JPanel(new GridBagLayout());
		JPanel bottomPanel = new JPanel(new BorderLayout());

		tfPort = new JTextField();
		tfDb = new JTextField();
		tfUser = new JTextField();
		tfPassword = new JPasswordField();

		fields.put(Consts.DB, tfDb);
		fields.put(Consts.PORT, tfPort);
		fields.put(Consts.USER, tfUser);
		fields.put(Consts.PASSWORD, tfPassword);
		int iy = 0;

		for (Map.Entry<String, JTextField> entry : fields.entrySet()) {
			String key = entry.getKey();
			JTextField value = entry.getValue();

			value.setPreferredSize(new Dimension(200, 20));

			JLabel lbl = new JLabel(key);

			GridBagConstraints c = new GridBagConstraints();

			c.insets = new Insets(10, 10, 10, 10);
			c.anchor = GridBagConstraints.WEST;

			c.gridx = 0;
			c.gridy = iy;
			center.add(lbl, c);

			c.gridx = 1;
			c.gridy = iy;
			center.add(value, c);
			iy++;
		}

		btnConfirm = new JButton(bundle.getString(Consts.BUTTON_SAVE_START));
		btnCheck = new JButton(bundle.getString(Consts.BUTTON_TEST_CON));

		btnCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Methods.testConn(tfDb.getText(), tfPort.getText(), tfUser.getText(), new String(tfPassword.getPassword()));
					JOptionPane.showMessageDialog(null, bundle.getString(Consts.SUCCESSFULL_CONNECTED_MESSAGE), bundle.getString(Consts.ALERT_TITEL),
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage(), bundle.getString(Consts.ERROR_TITEL), JOptionPane.ERROR_MESSAGE);
				}
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
