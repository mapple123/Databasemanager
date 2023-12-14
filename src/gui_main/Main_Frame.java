package gui_main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import mainpackage.ALL_CONST;

public class Main_Frame extends JFrame implements KeyListener, WindowStateListener, ComponentListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Container mainPanel;
	private Center_Main_Panel centerPanel;
	private North_Main_Panel northPanel;
	private South_Main_Panel southPanel;
	private West_Main_Panel westPanel;
	private East_Main_Panel eastPanel;

	private JSplitPane sp, sp2;

	public Main_Frame() {
		super(ALL_CONST.MAIN_FRAME_TITLE);
		initComponents();

		mainPanel.setLayout(new BorderLayout());

		setComponents();

		addKeyListener(this);
		addWindowStateListener(this);
		addComponentListener(this);
		addMouseListener(this);
		setFocusable(true);

		// Frame settings
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setSize(new Dimension(800, 650));
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initComponents() {
		mainPanel = this.getContentPane();
		centerPanel = new Center_Main_Panel(this);
		northPanel = new North_Main_Panel(this);
		southPanel = new South_Main_Panel(this);
		westPanel = new West_Main_Panel(this);
		eastPanel = new East_Main_Panel(this);
		eastPanel.setVisible(false);
	}

	private void setComponents() {
		sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel, centerPanel);
		sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, eastPanel);

		sp.setContinuousLayout(true);
		sp2.setContinuousLayout(true);

		sp2.setDividerSize(0);

		sp.setDividerLocation(290);
		mainPanel.add(sp2, BorderLayout.CENTER);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		// mainPanel.add(eastPanel, BorderLayout.EAST);
		// mainPanel.add(westPanel, BorderLayout.WEST);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		refreshTable(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		refreshTable(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		refreshTable(e);

	}

	private void refreshTable(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_R) {
			JOptionPane.showMessageDialog(null, "Erfolgreich aktualisiert", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
			try {
				centerPanel.getTable().setModel(centerPanel.loadData(westPanel.dbName, westPanel.tableName));
				centerPanel.getTable().revalidate();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	protected void refreshTable() {

		
		try {
			centerPanel.getTable().setModel(centerPanel.loadData(westPanel.dbName, westPanel.tableName));
			TableRowSorter<TableModel>  rowSorter= new TableRowSorter<>(centerPanel.getTable().getModel());
			
			centerPanel.setRowSorter(rowSorter);
			
			centerPanel.getTable().setRowSorter(rowSorter);
			centerPanel.getTable().revalidate();
			JOptionPane.showMessageDialog(null, "Erfolgreich aktualisiert", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public void windowStateChanged(WindowEvent e) {

		sp.setDividerLocation(290);
		sp.invalidate();
		sp.validate();
		sp2.setDividerLocation(this.getWidth() - 290);
		sp2.invalidate();
		sp2.validate();
		resizeAllComponents();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		resizeAllComponents();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// resizeAllComponents();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		resizeAllComponents();

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	private void resizeAllComponents() {

		sp.setDividerLocation(290);
		sp.invalidate();
		sp.validate();
		sp2.setDividerLocation(this.getWidth() - 290);
		sp2.invalidate();
		sp2.validate();
		centerPanel.resizeTable();
		southPanel.resizePanel();
		northPanel.resizePanel();
		westPanel.resizePanel();
		eastPanel.resizePanel();
		mainPanel.revalidate();
		centerPanel.revalidate();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		requestFocusOnMainFrame();

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		requestFocusOnMainFrame();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void requestFocusOnMainFrame() {
		this.requestFocus();
	}

	protected East_Main_Panel getEastPanel() {
		return eastPanel;
	}

	public West_Main_Panel getWestPanel() {
		return westPanel;
	}

	protected Center_Main_Panel getCenterPanel() {
		return centerPanel;
	}

	public North_Main_Panel getNorthPanel() {
		return northPanel;
	}

	protected JSplitPane getWestSplitPane() {
		return sp;
	}

	protected JSplitPane getEastSplitPane() {
		return sp2;
	}

}
