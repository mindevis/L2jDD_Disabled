
package org.l2jdd.tools.dbinstaller.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import org.l2jdd.tools.dbinstaller.DBOutputInterface;

/**
 * @author mrTJO
 */
public class DBInstallerGUI extends JFrame implements DBOutputInterface
{
	private final JProgressBar _progBar;
	private final JTextArea _progArea;
	private final Connection _con;
	
	public DBInstallerGUI(Connection con)
	{
		super("L2jDD - Database Installer");
		setLayout(new BorderLayout());
		setDefaultLookAndFeelDecorated(true);
		
		// Set icons.
		final List<Image> icons = new ArrayList<>();
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "database" + File.separator + "Database_16x16.png").getImage());
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "database" + File.separator + "Database_24x24.png").getImage());
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "database" + File.separator + "Database_32x32.png").getImage());
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "database" + File.separator + "Database_64x64.png").getImage());
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "database" + File.separator + "Database_128x128.png").getImage());
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "database" + File.separator + "Database_256x256.png").getImage());
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "database" + File.separator + "Database_512x512.png").getImage());
		setIconImages(icons);
		
		_con = con;
		
		final int width = 480;
		final int height = 360;
		final Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds((resolution.width - width) / 2, (resolution.height - height) / 2, width, height);
		setResizable(false);
		
		_progBar = new JProgressBar();
		_progBar.setIndeterminate(true);
		add(_progBar, BorderLayout.PAGE_START);
		_progArea = new JTextArea();
		final JScrollPane scrollPane = new JScrollPane(_progArea);
		_progArea.setEditable(false);
		appendToProgressArea("Connected");
		
		add(scrollPane, BorderLayout.CENTER);
		getContentPane().setPreferredSize(new Dimension(width, height));
		pack();
	}
	
	@Override
	public void setProgressIndeterminate(boolean value)
	{
		_progBar.setIndeterminate(value);
	}
	
	@Override
	public void setProgressMaximum(int maxValue)
	{
		_progBar.setMaximum(maxValue);
	}
	
	@Override
	public void setProgressValue(int value)
	{
		_progBar.setValue(value);
	}
	
	@Override
	public void appendToProgressArea(String text)
	{
		_progArea.append(text + System.getProperty("line.separator"));
		_progArea.setCaretPosition(_progArea.getDocument().getLength());
	}
	
	@Override
	public Connection getConnection()
	{
		return _con;
	}
	
	@Override
	public void setFrameVisible(boolean value)
	{
		setVisible(value);
	}
	
	@Override
	public int requestConfirm(String title, String message, int type)
	{
		return JOptionPane.showConfirmDialog(null, message, title, type);
	}
	
	@Override
	public void showMessage(String title, String message, int type)
	{
		JOptionPane.showMessageDialog(null, message, title, type);
	}
}
