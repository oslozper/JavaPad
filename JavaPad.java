/*
Course:				CP1340 - Object Oriented Programming
Lab 5:				GUI and Swing
File:				JavaPad.java
Description:		Implementation of JavaPad where a user can work on a
					document processing application. The user can open
					a file and save it.
Date:				November 26, 2020
Name:				OSCAR LOZANO-PEREZ
Student Number:		20164974
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class JavaPad extends JFrame{

	// Menu Objects
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu helpMenu;

	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem exitMenuItem;

	private JMenuItem aboutMenuItem;

	// Text Area object
	private JTextArea edit;

	// Icon object (presented on APP window)
	private ImageIcon javaPadIcon;

	// Application variables
	private String applicationName = "JavaPad";
	private String untitled = "Untitled";
	private String aboutJavaPad = applicationName + 
		" Version 1.0" + "\n" + 
		"CP1340 - Object Oriented Programming" + "\n" +
		"Oscar Lozano-Perez" + "\n" +
		"November 26, 2020";

	// JavaPad Constructor that initializes the basic application elements
	public JavaPad(){
		setTitle(untitled + " - JavaPad");

		// Sets the application icon
		javaPadIcon = new ImageIcon("Images/JavaPad.png");
		setIconImage(javaPadIcon.getImage());

		// Application methods to be executed
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(640,480));
		buildGUI();
		pack();
		setVisible(true);
	}

	// Creation of the application work environment
	private void buildGUI(){
		Container c = getContentPane();
		menuBar = new JMenuBar();
		c.add(menuBar, BorderLayout.NORTH);

		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		newMenuItem = new JMenuItem("New");
		fileMenu.add(newMenuItem);
		fileMenu.addSeparator();

		openMenuItem = new JMenuItem("Open");
		fileMenu.add(openMenuItem);
		fileMenu.addSeparator();

		saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();

		exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);

		helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);

		aboutMenuItem = new JMenuItem("About " + applicationName);
		helpMenu.add(aboutMenuItem);

		edit = new JTextArea(30,20);
		edit.setFont(new Font("Menlo", Font.PLAIN, 14));
		c.add(edit, BorderLayout.CENTER);

		JScrollPane sp = new JScrollPane(edit);
		sp.setPreferredSize(new Dimension(450,100));
		c.add(sp, BorderLayout.CENTER);

		MenuListener ml = new MenuListener(edit);
		newMenuItem.addActionListener(ml);
		openMenuItem.addActionListener(ml);
		saveMenuItem.addActionListener(ml);
		exitMenuItem.addActionListener(ml);
		aboutMenuItem.addActionListener(ml);
	}

	// Creation of a JavaPad object for the application execution
	public static void main(String[] args) throws IOException{
		JavaPad javaPad = new JavaPad();
	}

	// MenuListener that triggers events depending on the menu item selected
	public class MenuListener implements ActionListener {
		private JFileChooser fc;
		private JTextArea area;

		public MenuListener(JTextArea area){
			fc = new JFileChooser();
			this.area = area;
		}

		// Event execution
		public void actionPerformed(ActionEvent event){

			// New Item
			if(event.getSource() == newMenuItem){
				if(edit.getText().length()>0){
					int result = JOptionPane.showConfirmDialog(null,
						"Do you want to save changes to Untitled?", 
						"JavaPad",
		            JOptionPane.YES_NO_CANCEL_OPTION,
		            JOptionPane.QUESTION_MESSAGE);
			        if(result == JOptionPane.YES_OPTION){
			        	saveFile();
			        }
			        else if (result == JOptionPane.NO_OPTION){
			        	edit.setText("");
			        	setTitle(untitled + " - JavaPad");
			        }
			        else if (result == JOptionPane.CANCEL_OPTION){
			        	// No action required
			        }
			        else {
			        	// No action required
			        }
				}
				else{
					edit.setText("");
				}
			}

			// Open Item
			else if(event.getSource() == openMenuItem){
				openFile();
			}

			// Save Item
			else if(event.getSource() == saveMenuItem){
				saveFile();
			}

			// Exit Item
			else if(event.getSource() == exitMenuItem){
				if(edit.getText().length()>0){
					int result = JOptionPane.showConfirmDialog(null,
						"Do you want to save changes to Untitled?", 
						"JavaPad",
		            JOptionPane.YES_NO_CANCEL_OPTION,
		            JOptionPane.QUESTION_MESSAGE);
			        if(result == JOptionPane.YES_OPTION){
			        	saveFile();
			        	System.exit(0);
			        }
			        else if (result == JOptionPane.NO_OPTION){
			        	System.exit(0);
			        }
			        else if (result == JOptionPane.CANCEL_OPTION){
			        	// No action required
			        }
			        else {
			        	// No action required
			        }
				}
				else
					System.exit(0);				
			}

			// About JavaPad Item
			else if(event.getSource() == aboutMenuItem){
				JOptionPane.showInternalMessageDialog(null,
					aboutJavaPad, 
					applicationName,
	            	JOptionPane.INFORMATION_MESSAGE, javaPadIcon);
			}
		}

		// Method that opens a file for edition
		public void openFile(){
			int returnVal = fc.showOpenDialog(JavaPad.this);
			if(returnVal==JFileChooser.APPROVE_OPTION){
				String path = fc.getSelectedFile().getPath();
				String fileName = fc.getSelectedFile().getName();
				File f = new File(path);
				try{
					readFile(f);
					setTitle(fileName + " - " + applicationName);
				}
				catch(Exception fileEvent){
					System.out.println("Cannot read File");
				}
				
			}
		}

		// Method that saves the text into a file
		public void saveFile(){
			int returnVal = fc.showSaveDialog(JavaPad.this);
			if(returnVal==JFileChooser.APPROVE_OPTION){
				String path = fc.getSelectedFile().getPath();
				String fileName = fc.getSelectedFile().getName();
				File f = new File(path);
				try{
					writeFile(f);
					setTitle(fileName + " - " + applicationName);
				}
				catch(Exception fileEvent){
					System.out.println("Cannot write File");
				}
			}
		} 	    
	}

	// Method that reads a file and sets it on the text area
	public void readFile(File f) throws IOException {
	    FileReader in = new FileReader(f);
	    int size = (int) f.length();
	    char data[] = new char[size];
	    in.read(data);
	    in.close();
	    String s = new String(data);
	    edit.setText(s);
	}

	// Method that reads the text area and saves it onto a file
    public void writeFile(File f) throws IOException {
        FileWriter out = new FileWriter(f);
        String s = edit.getText();
        out.write(s);
        out.close();
    }


}