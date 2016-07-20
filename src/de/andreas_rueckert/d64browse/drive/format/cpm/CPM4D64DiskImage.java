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

import de.andreas_rueckert.d64browse.drive.format.d64.D64DiskImage;
import de.andreas_rueckert.d64browse.drive.format.d64.D64Sector;
import de.andreas_rueckert.d64browse.drive.format.Directory;
import de.andreas_rueckert.d64browse.drive.format.DirectoryEntry;
import de.andreas_rueckert.d64browse.drive.format.DirectoryImpl;
import de.andreas_rueckert.d64browse.drive.format.DiskImageImpl;
import de.andreas_rueckert.d64browse.drive.format.Sector;
import de.andreas_rueckert.d64browse.util.CharsetUtils;
import de.andreas_rueckert.NotYetImplementedException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * This class holds the data for a CP/M disk image as an overlay 
 * of a d64 disk image.
 *
 * @see <a href="https://archive.org/stream/C64CP-MOperatingSystemUsersGuidePreliminary/C64_CP-M_Operating_System_Users_Guide_Preliminary_djvu.txt">c64 CP/M manual</a>
 */
public class CPM4D64DiskImage extends DiskImageImpl {

    // Inner classes

    
    // Static variables


    // Instance variables

    /**
     * The d64 image, that is cp/m image is part of.
     */
    private D64DiskImage _d64DiskImage;

    /**
     * The current directory.
     */
    private Directory _directory = null;

    /**
     * This is the list of free blocks. It is created from the existing files on the disks on the fly.
     */
    private List<Integer> _freeBlockMap = null;


    // Constructors

    /**
     * Create a new CP/M overlay on top of a d64 disk image.
     *
     * @param d64DiskImage The d64 disk image containing the CP/M image.
     */
    public CPM4D64DiskImage( D64DiskImage d64DiskImage) {

	// Create the cp/m sectors as an overlay on top of the d64 image.
	super( d64DiskImage.getFileName(), d64DiskImage.getSectors());
	_sectors = createCPMsectors( d64DiskImage);

	// Store a reference to the d64 image in this instance.
	_d64DiskImage = d64DiskImage;
    }


    // Methods

    /**
     * Remove a block from the list of free blocks.
     *
     * @param blockIndex The index of the block to allocate.
     */
    private void allocateBlock( int blockIndex) {
	
	// Remove this block from the list of free blocks.
	getFreeBlockMap().remove( new Integer( blockIndex));
    }

    /**
     * Create a cp/m filesystem on top of a d64 diskimage.
     *
     * @param d64DiskImage The d64 disk image.
     *
     * @return A list of cp/m sectors.
     */
    private List<Sector> createCPMsectors( D64DiskImage d64DiskImage) {

	// Create an empty list for the result.
	List<Sector> result = new ArrayList<Sector>();

	// Loop over the d64 sectors
	for( Sector currentD64Sector : d64DiskImage.getSectors()) {

	    // Don't create cp/m sectors for track 1 + 2
	    // and for track 18 (cbm directory there).
	    if( ( currentD64Sector.getTrackIndex() > 2)
		&& ( currentD64Sector.getTrackIndex() != 18)) {
		
		// If it is one of the first 17 sectors (0 .. 16)
		if( currentD64Sector.getSectorIndex() < 17) {
			
		    // Calculate the sector and track for the cp/m sectors.
		    int sector = 2 * currentD64Sector.getSectorIndex();
		    int track = currentD64Sector.getTrackIndex() < 18 
			? currentD64Sector.getTrackIndex() - 3 
			: currentD64Sector.getTrackIndex() - 4;
		    
		    // Create 2 cp/m sectors for each d64 sector.
		    result.add( new CPM4D64Sector( (D64Sector)currentD64Sector, 0, track, sector));
		    
		    // The second is for the second 128 bytes of the d64 sector.
		    result.add( new CPM4D64Sector( (D64Sector)currentD64Sector, 128, track, sector + 1));
		}
	    }
	    
	}

	// Return the created list of sectors.
	return result;
    }

