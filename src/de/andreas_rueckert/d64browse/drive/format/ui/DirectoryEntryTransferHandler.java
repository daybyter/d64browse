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

package de.andreas_rueckert.d64browse.drive.format.ui;

import de.andreas_rueckert.d64browse.drive.format.cpm.ui.CPM4D64BrowserTableModel;
import de.andreas_rueckert.d64browse.drive.format.d64.ui.D64BrowserTableModel;
import de.andreas_rueckert.d64browse.drive.format.DirectoryEntry;
import de.andreas_rueckert.d64browse.drive.format.DiskImage;
import de.andreas_rueckert.d64browse.drive.format.filesystem.ui.FileSystemBrowserTableModel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTargetContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.TransferHandler;


/**
 * This is a transfer handler to drag and drop directory entries.
 *
 * @see <a href="http://stackoverflow.com/questions/638807/how-do-i-drag-and-drop-a-row-in-a-jtable">jtable drag n drop example</a>
 */
public class DirectoryEntryTransferHandler extends TransferHandler {

    // Inner classes


    // Static variables


    // Instance variables

    /**
     * The table with the directory entries.
     */
    private JTable _table = null;


    // Constructors
    
    /**
     * Create a new transfer handler to transfer directory entries.
     *
     * @param table The table with the directory entries.
     */
    public DirectoryEntryTransferHandler( JTable table) {

       _table = table;
    }


    // Methods

    @Override public boolean canImport(TransferHandler.TransferSupport info) {

	boolean b = ( info.getComponent() == _table) && info.isDrop();

	_table.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);

	return b;
    }

    /**
     * Copy a directory entry to a directory.
     *
     * @param directoryEntry The directory entry to copy.
     * @param directory The target directory to copy to.
     */
    private void copyDirectoryEntryToDirectory( DirectoryEntry directoryEntry, File directory) throws IOException {

	if( ! directoryEntry.isDirectory()) {  // If this is a file.

	    // Create a new output file.
	    File outputFile = new File( directory, directoryEntry.getFileName());

	    // Get the disk image, this directory enty is on.
	    DiskImage diskImage = directoryEntry.getDirectory().getDiskImage();

	    // Now get an input stream for the entry.
	    InputStream inputStream = diskImage.getInputStream( directoryEntry);

	    // Create a stream to write to.
	    FileOutputStream outputStream = new FileOutputStream( outputFile);
	    
	    // Now just copy all the bytes from the input stream to the output stream (not very fast, but so what...)
	    int currentByte;
	    while( ( currentByte = inputStream.read()) != -1) {

		outputStream.write( currentByte);
	    }

	    // Close the streams.
	    inputStream.close();
	    outputStream.close();
	}
    }

    /**
     * Make a directory entry a transferable object.
     *
     * @param component The source component, where the dragging starts.
     *
     * @return A directory entry as a transferable object.
     */
    @Override protected Transferable createTransferable( JComponent component) {

	assert( component == _table);  // Make sure, the data are from the directory.

	// Get the table model and the directory entry from it.
	DirectoryEntry entry = null;
	if( _table.getModel() instanceof D64BrowserTableModel) {  // If this is a displayed d64 filesystem.

	    entry = (DirectoryEntry)( ( (D64BrowserTableModel)_table.getModel()).getDirectoryEntry( _table.getSelectedRow()));

	} else if( _table.getModel() instanceof CPM4D64BrowserTableModel) {
	    
	    entry = (DirectoryEntry)( ( (CPM4D64BrowserTableModel)_table.getModel()).getDirectoryEntry( _table.getSelectedRow()));
	    
	}

	// If there is no entry just stop here.
	if( entry == null) {

	    return null;
	}

	// Just use the first and only data flavor to get the directory entry object.
	try {

	    return (DirectoryEntry)entry.getTransferData( entry.getTransferDataFlavors()[0]);

	} catch( UnsupportedFlavorException usfe) {

	    System.err.println( "Unsupported data flavor for drag and drop operation: " + usfe);

	    return null;   // Should never happen.

	} catch( IOException ioe) {

	    System.err.println( "IOException in drag and drop operation: " + ioe);

	    return null;   // Should never happen. 
	}
    }

    /**
     * Check, what we can do with this table.
     *
     * @param component The component sending the data.
     *
     * @return A bitmask indicating the supported actions.
     */
    @Override public int getSourceActions( JComponent component) {

	//return TransferHandler.COPY_OR_MOVE;
	return TransferHandler.COPY;  // No deleting for now.
    }

    /**
     * Do the actual import of data from the clipboard or a dnd operation.
     *
     * @param info The transfer support info with the component, the data etc.
     *
     * @return true, if the data transfer worked. False otherwise.
     */
    @Override public boolean importData( TransferHandler.TransferSupport info) {

        JTable target = (JTable) info.getComponent();  // Get the target of the dnd operation.

        JTable.DropLocation dropLocation = (JTable.DropLocation) info.getDropLocation();

	// Get the transferable to import.
	Transferable transferable = info.getTransferable();

	// Get all the data flavors (should be only 1).
	DataFlavor [] dataFlavors = transferable.getTransferDataFlavors();

	try {
	    // Get the directory entry from the transferable.
	    DirectoryEntry directoryEntry = (DirectoryEntry)transferable.getTransferData( dataFlavors[0]);
	    
	    // Determine the type of drop target.
	    TableModel tableModel = target.getModel();

	    // Is this a file system browser?
	    if( tableModel instanceof FileSystemBrowserTableModel) {

		FileSystemBrowserTableModel fileSystemBrowserModel = (FileSystemBrowserTableModel)tableModel;

		// Get the target directory from the file system browser.
		File currentDirectory = fileSystemBrowserModel.getDirectory();

		// Try to copy the directory entry.
		try {

		    copyDirectoryEntryToDirectory( directoryEntry, currentDirectory);

		} catch( IOException ioe) {

		    System.err.println( "Copying the file failed: " + ioe);
		}

	    }

	    return true;

	} catch( UnsupportedFlavorException usfe) {

	    System.err.println( "Unsupported data flavor for drag and drop operation: " + usfe);

	    return false;   // Should never happen.

	} catch( IOException ioe) {

	    System.err.println( "IOException in drag and drop operation: " + ioe);

	    return false;   // Should never happen. 
	}
    }
}