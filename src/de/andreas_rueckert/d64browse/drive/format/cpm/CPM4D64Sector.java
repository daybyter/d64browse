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

import de.andreas_rueckert.d64browse.drive.format.d64.D64Sector;
import de.andreas_rueckert.d64browse.drive.format.SectorImpl;


/**
 * Class to hold the data of a cp/m sector overlayed on top of a d64 sector.
 */
class CPM4D64Sector extends SectorImpl {

    // Inner classes

    
    // Static variables


    // Instance variables

    /** 
     * The d64 sector under the cp/m sector.
     */
    private D64Sector _d64Sector;


    // Constructors

    /**
     * Create a new sector from some given data.
     *
     * @param data A buffer with the data.
     * @param dataStart The start index of the data.
     * @param trackIndex The index of the track.
     * @param sectorIndex The index of the sector on the given track.
     */
    public CPM4D64Sector( D64Sector d64Sector, int dataStart, int trackIndex, int sectorIndex) {

	// Store the parameter in the sector instance.
	// d64 sectors are always 256 it seems.
	super( d64Sector.getDataBytes(), dataStart, 128, trackIndex, sectorIndex);
    }


    // Methods

    /**
     * Get the d64 sector under this cp/m sector.
     *
     * @return The d64 sector under this cp/m sector.
     */
    public D64Sector getD64Sector() {

	return _d64Sector;
    }
}