    /**
     * Create a new extent for a given directory entry (this is most likely NOT a cp/m directory entry,
     * but the entry of a file to move or copy!
     *
     * @param extentIndex The index of the extent to create.
     * @param directoryEntry The directory of the file.
     */
    private void createExtent( int extentIndex, DirectoryEntry directoryEntry) {

	// Get the index of the new extent to create.
	int newExtentIndex = getFreeExtentIndex();

	// Get the sector data and the data offset of the new extent entry.
	Sector extentSector = _d64DiskImage.getSector( 3, newExtentIndex / 4);
	int dataOffset = ( newExtentIndex % 4) * 32;

	// Set the type of the extent to used by user 0.
	extentSector.setDataByte( dataOffset + 0, (byte)0);
	
	// Set the extent index of this extent.
	extentSector.setDataByte( dataOffset + 12, (byte)(( extentSector.getDataBytes()[ dataOffset + 12] & ( 255 - 31)) | ( extentIndex & 31)));
	extentSector.setDataByte( dataOffset + 14, (byte)( extentIndex / 32));  // Set the highbyte.

	// Set the name of the file.
	String filename = "";
	String suffix = "";
	if( directoryEntry.getFileName().indexOf(".") != -1) { // File has a suffix?

	    String [] filenameParts = directoryEntry.getFileName().split( ".");
	    filename = filenameParts[0];
	    suffix = filenameParts[1];
	} else {
	    filename = directoryEntry.getFileName();
	}

	// Limit the filename to 8 characters.
	if( filename.length() > 0) {
	    filename = filename.substring( 0, 8);
	}

	// Limit the suffix to 3 characters
	if( suffix.length() > 3) {
	    suffix = suffix.substring( 0, 3);
	}
	
	
	// Store the filename in the extent.
	int currentCharIndex = 1;
	for( char currentChar : filename.toCharArray()) {
	    extentSector.setDataByte( dataOffset + currentCharIndex++, CharsetUtils.getInstance().ascii2petscii( currentChar));
	}
	
	// Store the suffix in the extent.
	currentCharIndex = 9;
	for( char currentChar : suffix.toCharArray()) {
	    extentSector.setDataByte( dataOffset + currentCharIndex++, CharsetUtils.getInstance().ascii2petscii( currentChar));
	}

	throw new NotYetImplementedException( "Creating an extent is not yet implemented");

	// Store the file content in the blocks.
	//....

	// Remove the allocated blocks from the list of free blocks.
	//allocateBlocks(...);
    }
    
    /**
     * Get the underlying d64 disk image.
     *
     * @return The underlying d64 disk image.
     */
    public D64DiskImage getD64DiskImage() {

	return _d64DiskImage;
    }

    /**
     * Get the current directory.
     *
     * @return The current directory.
     */
    public Directory getDirectory() {

	if( _directory == null) {  // If the current directory was not parsed yet,

	    _directory = parseDirectory();  // parse it.
	}

	return _directory;
    }

    /**
     * Get a number of free blocks.
     *
     * @param numBlocks The number of required blocks.
     *
     * @return A list of free block, or null if there are not enough free blocks.
     */
    public List<Sector> getFreeSectors( int numBlocks) {

	if( getFreeBlockMap().size() < numBlocks) {  // If there are not enough free blocks...

	    return null; 
	}

	// Get the first <numBlocks> free blocks and return them.
	List<Integer> freeSectors = getFreeBlockMap().subList( 0, numBlocks);

	// Now convert the sector numbers to actual sectors...

	throw new NotYetImplementedException( "Getting free sectors is not yet implemented in CPM4D64DiskImage");
    }

    /**
     * Get the map of free blocks.
     *
     * @return The list of free blocks.
     */
    private List<Integer> getFreeBlockMap() {

	if( _freeBlockMap == null) {  // If the map was not created yet,

	    //...create it from the existing files.
	    throw new NotYetImplementedException( "Creating a freeBlockMap is not yet implemented");
	}

	return _freeBlockMap;
    }

