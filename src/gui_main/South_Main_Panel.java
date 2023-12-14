package gui_main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class South_Main_Panel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Main_Frame frame;
	
	public South_Main_Panel(Main_Frame frame) {
		this.frame = frame;
		initComponents();
		setComponents();
		setBackground(Color.DARK_GRAY);
		
		//setPreferredSize(new Dimension(0, 120));
		resizePanel();
	}

	private void initComponents() {
		
	}

	private void setComponents() {
	}
	
	

	protected void resizePanel() {
		
		//setPreferredSize(new Dimension(0, (int)(frame.getHeight() - frame.getHeight() / 1.15)));	
	}

}
