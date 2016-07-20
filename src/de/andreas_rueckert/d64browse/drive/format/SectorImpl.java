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


/**
 * Generic class to hold the data for a sector.
 */
public class SectorImpl implements Sector {

    // Inner classes

    
    // Static variables


    // Instance variables

    /**
     * A buffer holding the data of the sector.
     */
    private byte [] _dataBuffer;

    /**
     * The start index of the sector data in the buffer;
     */
    private int _dataStart = 0;

    /**
     * The index of the sector on the current track (starting at 0!).
     */
    private int _sectorIndex;

    /**
     * The size of this sector in bytes.
     */
    private int _size;

    /** 
     * The track index of this sector (starting with track 0!).
     */
    private int _trackIndex;


    // Constructors

    /**
     * Create a new sector from some given data.
     *
     * @param data A buffer with the data.
     * @param dataStart The start index of the data.
     * @param size The size of the sector in bytes.
     * @param trackIndex The index of the track.
     * @param sectorIndex The index of the sector on the given track.
     */
    public SectorImpl( byte [] data, int dataStart, int size, int trackIndex, int sectorIndex) {

	// Store the parameter in the sector instance.
	_dataBuffer = data;
	_dataStart = dataStart;
	_size = size;
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
	
	// Create a buffer for the result.
	byte [] result = new byte[ getSize()];
	
	// Copy the bytes of this sector to the result.
	System.arraycopy( _dataBuffer, _dataStart, result, 0, getSize());

	return result;  // Return the result buffer.
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

	return _size;
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

	// Just set the byte in the data buffer.
	_dataBuffer[ _dataStart + position] = value;
    }
}