    /**
     * Get the free disk space as bytes.
     *
     * @return The free disk space as bytes.
     */
    public long getFreeDiskSpace() {

	// The cp/m part of a d64 has 31.5 free tracks (32 - directory).
	// Each has 34 record with 128 bytes each.
	long result = 137088L;

	// Just subtract the sizes of all exisiting files from the total diskspace.
	for( DirectoryEntry currentEntry : getDirectory().getDirectoryEntries()) {

	    // Subtract the size of the current file.
	    result -= currentEntry.getFileSizeAsBytes();
	}

	return result; // Return the result.
    }
    
    /**
     * Get the index of the first free extent.
     *
     * @return The index of the first free extent or -1, if there are no free extents.
     */
    public int getFreeExtentIndex() {

	// The track and sector pos of the currency directory sector.
	int trackIndex = 3;  // CP/M dir starts at track 3 sector 0.
	int sectorIndex = 0;
	int currentExtentIndex = 0;  // The index of the current extent.

	// The directory goes from sector 0 to sector 7.
	while( sectorIndex < 8) {

	    // Get the data of the current sector.
	    byte [] sectorData = _d64DiskImage.getSector( trackIndex, sectorIndex).getDataBytes();

	    // Parse the entire sector for directory extents of 32 bytes each.
	    for( int currentDataStart = 0; currentDataStart < 128; currentDataStart += 32) {

		// Parse extent status. If it's 0xe5, this extent is not used.
		int extentStatus = sectorData[ currentDataStart] & 0xff;
		if( extentStatus == 0xe5) {

		    return currentExtentIndex;
		}

		++currentExtentIndex;
	    }

	    ++sectorIndex;  // Get the next sector.
	}

	return -1;
    }
		

    /**
     * Get an input stream for a file of this image.
     *
     * @param directoryEntry The directory entry.
     *
     * @return An input stream for the file represented by this directory entry.
     */
    public InputStream getInputStream( DirectoryEntry directoryEntry) {

	// Get all the blocks for the file.
	byte [] blocks = ((CPMDirectoryEntry)directoryEntry).getAllBlocks();

	// Count all the used blocks
	int usedBlocks = 0;
	for( byte currentBlock : blocks) {

	    // If the block number is not 0,
	    if( currentBlock != 0) {

		// the block is used.
		++usedBlocks;
	    }
	}

	// Replace a sector for every used block in the result.
	byte [] result = new byte[ usedBlocks * 8 * getUnitSize()];

	// A pointer to the result.
	int currentResultIndex = 0;

	// The number of tracks in this file system.
	int nTracks = getMaxTrackIndex() + 1;

	// Get the sectors for each block.
	for( byte currentBlock : blocks) {
	    
	    if( currentBlock != 0) {

		// System.out.println( "Adding block for input stream: " + currentBlock);

		for( int currentBlockSector = currentBlock * 8; currentBlockSector < currentBlock * 8 + 8; ++currentBlockSector) {

		    // Map the block sector to a track and sector index.
		    int trackIndex = currentBlockSector / nTracks;
		    int sectorIndex = currentBlockSector % nTracks;
		
		    // System.out.println( "Getting track / sector: " + trackIndex + " / " + sectorIndex);
		    
		    // Copy the bytes from the current sector to the result.
		    System.arraycopy( getSector( trackIndex, sectorIndex).getDataBytes(), 0, result, currentResultIndex, getUnitSize());
		    
		    // Increment the pointer to the next record.
		    currentResultIndex += getUnitSize();
		}
	    }
	}

	// Return a byte input stream of the result;
	return new ByteArrayInputStream( result);
    }

    /**
     * Write a new file to the disk image.
     *
     * @param directoryEntry The directory entry for the new file. Pass filename and filesize via this entry.
     *
     * @return An output stream to write to the image.
     */
    public OutputStream getOutputStream( DirectoryEntry directoryEntry) {

	// Check the filesize of the new file to calculate the number of required extents.
	int requiredBlocks = directoryEntry.getFileSizeAsBytes() / 1024;  // A block is 1kb on the c64.
	if( ( directoryEntry.getFileSizeAsBytes() % 1024) > 0) {
	    ++requiredBlocks;
	}
	int requiredExtents = requiredBlocks / 16;  // An extent can hold 16kb on the 1541 cp/m format.
	if( ( requiredBlocks % 16) > 0) {
	    ++requiredExtents;
	}

	// Create all the required extents.
	for( int currentExtent = 0; currentExtent < requiredExtents; ++currentExtent) {

	    // Create another extent.
	    createExtent( currentExtent, directoryEntry);
	}

	throw new NotYetImplementedException( "Writing to a cp/m image is not yet implemented");
    }

