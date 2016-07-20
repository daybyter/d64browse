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

package de.andreas_rueckert.d64browse.drive.format;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


/**
 * Implementation of a directory entry.
 */
public class DirectoryEntryImpl implements DirectoryEntry {

    // Inner classes


    // Static variables


    // Instance variables
    
    /**
     * The available data flavors of a directory entry.
     */
    private DataFlavor [] _dataFlavors;
    
    /**
     * The directory, this entry belongs to.
     */
    private Directory _directory;

    /**
     * The name of the file, that this directory entry represents.
     */
    private String _filename;

    /**
     * The size of the file, as it is stored in the directory.
     */
    private int _unitFilesize = -1;

    /**
     * Flag to indicate, that this entry represents a directory.
     */
    private boolean _isDirectory = false;

    
    // Constructors

    /**
     * Create an entry for a (non-directory) file.
     *
     * @param filename The name of the file.
     * @param unitFilesize The size of the file as units.
     * @param directory The directory, this entry belongs to.
     */
    public DirectoryEntryImpl( String filename, int unitFilesize, Directory directory) {

	// Store the values in the instance.
	_filename = filename;
	_unitFilesize = unitFilesize;
	_directory = directory;

	// Create the available data flavors of a directory entry.
	_dataFlavors = new DataFlavor[1];

	// Since I just want to dnd within the application, I use a jvm specific flavor.
	try {


	    _dataFlavors[0] = new DataFlavor( DataFlavor.javaJVMLocalObjectMimeType + ";class=de.andreas_rueckert.d64browse.drive.format.DirectoryEntryImpl");

	} catch( ClassNotFoundException cnfe) {

	    // Should never happen.
	    System.err.println( "DirectoryEntryImpl not found for data flavor: " + cnfe); 
	}
    }

    // Methods

    /**
     * Get the directory, that this entry belongs to.
     *
     * @return The directory, that this entry belongs to.
     */
    public Directory getDirectory() {

	return _directory;
    }

    /**
     * Get the name of the file, that this entry represents.
     *
     * @return The name of the file, that this entry represents.
     */
    public String getFileName() {

	return _filename;
    }

    /**
     * Get the size of the file, that this entry represents as bytes.
     *
     * @return the size of the file, that this entry represents as bytes.
     */ 
    public int getFileSizeAsBytes() {

	// Multiply the units with the unit size to get the size as bytes.
	return getFileSizeAsUnits() * getDirectory().getDiskImage().getUnitSize();
    }

    /**
     * Get the size of the file, that this entry represents.
     *
     * @return the size of the file, that this entry represents.
     */ 
    public int getFileSizeAsUnits() {

	return _unitFilesize;
    }

    /**
     * Get the transfer data for a directory entry.
     *
     * @param dataFlavor The data flavor to return.
     *
     * @return The directory entry as the given data flavor.
     */
    public Object getTransferData( DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {

	if( ! isDataFlavorSupported( dataFlavor)) {

	    throw new UnsupportedFlavorException( dataFlavor);
	}

	// Just return the directory entry as a java object.
	return this;
    }

    /**
     * Get the drag and drop data flavor for a directory entry.
     *
     * @return The list of available data flavors.
     */
    public DataFlavor [] getTransferDataFlavors() {
	
	return _dataFlavors;
    }

    /**
     * Check, if a given DataFlavor is supported by this class.
     *
     * @param dataFlavor The data flavor to check.
     *
     * @return true, if this data flavor is supported. False otherwise.
     */
    public boolean isDataFlavorSupported( DataFlavor dataFlavor) {

	// Just loop over the supported data flavors.
	for( DataFlavor currentFlavor : getTransferDataFlavors()) {

	    // If this data flavor equals the given data flavor,
	    if( currentFlavor.equals( dataFlavor)) {

		// the given flavor is supported.
		return true;
	    }
	}

	return false;  // This flavor is not supported.
    }

    /**
     * Check, if this entry is a directory.
     *
     * @return true, if this entry is a directory. False otherwise.
     */
    public boolean isDirectory() {

	return _isDirectory;
    }
}
