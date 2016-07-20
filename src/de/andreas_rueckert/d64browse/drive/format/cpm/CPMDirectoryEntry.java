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

import de.andreas_rueckert.d64browse.drive.format.Directory;
import de.andreas_rueckert.d64browse.drive.format.DirectoryEntryImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * This class holds a cpm directory entry.
 */
public class CPMDirectoryEntry extends DirectoryEntryImpl {

    // Inner classes


    // Static variables


    // Instance variables

    /**
     * The list of extents of this directory entry.
     */
    private List<CPMDirectoryExtent> _extents = new ArrayList<CPMDirectoryExtent>();


    // Constructors

    /**
     * Create an entry for a (non-directory) file.
     *
     * @param filename The name of the file.
     * @param unitFilesize The size of the file as units.
     
     * @param directory The directory, this entry belongs to.
     */
    public CPMDirectoryEntry( String filename, int unitFilesize, List<CPMDirectoryExtent> extents, Directory directory) {

	// Call the super constructor.
	super( filename, unitFilesize, directory);

	// Store the values in the instance.
	_extents = extents;

	// Sort all the extents according to their extent number, so the blocks are returned in correct order.
	Collections.sort( _extents);
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
     * Get the memory allocation blocks from all extents.
     *
     * @return The memory allocation blocks from all extents.
     */
    public byte [] getAllBlocks() {

	// Compute a buffer for the result.
	// Each extent has 16 blocks (= bytes) max?
	byte [] result = new byte[ getExtents().size() * 16];

	// A pointer to the current pos in the result array.
	int currentResultIndex = 0;
	
	// Loop over the extents and add the block from each extent to the result.
	for( CPMDirectoryExtent currentExtent : getExtents()) {
	    
	    // Copy the 16 blocks from the current extent.
	    System.arraycopy( currentExtent.getBlocks(), 0, result, currentResultIndex, 16);

	    // Add 16 bytes to the pointer for the next blocks.
	    currentResultIndex += 16;
	}

	// Return the result.
	return result;
    }

    /**
     * Get all the extents of this directory entry.
     *
     * @return All the extends of this directory entry.
     */
    public List<CPMDirectoryExtent> getExtents() {

	return _extents;
    }
}