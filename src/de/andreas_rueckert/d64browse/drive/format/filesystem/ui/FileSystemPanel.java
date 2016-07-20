/**
 * Java tool for C= disk images
 *
 * Copyright (c) 2014 the authors:
 * 
 * @author Andreas Rueckert <mail@andreas-rueckert.de>
 *
 * Permission is hereby granted, free of charge, to any person obtaining 
 * a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION 
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.andreas_rueckert.d64browse.drive.format.filesystem.ui;

import de.andreas_rueckert.d64browse.drive.format.ui.DirectoryEntryTransferHandler;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


/**
 * Class to create a panel for file system browsing.
 */
public class FileSystemPanel extends JPanel {

    // Inner classes


    // Static variables


    // Instance variables
	
    /**
     * The directory, we currently browse.
     */
    private File _directory;

    /**
     * The table to render a directory.
     */
    private JTable _filesTable;


    // Constructors

    /**
     * Create a new panel to browse a file system.
     *
     * @param directory The current directory to browse.
     */
    public FileSystemPanel( File directory) {

	// Store the parameters in the instance.
	_directory = directory;

	// Set a border layout, so we can display additional info.
	setLayout( new BorderLayout());

	// Add info on the displayed filesyste.
	try {

	    add( new JLabel( "Filesystem: " + _directory.getCanonicalPath()), BorderLayout.NORTH);

	} catch( IOException ioe) {  // If we cannot get the absolute path.
	    
	    // Just use the filename.
	    add( new JLabel( "Filesystem: " + _directory.getName()), BorderLayout.NORTH);
	}
	
	// Add a new table with the image contents.
	add( new JScrollPane( _filesTable = new JTable( new FileSystemBrowserTableModel( _directory))), BorderLayout.CENTER);

	// Add a border to the panel.
	setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED));

	// Enable drag and drop for the table.
	_filesTable.setDragEnabled( true);  // Not for now...
	_filesTable.setDropMode( DropMode.INSERT_ROWS);
	_filesTable.setTransferHandler( new DirectoryEntryTransferHandler( _filesTable)); 
    }
	

    // Methods
}