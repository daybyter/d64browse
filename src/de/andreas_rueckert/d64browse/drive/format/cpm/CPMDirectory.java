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

package de.andreas_rueckert.d64browse.drive.format.cpm;

import de.andreas_rueckert.d64browse.drive.format.DirectoryEntryImpl;
import de.andreas_rueckert.d64browse.drive.format.DirectoryImpl;
import de.andreas_rueckert.d64browse.drive.format.DiskImage;
import java.util.ArrayList;
import java.util.List;


/**
 * Class to represent a CP/M directory.
 */
class CPMDirectory extends DirectoryImpl {

    // Inner classes


    // Static variables


    // Instance variables

    /**
     * The list of extents of this directory.
     */
    private List<CPMDirectoryExtent> _extents = new ArrayList<CPMDirectoryExtent>();


    // Constructors
    
    /**
     * Create a new directory for a given cp/m disk image.
     *
     * @param diskImage The disk image, this directory belongs to.
     */
    public CPMDirectory( DiskImage diskImage) {

	// Use the base class constructor to store the image reference.
	super( diskImage);
    }
    
    // Methods

    /**
     * Add a new extent to the list of extents.
     *
     * @param extent The new extent to add.
     */
    public void addExtent( CPMDirectoryExtent extent) {

	_extents.add( extent);
    }

    /**
     * Create the directory entries for the current extents.
     */
    public void createDirectoryEntriesFromExtents() {

	// Loop over all the filenames of the extents.
	for( String currentFilename : getExtentFilenames()) {

	    // Now get all the extents for the current filename.
	    List<CPMDirectoryExtent> currentExtentList = getExtentsForFilename( currentFilename);

	    // Loop over the extents and add all the used units to get the total filesize.
	    int nUnits = 0;
	    for( CPMDirectoryExtent currentExtent : currentExtentList) {

		// Add the number of records from the current extent.
		nUnits += currentExtent.getRecordCount();
	    }

	    // Now create a directory entry for this filename.
	    addDirectoryEntry( new CPMDirectoryEntry( currentFilename, nUnits, currentExtentList, this));
	}
    }

    /**
     * Get all the extent filenames with removed duplicates.
     *
     * @return The extent filenames with removed duplicates.
     */
    public List<String> getExtentFilenames() {

	// Create a buffer for the result.
	List<String> result = new ArrayList<String>();

	// Loop over the extents.
	for( CPMDirectoryExtent currentExtent : _extents) {

	    // if the filename of this extent is not already in the result,
	    if( ! result.contains( currentExtent.getFileName())) {
		
		// add it to the result.
		result.add( currentExtent.getFileName());
	    }
			    
	}

	// Return the resulting list.
	return result;
    }

    /**
     * Get all the extents for a given filename.
     *
     * @param filename The name of the file.
     *
     * @return A list of extents for the given file.
     */
    public List<CPMDirectoryExtent> getExtentsForFilename( String filename) {

	// Create a list for the result.
	List<CPMDirectoryExtent> result = new ArrayList<CPMDirectoryExtent>();

	// Loop over the list of available extents.
	for( CPMDirectoryExtent currentExtent : _extents) {

	    // If the filename of the extent equals the given filename.
	    if( filename.equals( currentExtent.getFileName())) {

		// Add this extent to the result.
		result.add( currentExtent);
	    }
	}

	return result;  // Return the list of results.
    }
}