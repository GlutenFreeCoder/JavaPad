/**
 *	Java-based text editor
 *	
 *  @author		Matthew Douglas
 *  @version	0.1
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * Creates GUI for editing and viewing text files, and 
 * provides a menu bar for additional options and functionality.
 * 
 * @author 	Matthew Douglas
 */
@SuppressWarnings("serial")
public class mainEditor extends JFrame {
	
	//variable declaration
	JPanel mainPanel, editorPanel, displayPanel;
	JMenuBar mbMain;
	JMenu menuFile, menuEdit, menuFormat, menuWindow, menuHelp; 
	JMenuItem miFileNew, miFileOpen, miFileSave, miFileSaveAs, miFileClose, miFileQuit;  //File menu options
	JMenuItem miEditUndo, miEditRedo, miEditCut, miEditCopy, miEditPaste;  //Edit menu options
	JMenuItem miFormatFont, miFormatBold, miFormatItalic, miFormatUnderline, miFormatActualSize, miFormatZoomIn, miFormatZoomOut;  //Format menu options
	JMenuItem miWindowMin, miWindowBtFront, miWindowShowNamed;  //Window menu options
	JMenuItem miHelpWelcome, miHelpContents, miHelpSearch;  //Help menu options
	JDialog newFile;
	JTextArea editorTextArea;
	JScrollPane editorArea;
	JLabel fileName;
	Font fileNameFont;
	BufferedReader inFileStream;
	File openFile;
	
