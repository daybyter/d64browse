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

package de.andreas_rueckert.d64browse.drive.format.d64;

import de.andreas_rueckert.d64browse.drive.format.DirectoryEntry;
import de.andreas_rueckert.d64browse.drive.format.Sector;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


/**
 * Class to write to a d64 disk image.
 */
class D64FileOutputStream extends OutputStream {
    
    // Inner classes


    // Static variables


    // Instance variables

    /**
     * The number of written bytes.
     */
    private int _bytesWritten = 0;

    /**
     * The directory entry of the new file.
     */
    DirectoryEntry _directoryEntry;

    /**
     * The disk image to write to.
     */
    D64DiskImage _targetImage;

    /**
     * The list of sectors to use.
     */
    private List<Sector> _usedSectors;


    // Constructors

    /**
     * Create a new output stream to write a file to a d64 image.
     *
     * @param usedSectors The list of sectors to use.
     * @param directoryEntry The directory entry of the file.
     * @param targetImage The disk image to write to.
     */
    public D64FileOutputStream( List<Sector> usedSectors
				, DirectoryEntry directoryEntry
				, D64DiskImage targetImage) {

	// Store the parameters in the instance.
	_usedSectors = usedSectors;
	_directoryEntry = directoryEntry;
	_targetImage = targetImage;
    }

    
    // Methods

    /**
     * Closes this output stream and releases any system resources associated with this stream.
     */
    public void close() {

	// Pad the rest of the last sector with 0.
	if( ( _bytesWritten % _targetImage.getUnitSize()) != 0) {
	    
	    Sector lastFileSector = _usedSectors.get( _bytesWritten / _targetImage.getUnitSize());
	    
	    // Loop over the rest of the sector.
	    for( int currentSectorOffset = _bytesWritten % _targetImage.getUnitSize(); currentSectorOffset < 256; ++currentSectorOffset) {

		lastFileSector.setDataByte( currentSectorOffset, (byte)0);
	    }
	}
    }

    /**
     * Flushes this output stream and forces any buffered output bytes to be written out.
     */
    public void flush() {
    }

    /**
     * Writes b.length bytes from the specified byte array to this output stream.
     *
     * @param b The byte array to write.
     */
    public void write( byte [] b) throws IOException {

	// See this just as a special case of the next write method.
	write( b, 0, b.length);
    }

    /**
     * Writes len bytes from the specified byte array starting at offset off to this output stream.
     *
     * @param b The byte array with the data.
     * @param off The offset to start with.
     * @param len The number of bytes to write.
     */
    public void write( byte [] b, int off, int len) throws IOException {

	// Just loop over the array.
	for( int currentByteCount = 0; currentByteCount < len; ++currentByteCount) {
	    
	    // Just use the single byte version of write to write to the file.
	    write( (int)b[ off++]);
	}
    }

    /**
     * Writes the specified byte to this output stream. The general contract for write is that one byte 
     * is written to the output stream. The byte to be written is the eight low-order bits of the argument b. 
     * The 24 high-order bits of b are ignored. 
     *
     * @param b The byte to write.
     */
    public void write( int b) throws IOException {

	// If the file was already completely written, throw an error.
	if( _bytesWritten >= _directoryEntry.getFileSizeAsBytes()) {

	    throw new IOException( "File " + _directoryEntry.getFileName() + " already completely written.");
	}

	// Compute sector and position in the sector.
	int sectorIndex = _bytesWritten / _targetImage.getUnitSize();
	int sectorOffset = _bytesWritten % _targetImage.getUnitSize();
	
	// Get the sector and write the byte to it. Leave the first 2 bytes of the sector for the link to the next sector.
	_usedSectors.get( sectorIndex).setDataByte( 2 + sectorOffset, (byte)( b & 0xff));

	++_bytesWritten;  // Count the written bytes.
    }
}
