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

import de.andreas_rueckert.d64browse.drive.format.d64.D64DiskImage;
import de.andreas_rueckert.d64browse.drive.format.Directory;
import de.andreas_rueckert.d64browse.drive.format.DirectoryEntry;
import de.andreas_rueckert.d64browse.drive.format.DiskImageImpl;
import de.andreas_rueckert.d64browse.drive.format.Sector;
import de.andreas_rueckert.NotYetImplementedException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * This class holds the data for a cp/m 3.0 image based on a
 * d64 image. This format is used by the c128 running cp/m 3
 * with a 1541 floppy drive.
 */
public class CPM34D64DiskImage extends DiskImageImpl {

    // Inner classes
    

    // Static variables


    // Instance variables

    /**
     * The d64 image, that is cp/m image is part of.
     */
    private D64DiskImage _d64DiskImage;

    /**
     * Keep track 18 for c64 directory and a program
     * to start the cp/m.
     */
    private boolean _preserveTrack18 = false;


    // Constructors

    /**
     * Create a new CP/M overlay on top of a d64 disk image.
     *
     * @param d64DiskImage The d64 disk image containing the CP/M image.
     */
    public CPM34D64DiskImage( D64DiskImage d64DiskImage) {

	// Create the cp/m sectors as an overlay on top of the d64 image.
	super( d64DiskImage.getFileName(), d64DiskImage.getSectors());
	_sectors = createCPM3sectors( d64DiskImage);

	// Store a reference to the d64 image in this instance.
	_d64DiskImage = d64DiskImage;
    }

    
    // Methods

    /**
     * Create a cp/m 3.0 filesystem on top of a d64 diskimage.
     *
     * @param d64DiskImage The d64 disk image.
     *
     * @return A list of cp/m 3.0 sectors.
     */
    private List<Sector> createCPM3sectors( D64DiskImage d64DiskImage) {

	int maxsystrack = 2; // Reserve track 1 + 2 for the system.

	// Create an empty list for the result.
	List<Sector> result = new ArrayList<Sector>();

	// Create a buffer for the list of 4 sectors for 1 cpm sector.
	List<Sector> cpmSectorBuffer = new ArrayList<Sector>();

	// Loop over the d64 sectors
	for( Sector currentD64Sector : d64DiskImage.getSectors()) {

	    // Don't create cp/m sectors for track 1 - <maxsystrack>
	    // and for track 18 (cbm directory there), if preserveTrack18
	    // is set.
	    if( ( currentD64Sector.getTrackIndex() > maxsystrack)
		&& ( !_preserveTrack18 
		     || ( _preserveTrack18 && currentD64Sector.getTrackIndex() != 18))) {
		
		// Add this sector to the list of sectors for the next
		// cpm3 sector.
		cpmSectorBuffer.add( currentD64Sector);

		if( cpmSectorBuffer.size() == 4) { // Buffer is full?

		    // Calculate the sector and track for the cp/m sector?
		    int sector = 2 * currentD64Sector.getSectorIndex();
		    int track = currentD64Sector.getTrackIndex() < 18 
			? currentD64Sector.getTrackIndex() - 3 
			: currentD64Sector.getTrackIndex() - 4;
		    
		    // Create 1 cp/m sector for 4 d64 sectors.
		    result.add( new CPM34D64Sector( cpmSectorBuffer, track, sector));

		    // Create a new buffer for the next sector
		    cpmSectorBuffer = new ArrayList<Sector>();
		}
	    }
	}
	
	// Return the list of created cp/m 3 sectors.
	return result;
    }

    /**
     * Get the current directory.
     *
     * @return The current directory.
     */
    public Directory getDirectory() {
	throw new NotYetImplementedException( "Getting the directory is not yet implemented in CPM34D64DiskImage");
    }

    /**
     * Get the free disk space as bytes.
     *
     * @return The free disk space as bytes.
     */
    public long getFreeDiskSpace() {
	throw new NotYetImplementedException( "Getting the free diskspace is not yet implemented in CPM34D64DiskImage");
    }

    /**
     * Get a number of free blocks.
     *
     * @param numBlocks The number of required blocks.
     *
     * @return A list of free block, or null if there are not enough free blocks.
     */
    public List<Sector> getFreeSectors( int numBlocks) {
	throw new NotYetImplementedException( "Getting free sectors is not yet implemented in CPM34D64DiskImage");
    }

    /**
     * Get an input stream for a file of this image.
     *
     * @param directoryEntry The directory entry.
     *
     * @return An input stream for the file represented by this directory entry.
     */
    public InputStream getInputStream( DirectoryEntry directoryEntry) {
	throw new NotYetImplementedException( "Reading from a file is not yet implemented in CPM34D64DiskImage");
    }

    /**
     * Write a new file to the disk image.
     *
     * @param directoryEntry The directory entry for the new file. Pass filename and filesize via this entry.
     *
     * @return An output stream to write to the image.
     */
    public OutputStream getOutputStream( DirectoryEntry directoryEntry) {

	throw new NotYetImplementedException( "Writing to a new file is not yet implemented in CPM34D64DiskImage");
    }

    /**
     * Get the name of the native unit, this format uses.
     *
     * @return The name of native unit, this format uses.
     */
    public String getUnitName() {

	return "Record(s)";
    }

    /**
     * Get the size of this unit as bytes.
     *
     * @return The size of this unit as bytes.
     */
    public int getUnitSize() {
	return 1024;
    }
}
