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

package de.andreas_rueckert.d64browse.drive.format.d64.ui;

import de.andreas_rueckert.d64browse.drive.format.d64.D64DiskImage;
import de.andreas_rueckert.d64browse.drive.format.ui.DirectoryEntryTransferHandler;
import java.awt.BorderLayout;
import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;


/**
 * Class to create a panel for a d64 filebrowser.
 */
public class D64DiskImagePanel extends JPanel {

    // Inner classes


    // Static variables


    // Instance variables

    /**
     * The disk image, we currently browse.
     */
    private D64DiskImage _diskImage;

    /**
     * The table to render a directory.
     */
    private JTable _filesTable;


    // Constructors

    /**
     * Create a new d64 disk image panel.
     */
    public D64DiskImagePanel( D64DiskImage diskImage) {

	// Store the parameters in the instance.
	_diskImage = diskImage;

	// Set a border layout, so we can display additional info.
	setLayout( new BorderLayout());

	// Add info on the displayed filesyste.
	add( new JLabel( "D64 filesystem: " + _diskImage.getFileName()), BorderLayout.NORTH);
	
	// Add a new table with the image contents.
	_filesTable = new JTable( new D64BrowserTableModel( _diskImage));
	add( new JScrollPane( _filesTable), BorderLayout.CENTER);
	
	// Enable drag and drop for the table.
	_filesTable.setDragEnabled( true);  // Not for now...
	_filesTable.setDropMode( DropMode.INSERT_ROWS);
	_filesTable.setTransferHandler( new DirectoryEntryTransferHandler( _filesTable)); 

	// Display the free diskspace.
	add( new JLabel( "" + _diskImage.getFreeDiskSpace() + " bytes free"), BorderLayout.SOUTH);

	// Surround the panel with a border.
	setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED));
    }


    // Methods
}