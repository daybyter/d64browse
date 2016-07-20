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

import java.util.List;


/**
 * Instance of this class hold the data of a disk image.
 */
public abstract class DiskImageImpl implements DiskImage {

    // Inner classes

    
    // Static variables


    // Instance variables

    /**
     * Flag to indicate, if this image was modified.
     */
    private boolean _modified = false;
    
    /**
     * The file name of the disk image.
     */
    private String _filename = null;

    /**
     * An image contains a list of sectors.
     */
    protected List<Sector> _sectors;


    // Constructors

    /**
     * Create a new image from a given list of sectors.
     * 
     * @param filename The filename of the image.
     * @param sectors The list of sectors.
     */
    public DiskImageImpl( String filename, List<Sector> sectors) {

	// Store the filename.
	_filename = filename;

	// Store the sectors in the image.
	_sectors = sectors;
    }


    // Methods

    /**
     * Get the filename of this disk image.
     *
     * @return The filename of this disk image.
     */
    public String getFileName() {

	return _filename;
    }

    /**
     * Get the maximum track index.
     *
     * @return The highest track number of the file system, or -1, if no sectors are available.
     */
    public int getMaxTrackIndex() {

	int currentResult = -1;

	for( Sector currentSector : getSectors()) {

	    if( ( currentResult == -1) || ( currentSector.getTrackIndex() > currentResult)) {

		currentResult = currentSector.getTrackIndex();
	    }
	}

	return currentResult;
    }

    /**
     * Get the minimum track number.
     *
     * @return The start track for the filesystem, or -1, if no sectors are available.
     */
    public int getMinTrackIndex() {

	int currentResult = -1;

	for( Sector currentSector : getSectors()) {

	    if( ( currentResult == -1) || ( currentSector.getTrackIndex() < currentResult)) {

		currentResult = currentSector.getTrackIndex();
	    }
	}

	return currentResult;
    }

    /**
     * Get the sector on a given track with a given sector pos.
     *
     * @param trackIndex The index of the track ( 1 .. 35 / 40 ).
     * @param sectorIndex The index of the sector on the given track.
     *
     * @return The found sector or null, if it's not found in the disk image.
     */
    public Sector getSector( int trackIndex, int sectorIndex) {
	
	// The following loop is extremely inefficient, but I cannot assume, that the sectors 
	// are sorted, only more complicated data structure could speed up the seach 
	// (track + sector index combined in a map maybe?).
	for( Sector currentSector : getSectors()) {

	    if( ( currentSector.getTrackIndex() == trackIndex)
		&& ( currentSector.getSectorIndex() == sectorIndex)){

		return currentSector;
	    }
	}
	
	return null;  // Sector not found.
    }

    /**
     * Get all the sectors of this image.
     *
     * @return The sectors of this image.
     */
    public List<Sector> getSectors() {

	return _sectors;
    }

    /**
     * Get the size of the image in bytes.
     *
     * @return The size of the disk image.
     */
    public int getSize() {

	// Just add the size of the sectors up for now.
	int size = 0;
	for( Sector currentSector : _sectors) {

	    size += currentSector.getSize();
	}

	return size;  // Return the result.
    }

    /**
     * Check, if this disk image was modified.
     *
     * @return true, if this disk image was modified. False otherwise.
     */
    public boolean isModified() {

	return _modified;
    }

    /**
     * Set the modified flag of this disk image.
     *
     * @param modified The new value of the modified flag.
     */
    public void setModified( boolean modified) {

	_modified = modified;
    }
}