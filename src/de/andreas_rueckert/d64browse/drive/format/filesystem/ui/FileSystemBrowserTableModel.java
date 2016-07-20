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

import java.io.File;
import javax.swing.table.AbstractTableModel;


/**
 * Create a table model for the browser panel.
 */
public class FileSystemBrowserTableModel extends AbstractTableModel {
	
    // Variables

    /**
     * The column names of the browser.
     */
    private String [] _columnNames = { "Filename", "Filesize" };

    /**
     * The current directory.
     */
    private File _directory;

    private File [] _directoryContent;


    // Constructors

    /**
     * Create a new table model for the filesystem browser.
     *
     * @param directory The directory to start with.
     */
    FileSystemBrowserTableModel( File directory) {

	// Store the directory in the instance.
	_directory = directory;

	// Also get the content of the directory.
	_directoryContent = _directory.listFiles();
    }


    // Methods

    /**
     * Get the number of columns.
     *
     * @return The number of columns.
     */
    @Override public int getColumnCount() {

	return _columnNames.length;
    }

    /**
     * Get the column name for a given column index.
     * 
     * @param columnIndex The index of the column.
     *
     * @return The name of the column.
     */
    @Override public String getColumnName( int columnIndex) {

	return _columnNames[ columnIndex];
    }

    /**
     * Get the current directory of this filesystem browser.
     *
     * @return The current directory of this filesystem browser.
     */
    public File getDirectory() {

	return _directory;
    }

    /**
     * Get the number of rows of the browser table.
     *
     * @return The number of rows of this browser table.
     */
    @Override public int getRowCount() {

	// Just return the number of directory entries.
	return _directoryContent.length;
    }

    /**
     * Get the field content at a given position of the table.
     *
     * @param rowIndex The index of the row.
     * @param columnIndex The index of the column.
     */
    @Override public Object getValueAt(int rowIndex, int columnIndex) {

	switch( columnIndex) {
	case 0: return _directoryContent[rowIndex].getName();
	case 1: if( _directoryContent[rowIndex].isDirectory()) {
		return "<directory>";
	    } else {
		return "" + _directoryContent[rowIndex].length() + " bytes";
	    }
	}
	return null;
    }
}

