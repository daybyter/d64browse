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


/**
 * Class to hold the data for an cp/m directory extent.
 */
public class CPMDirectoryExtent implements Comparable<CPMDirectoryExtent> {

    // Inner classes


    // Static variables


    // Instance variables
    
    /**
     * Get the memory allocation blocks from this extent.
     */
    private byte [] _blocks;

    /**
     * The number of this extent.
     */
    private int _extentNumber;
    
    /**
     * The filename of this extent (including the suffix).
     */
    private String _filename;

    /**
     * The number of records, that this extent represents.
     */
    private int _recordCount;


    // Constructors

    /**
     * Create a new directory extent.
     *
     * @param filename The filename of the extent.
     * @param extentNumer The number of this extent.
     * @param nrecords The number of records, that this extent uses.
     * @param blocks The memory allocation blocks of this extent.
     */
    public CPMDirectoryExtent( String filename, int extentNumber, int nrecords, byte [] blocks) {

	// Store the data in the instance.
	_filename = filename;
	_extentNumber = extentNumber;
	_recordCount = nrecords;
	_blocks = blocks;
    }


    // Methods

    /**
     * Compare 2 extents, so they can be sorted according to their extent number.
     *
     * @param extent The extent to compare to.
     *
     * @return < 0, if the other extent has a higher extent number. 0, if they are equal. > 0, if this extent has a higher number.
     */
    @Override public int compareTo( CPMDirectoryExtent extent) {

	return getExtentNumber() - extent.getExtentNumber();
    }

    /**
     * Get the memory allocation blocks of this extent.
     *
     * @return The memory allocation blocks of this extent.
     */
    public byte [] getBlocks() {

	return _blocks;
    }

    /**
     * Get the number of this extent.
     *
     * @return The number of this extent.
     */
    public int getExtentNumber() {
	
	return _extentNumber;
    }

    /**
     * Get the filename of this extent.
     *
     * @return The filename of this extent.
     */
    public String getFileName() {
	
	return _filename;
    }
    
    /**
     * Get the number of records, that this extent represents.
     *
     * @return The number of records, that this extent represents.
     */
    public int getRecordCount() {

	return _recordCount;
    }
}