	//constructor
	public mainEditor() {
		setTitle("Text Editor");
		initWindow();
		
		//initialize the event listeners
		initFileNew(); 
		initFileOpener();
		initFileClose();
		initFileQuit();
		
		pack();
	} //end constructor
	
	
	/**
	 * Creates the menu bar and main display panel
	 */
	private void initWindow() {
		//set up main window area
		mainPanel = new JPanel();
		editorPanel = new JPanel(new BorderLayout());
		displayPanel= new JPanel(new BorderLayout());
		Dimension d = new Dimension(600,800);
		mainPanel.setPreferredSize(d);
		mainPanel.setBackground(Color.gray);
		editorPanel.setBackground(Color.gray);
		displayPanel.setBackground(Color.gray);
		fileNameFont = new Font("SansSerif", Font.BOLD, 14);
		
		//set up menu bar
		mbMain = new JMenuBar();  
		mbMain.setBackground(Color.lightGray);
		
		//set up file menu and options
		menuFile = new JMenu("File");
		menuFile.setBackground(Color.blue);
		miFileNew = new JMenuItem("New");
		miFileNew.setBackground(Color.lightGray);
		miFileOpen = new JMenuItem("Open...");
		miFileOpen.setBackground(Color.lightGray);
		miFileClose = new JMenuItem("Close");
		miFileClose.setBackground(Color.lightGray);			
		miFileSave = new JMenuItem("Save");
		miFileSave.setBackground(Color.lightGray);
		miFileSaveAs = new JMenuItem("Save As...");
		miFileSaveAs.setBackground(Color.lightGray);
		miFileQuit = new JMenuItem("Quit");
		miFileQuit.setBackground(Color.lightGray);
		
		menuFile.add(miFileNew);
		menuFile.add(miFileOpen);
		menuFile.add(miFileClose);
		menuFile.add(miFileSave);
		menuFile.add(miFileSaveAs);
		menuFile.add(miFileQuit);

		//set up Edit menu and options
		menuEdit = new JMenu("Edit");
		menuEdit.setBackground(Color.blue);
		miEditUndo = new JMenuItem("Undo");
		miEditUndo.setBackground(Color.lightGray);
		miEditRedo = new JMenuItem("Redo");
		miEditRedo.setBackground(Color.lightGray);
		miEditCut = new JMenuItem("Cut");
		miEditCut.setBackground(Color.lightGray);
		miEditCopy = new JMenuItem("Copy");
		miEditCopy.setBackground(Color.lightGray);
		miEditPaste = new JMenuItem("Paste");
		miEditPaste.setBackground(Color.lightGray);
		
		menuEdit.add(miEditUndo);
		menuEdit.add(miEditRedo);
		menuEdit.add(miEditCut);
		menuEdit.add(miEditCopy);
		menuEdit.add(miEditPaste);
		
		//set up Format menu and options
		menuFormat = new JMenu("Format");
		menuFormat.setBackground(Color.blue);
		miFormatFont = new JMenuItem("Fonts");
		miFormatFont.setBackground(Color.lightGray);
		miFormatBold = new JMenuItem("Bold");
		miFormatBold.setBackground(Color.lightGray);
		miFormatItalic = new JMenuItem("Italic");
		miFormatItalic.setBackground(Color.lightGray);
		miFormatUnderline = new JMenuItem("Underline");
		miFormatUnderline.setBackground(Color.lightGray);
		miFormatActualSize = new JMenuItem("Actual Size");
		miFormatActualSize.setBackground(Color.lightGray);
		miFormatZoomIn = new JMenuItem("Zoom In");
		miFormatZoomIn.setBackground(Color.lightGray);
		miFormatZoomOut = new JMenuItem("Zoom Out");
		miFormatZoomOut.setBackground(Color.lightGray);
		
		menuFormat.add(miFormatFont);
		menuFormat.add(miFormatBold);
		menuFormat.add(miFormatItalic);
		menuFormat.add(miFormatUnderline);
		menuFormat.add(miFormatActualSize);
		menuFormat.add(miFormatZoomIn);
		menuFormat.add(miFormatZoomOut);
		
		//set up Window menu and options
		menuWindow = new JMenu("Window");
		menuWindow.setBackground(Color.blue);
		miWindowMin = new JMenuItem("Minimize");
		miWindowMin.setBackground(Color.lightGray);
		miWindowBtFront = new JMenuItem("Bring All to Front");
		miWindowBtFront.setBackground(Color.lightGray);
		miWindowShowNamed = new JMenuItem("Show Named Window");
		miWindowShowNamed.setBackground(Color.lightGray);
		
		menuWindow.add(miWindowMin);
		menuWindow.add(miWindowBtFront);
		menuWindow.add(miWindowShowNamed);
		
		//set up Help menu and options
		menuHelp = new JMenu("Help");
		menuHelp.setBackground(Color.blue);
		miHelpWelcome = new JMenuItem("Welcome");
		miHelpWelcome.setBackground(Color.lightGray);
		miHelpContents = new JMenuItem("Contents");
		miHelpContents.setBackground(Color.lightGray);
		miHelpSearch = new JMenuItem("Search");
		miHelpSearch.setBackground(Color.lightGray);
		
		menuHelp.add(miHelpWelcome);
		menuHelp.add(miHelpContents);
		menuHelp.add(miHelpSearch);
		
		//setting up menu bar and text area
		mbMain.add(menuFile);
		mbMain.add(menuEdit);
		mbMain.add(menuFormat);
		mbMain.add(menuWindow);
		mbMain.add(menuHelp);

		setJMenuBar(mbMain);		
		add(mainPanel, BorderLayout.CENTER);

	}  // end initWindow
	
	
	/**
	 * Sets an ActionListener to catch menu item selections (miClick).
	 * Clicking on "New" calls the displayFile method with no arguments, which
	 * creates a new editing session (text area), providing a confirmation dialog 
	 * box when creating the new file (e.g. Creating document "Untitled"), which 
	 * is then displayed above the editable text area.
	 */
	private void initFileNew() {
		miFileNew.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent miClick) {
		    	miFileNewActionPerformed(miClick);
		    }
		}); 
	}
	
	private void miFileNewActionPerformed(ActionEvent evt) {
		//calls displayFile with no arguments, creating a new, untitled file
		displayFile();
	}
	
	/**
	 * Sets an ActionListener to catch menu item selections (miClick).
	 * Clicking on Open triggers a JFileChooser prompt, which then
	 * calls the displayFile method with the file chosen. This file
	 * is then displayed in the main window as an on-screen text buffer.
	 */
	private void initFileOpener() {
		miFileOpen.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent miClick) {
		    	miFileOpenActionPerformed(miClick);
		    }
		});
	}
	
	private void miFileOpenActionPerformed(ActionEvent evt) {
		//Displays the file chooser dialog, and then calls displayFile to show the contents in the editorTextArea.
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text (.txt) Files", "txt");
		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			inFileStream = null;
			try {
				inFileStream = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
				displayFile(inFileStream, fileChooser.getSelectedFile().getName());
			} 
			catch (IOException ex) {	
				JOptionPane.showMessageDialog(this, "Could not open file!", "I/O Error", JOptionPane.WARNING_MESSAGE); 
			}
			finally {
				try {
					inFileStream.close();
				} 
				catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Could not properly close file!", "Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}
	

	/**
	 * Displays file contents provided by the BufferedReader in the editorTextArea,
	 * with the name of the file as a label above the file text.
	 * 
	 * @param 	inStream		BufferedReader, file text to display.
	 * @param 	name			String, file's name.
	 * @throws 	IOException		Exception thrown if file cannot be read.
	 */
	private void displayFile(BufferedReader inStream, String name) throws IOException {
		//creates the text area for the new file
		editorTextArea = new JTextArea();
		editorTextArea.setRows(40);
		editorTextArea.setColumns(40);
		editorTextArea.setMargin(new Insets(10,10,10,10));
		editorTextArea.setLineWrap(true);
		editorTextArea.setWrapStyleWord(true);
		fileName = new JLabel(name);
		fileName.setFont(fileNameFont);
		fileName.setHorizontalAlignment(JLabel.CENTER);
		editorPanel.removeAll();
		editorPanel.add(editorTextArea, BorderLayout.CENTER);
		
		//reads in file
		String inFileText;
		inFileStream = inStream;
		while ((inFileText = inFileStream.readLine()) != null) {
			editorTextArea.append(inFileText);
			editorTextArea.append(System.getProperty("line.separator"));
		}
		
		//displays the file
		editorArea = new JScrollPane(editorPanel);
		editorArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		displayPanel.removeAll();
		displayPanel.add(fileName, BorderLayout.NORTH);
		displayPanel.add(editorArea, BorderLayout.CENTER);
		mainPanel.removeAll();
		mainPanel.add(displayPanel, BorderLayout.CENTER);
		
		revalidate();
		repaint();
		
		//confirmation dialog box seems unnecessary, but if needed, uncomment the line below.
		//JOptionPane.showMessageDialog(this, "Opening document " + "'", "Opening Document...", JOptionPane.PLAIN_MESSAGE);
		
	}
	
	/**
	 * When provided no arguments, displays a blank JTextArea for user input,
	 * with the label of Untitled above.  Also provides a dialog box informing
	 * the user that a new editor session ("Untitled") has been created.
	 */
	private void displayFile() {
		//creates the text area for the new file
		editorTextArea = new JTextArea();
		editorTextArea.setRows(40);
		editorTextArea.setColumns(40);
		editorTextArea.setMargin(new Insets(10,10,10,10));
		editorTextArea.setLineWrap(true);
		editorTextArea.setWrapStyleWord(true);
		fileName = new JLabel("Untitled");
		fileName.setFont(fileNameFont);
		fileName.setHorizontalAlignment(JLabel.CENTER);
		editorPanel.removeAll();
		editorPanel.add(editorTextArea, BorderLayout.CENTER);
		
		//displays the text area and label
		editorArea = new JScrollPane(editorPanel);
		editorArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		displayPanel.removeAll();
		displayPanel.add(fileName, BorderLayout.NORTH);
		displayPanel.add(editorArea, BorderLayout.CENTER);
		mainPanel.removeAll();
		mainPanel.add(displayPanel, BorderLayout.CENTER);
 
		revalidate();
		repaint();
		
		//provides dialog box for user, indicating that a new document has been generated
		JOptionPane.showMessageDialog(this, "Creating document 'Untitled'", "New Document", JOptionPane.PLAIN_MESSAGE);
		
	}
	
	
	/**
	 * Sets an ActionListener to catch menu item selections (miClick).
	 * Closes the open file and returns to the default editor view.
	 */
	private void initFileClose() {
		miFileClose.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent miClick) {
		    	// Tries to close any open file streams
		    	if (inFileStream != null) {
		    		try {
						inFileStream.close();
					} 
					catch (Exception ex) {
						//ignores exception
					}
		    	}
		    	
		    	//restores the default editor view
		    	mainPanel.removeAll();
				revalidate();
				repaint();
		    }
		});
	}
	
	
	/**
	 * Sets an ActionListener to catch menu item selections (miClick).
	 * Closes the window, releasing the resources for the system to clean up before the JVM exits.
	 * Using the dispose() method instead of System.exit() so that any threads still in progress are
	 * allowed to complete before the JVM exits. 
	 */
	private void initFileQuit() {
		miFileQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent miClick) {
				SwingUtilities.getWindowAncestor(mainPanel).dispose();
			}
		});
	}
	
	
	/**
	 * Creates an instance of mainEditor and runs it, displaying the application window.
	 * 
	 * @param 	args	No arguments expected - will be ignored.
	 */
	public static void main(String[] args) {
        //Creates and displays the application window
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new mainEditor().setVisible(true);
            }
        });
	}
	
}