    /**
     * Parse the CP/M directory of this d64 image.
     *
     * @see <a href="http://manpages.ubuntu.com/manpages/intrepid/man5/cpm.5.html">CP/M directory structure</a>
     *
     * @return A directory object.
     */
    public Directory parseDirectory() {

	// Create a buffer for the result.
	CPMDirectory result = new CPMDirectory( this);

	// The track and sector pos of the currency directory sector.
	int trackIndex = 3;  // CP/M dir starts at track 3 sector 0.
	int sectorIndex = 0;

	// The directory goes from sector 0 to sector 7.
	while( sectorIndex < 8) {

	    // Get the data of the current sector.
	    byte [] sectorData = _d64DiskImage.getSector( trackIndex, sectorIndex).getDataBytes();

	    // Parse the entire sector for directory extents of 32 bytes each.
	    for( int currentDataStart = 0; currentDataStart < 128; currentDataStart += 32) {

		// Parse extent status. If it's 0xe5, this extent is not used.
		int extentStatus = sectorData[ currentDataStart] & 0xff;
		if( extentStatus != 0xe5) {
		
		    // Parse the file name of the extent.
		    StringBuffer filenameBuffer = new StringBuffer();
		    for( int currentCharIndex = 1; currentCharIndex < 9; ++currentCharIndex) {
		
			String currentCharacter = CharsetUtils.getInstance().petscii2ascii( (byte)( sectorData[ currentDataStart + currentCharIndex] & 127));
			if( ! currentCharacter.equals( " ")) {  // Trim filename.
			    filenameBuffer.append( currentCharacter);
			}
		    }
		    
		    filenameBuffer.append( ".");
		    
		    // Now parse the suffix.
		    for( int currentCharIndex = 9; currentCharIndex < 12; ++currentCharIndex) {
			
			String currentCharacter = CharsetUtils.getInstance().petscii2ascii( (byte)( sectorData[ currentDataStart + currentCharIndex] & 127));
			if( ! currentCharacter.equals( " ")) {  // Trim suffix.
			    filenameBuffer.append( currentCharacter);
			}
		    }
		    
		    // Compute the number of records, that this extent uses.
		    // For the correct formula see: http://www.seasip.demon.co.uk/Cpm/format22.html
		    int nrecords = sectorData[ currentDataStart + 15] & 127;

		    // Now compute the extent number of this extent.
		    int extentNumber = sectorData[ currentDataStart + 14] * 32 + ( sectorData[ currentDataStart + 12] & 31);

		    // Just show the filename for debugging purposes.
		    //System.out.println( "DEBUG: found cp/m file: " + filenameBuffer.toString());

		    // Now parse the block number, that this extent allocates.
		    byte [] blocks = new byte[ 16];
		    int currentBlock = 0;
		    for( int currentBlockIndex = 16; currentBlockIndex < 32; ++currentBlockIndex) {

			blocks[ currentBlock++]= sectorData[ currentDataStart + currentBlockIndex];
		    }

		    // Add the extent to the cp/m directory.
		    result.addExtent( new CPMDirectoryExtent( filenameBuffer.toString(), extentNumber, nrecords, blocks));
		    
		}
	    }

	    ++sectorIndex;  // Get the next sector.
	}

	// Now delete and recreate all directory entries.
	result.removeAllDirectoryEntries();  // Shouldn't be necessary, since the directory was just created, but jsut in case...
	result.createDirectoryEntriesFromExtents();  // Create new entries from extents.

	// Return the created directory.
	return result;
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
	
	return 128; // A cp/m record is 128 bytes.
    }
}
