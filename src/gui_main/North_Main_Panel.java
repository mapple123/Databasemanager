package gui_main;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import functionality.Methods;

public class North_Main_Panel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Main_Frame frame;
	private CustomMenu menu;
	
	private JLabel labelDBPathInfo, labelDBPathInfo2;

	public JLabel getLabelDBPathInfo2() {
		return labelDBPathInfo2;
	}

	public void setLabelDBPathInfo2(JLabel labelDBPathInfo2) {
		this.labelDBPathInfo2 = labelDBPathInfo2;
	}

	public JLabel getLabelDBPathInfo() {
		return labelDBPathInfo;
	}

	public void setLabelDBPathInfo(JLabel labelDBPathInfo) {
		this.labelDBPathInfo = labelDBPathInfo;
	}

	public North_Main_Panel(Main_Frame frame) {
		this.frame = frame;
		initComponents();
		setComponents();
		//setBackground(Color.DARK_GRAY);

		resizePanel();
	}

	private void initComponents() {
		menu = new CustomMenu();
		
		
		
		labelDBPathInfo = new JLabel("", SwingConstants.CENTER);
		//labelDBPathInfo.setForeground(Color.WHITE);
		
		
		labelDBPathInfo2 = new JLabel("", SwingConstants.CENTER);
		//labelDBPathInfo2.setForeground(Color.WHITE);
	}

	private void setComponents() {
		frame.setJMenuBar(menu.getMenu());
		add(labelDBPathInfo);
		add(labelDBPathInfo2);
	}
	
	public void setPathInfo(String text) {
		labelDBPathInfo.setText(text);
	}
	protected void setPathInfo2(String text) {
		labelDBPathInfo2.setText(text);
	}
	
	protected void setIconDB() {
		if(labelDBPathInfo.getText().isBlank()) {
			labelDBPathInfo.setIcon( null );
		}else {
			ImageIcon iconDb = Methods.loadImage("datenbank.png", 15, 15);
			
			iconDb.getImage().flush();
			labelDBPathInfo.setIcon( iconDb );
		}
	}
		
		protected void setIconTable() {
			if(labelDBPathInfo2.getText().isBlank()) {
				labelDBPathInfo2.setIcon( null );
			}else {
				ImageIcon iconTable = Methods.loadImage("tabelle.png", 15, 15);
				
				iconTable.getImage().flush();
				labelDBPathInfo2.setIcon( iconTable );
			}
		
	}

	protected void resizePanel() {
		//setPreferredSize(new Dimension(0, (int) (frame.getHeight() - frame.getHeight() / 1.15)));
	}

}
