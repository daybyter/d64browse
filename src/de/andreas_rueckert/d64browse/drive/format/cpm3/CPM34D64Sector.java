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

package de.andreas_rueckert.d64browse.drive.format.cpm3;

import de.andreas_rueckert.d64browse.drive.format.Sector;
import java.util.List;


/**
 * This class implements a cp/m 3 sector. It's kind of a meta sector, 
 * since it's made up of 4 d64 sector holding 256 byte each.
 */
public class CPM34D64Sector implements Sector {

    // Inner classes


    // Static variables


    // Instance variables

    /**
     * Just keep the d64 sectors as a list of sectors, for now.
     */
    private List<Sector> _d64Sectors;

    /**
     * The index of the sector.
     */
    private int _sectorIndex;

    /**
     * The index of the track.
     */
    private int _trackIndex;


    // Constructors

    /**
     * Create a new cpm 3 sector from a list of d64 sectors.
     *
     * @param d64Sectors 4 d64 sector with 256 byte each.
     * @param trackIndex The track number of the cpm3 sector.
     * @param sectorIndex The sector index of the cpm3 sector.
     */
    public CPM34D64Sector( List<Sector> d64Sectors, int trackIndex, int sectorIndex) {

	_d64Sectors = d64Sectors;
	_trackIndex = trackIndex;
	_sectorIndex = sectorIndex;
    }

    
    // Methods
    /**
     * Get the content of this sector.
     *
     * @return The content of this sector as a list of bytes.
     */
    public byte [] getDataBytes() {

	// To make sure, the date are uptodate, I just create the resulting
	// array on the fly.
	// 4 d64 sectors with 256 byte each are 1024 byte.
	byte [] result = new byte[1024];

	for( int i = 0; i < 4; ++i) {

	    System.arraycopy( _d64Sectors.get(i).getDataBytes()
			      , 0
			      , result
			      , i * 256
			      , 256);
	}

	// Return the create array.
	return result;
    }

    /**
     * Get the index (sector pos) of the sector on the track.
     *
     * @return The index (sector position) of the sector on the track.
     */
    public int getSectorIndex() {
	return _sectorIndex;
    }

    /**
     * Get the size of this sector.
     *
     * @return The size of this sector as an int.
     */
    public int getSize() {
	return 1024;
    }

    /**
     * Get the index of the track of this sector.
     *
     * @return The index of the track of this sector.
     */
    public int getTrackIndex() {
	return _trackIndex;
    }

    /**
     * Set a data byte of the sector.
     *
     * @param pos The position of the byte to set.
     * @param value The new value of the byte.
     */
    public void setDataByte( int position, byte value) {

	// Compute the d64 sector, in which the byte is,
	// and set the byte there.
	_d64Sectors.get( position / 256).setDataByte( position % 256, value);
    }
}
