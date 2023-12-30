package gui_main;

import javax.swing.JPanel;

//TODO: Working on it
/**
 * Klasse fuer das South-Main-Panel inklusive Verhalten und Aussehen
 * 
 * Entwickler: Jan Schwenger
 */
public class South_Main_Panel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private Main_Frame frame;
	
	public South_Main_Panel(Main_Frame frame) {
		this.frame = frame;
		initComponents();
		setComponents();
		//resizePanel();
	}

	private void initComponents() {
		
	}

	private void setComponents() {
	}
	
	

	/*protected void resizePanel() {
		
		//setPreferredSize(new Dimension(0, (int)(frame.getHeight() - frame.getHeight() / 1.15)));	
	}*/

}
