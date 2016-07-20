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
import de.andreas_rueckert.d64browse.drive.format.Directory;
import de.andreas_rueckert.d64browse.drive.format.DirectoryEntry;
import javax.swing.table.AbstractTableModel;


/**
 * Create a table model for the browser panel.
 */
public class D64BrowserTableModel extends AbstractTableModel {

    // Variables

    /**
     * The column names of the browser.
     */
    private String [] _columnNames = { "Filename", "Filesize" };
	
    /**
     * The currently displayed directory.
     */
    private Directory _directory;
	
    /**
     * The disk image to browse.
     */
    private D64DiskImage _diskImage;


    // Constructors

    /**
     * Create a new table model for the disk image browser.
     *
     * @param diskImage The disk image to browse.
     */
    D64BrowserTableModel( D64DiskImage diskImage) {

	// Store the image reference in the instance.
	_diskImage = diskImage;

	// Get the directory of the image.
	_directory = _diskImage.parseDirectory();
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
     * Get the directory entry of a given row.
     *
     * @param row The table row.
     *
     * @return The directory entry at the given row.
     */
    public DirectoryEntry getDirectoryEntry( int rowIndex) {

	// Get the directory entry of a given row.
	return _directory.getDirectoryEntries().get( rowIndex);
    }

    /**
     * Get the number of rows of the browser table.
     *
     * @return The number of rows of this browser table.
     */
    @Override public int getRowCount() {

	// Just return the number of directory entries.
	return _directory.getDirectoryEntries().size();
    }

    /**
     * Get the field content at a given position of the table.
     *
     * @param rowIndex The index of the row.
     * @param columnIndex The index of the column.
     */
    @Override public Object getValueAt( int rowIndex, int columnIndex) {

	switch( columnIndex) {
	case 0: return _directory.getDirectoryEntries().get( rowIndex).getFileName();
	case 1: return "" + _directory.getDirectoryEntries().get( rowIndex).getFileSizeAsBytes() + " bytes";
	}
	return null;
    }
}
    