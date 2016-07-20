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

import de.andreas_rueckert.d64browse.drive.format.Directory;
import de.andreas_rueckert.d64browse.drive.format.DirectoryEntry;
import de.andreas_rueckert.d64browse.drive.format.DirectoryEntryImpl;
import de.andreas_rueckert.d64browse.drive.format.DirectoryImpl;
import de.andreas_rueckert.d64browse.drive.format.DiskImageImpl;
import de.andreas_rueckert.d64browse.drive.format.Sector;
import de.andreas_rueckert.d64browse.util.CharsetUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Instance of this class hold the data of a disk image.
 *
 * @see http://unusedino.de/ec64/technical/formats/d64.html
 */
public class D64DiskImage extends DiskImageImpl {

    // Inner classes

    
    // Static variables


    // Instance variables

    /**
     * The current root directory.
     */
    private Directory _directory = null;


    // Constructors

    /**
     * Create a new image from a list of sectors.
     *
     * @param filename The filename of the image.
     * @param sectorList The list of sectors.
     */
    public D64DiskImage( String filename, List<Sector> sectorList) {

	// Use the constructor of the base class.
	super( filename, sectorList);
    }
    

    // Methods

    /**
     * Create a new directory entry in the d64.
     *
     * @param directoryEntry The directory entry of the file to add.
     * @param usedSectors The sectors, that the new file will use on the d64.
     */
    private void createD64DirectoryEntry( DirectoryEntry directoryEntry, List<Sector> usedSectors) {

	// Get the position of the new directory entry to create.
	// Just count the existing known directory entries for now.
	int newEntryPos = getDirectory().getDirectoryEntries().size();

	// A d64 directory sector has 8 entries per sector, so calculate the sector and offset.
	int entrySector = 1 + newEntryPos / 8;
	int entryOffset = 32 * ( newEntryPos % 8);

	// Fetch the sector with the directory entries.
	Sector directorySector = getSector( 18, entrySector);  // Sector 0 contains the BAM.

	// Check, if the filename is C= compliant.
	if( ! directoryEntry.getFileName().endsWith( ".PRG")) {

	    System.err.println( "Can only store .PRG files for now");

	    return;
	}

	// Remove the suffix of the filename.
	String filename = directoryEntry.getFileName();
	filename = filename.substring( 0, filename.indexOf( ".PRG"));
	if( filename.length() > 16) {  // Cut the filename to 16 characters.
	    filename = filename.substring( 0, 16);
	}

	// Now set the data of the entry.
	directorySector.setDataByte( entryOffset + 2, (byte)0x82);  // Set type to .prg
	
	// Copy the name into the directory.
	int currentCharIndex = 5;
	for( char currentChar : filename.toCharArray()) {

	    directorySector.setDataByte( currentCharIndex++, CharsetUtils.getInstance().ascii2petscii( currentChar));
	}

	// Pad the file with 0xa0, if it is too short.
	while( currentCharIndex < 20) {
	    directorySector.setDataByte( currentCharIndex++, (byte)0xa0);
	}
	 
	// Set the position of the first sector.
	directorySector.setDataByte( 3, (byte)( usedSectors.get( 0).getTrackIndex()));
	directorySector.setDataByte( 4, (byte)( usedSectors.get( 0).getSectorIndex()));

	// Set the length of the file.
	directorySector.setDataByte( 0x1e, (byte)( usedSectors.size() % 256));
	directorySector.setDataByte( 0x1f, (byte)( usedSectors.size() / 256));
    }

    /**
     * Get the current root directory of this image.
     *
     * @return The current root directory.
     */
    public Directory getDirectory() {

	if( _directory == null) {  // If the current directory was not already parsed,
 
 	    _directory = parseDirectory();   // parse it.
	}

	return _directory;  // Return the current root directory.
    }

    /**
     * Get the free disk space as bytes.
     *
     * @return The free disk space as bytes.
     */
    public long getFreeDiskSpace() {

	// Get the data of the BAM.
	byte [] bamData = getSector( 18, 0).getDataBytes();

	long result = 0L;  // Buffer for the result.

	// Just count the set bits from 0x04 to 0x8f.
	for( int currentByteIndex = 0x04; currentByteIndex <= 0x8f; ++currentByteIndex) {
	    
	    // Get the current byte from the BAM.
	    byte currentBamByte = bamData[ currentByteIndex];

	    // Count the set bits in the current byte.
	    for( int currentBit = 1; currentBit <= 0x80; currentBit <<= 1) {

		if( ( currentBamByte & currentBit) != 0) {  // Is this block available?

		    // Yes => add this free sector to the result;
		    result += getUnitSize();
		}
	    }
	}

	return result; // Return the result.
    }

    /**
     * Get a given number of free sector (to store a new file).
     *
     * @param num The number of requested sectors or -1 if all free sectors should be returned.
     *
     * @return A list of free sectors or null, if there are not enough free sectors available.
     */
    public List<Sector> getFreeSectors( int num) {

	// Get the data of the BAM.
	byte [] bamData = getSector( 18, 0).getDataBytes();

	List<Sector> result = new ArrayList<Sector>();  // Buffer for the result.

	// If the user requested 0 sectors, just return the empty list.
	if( num == 0) {

	    return result;

	}

	// Just count the set bits from 0x04 to 0x8f.
	int currentBlockIndex = 0;
	for( int currentByteIndex = 0x04; currentByteIndex <= 0x8f; ++currentByteIndex) {
	    
	    // Get the current byte from the BAM.
	    byte currentBamByte = bamData[ currentByteIndex];

	    // Count the set bits in the current byte.
	    for( int currentBit = 1; currentBit <= 0x80; currentBit <<= 1) {

		if( ( currentBamByte & currentBit) != 0) {  // Is this block available?

		    // Yes => add this free sector to the result;
		    Sector nextFreeSector = getSectorForBlockIndex( currentBlockIndex++);

		    // If this sector exists, add it to the result.
		    if( nextFreeSector != null) {

			result.add( nextFreeSector);
		    }

		    // If the number of free sectors is sufficient, return the result.
		    if( result.size() == num) {

			return result;
		    }
		}
	    }
	}	

	return null;  // Not enough free sectors found.
    }

    /**
     * Get an input stream to read a file from the image.
     *
     * @param directoryEntry The directory entry of the file.
     *
     * @return An input stream to read file from the image.
     */
    public InputStream getInputStream( DirectoryEntry directoryEntry) {

	// If the file has a 0 size, just return null for now.
	if( directoryEntry.getFileSizeAsBytes() == 0) {
	    return null;
	}

	// A pointer to the current sector.
	D64Sector sectorBuffer;

	// Create a byte buffer for the file content.
	byte [] fileContent = new byte[ directoryEntry.getFileSizeAsBytes()];

	// Get the first sector of the file and add it to the result.
	System.arraycopy( ( sectorBuffer = (D64Sector)getSector( ((D64DirectoryEntry)directoryEntry).getStartTrack()
						      , ((D64DirectoryEntry)directoryEntry).getStartSector())).getDataBytes()
			  , 2
			  , fileContent
			  , 0
			  , getUnitSize());

	// Loop over the sectors of the file and add them to the buffer.
	int currentFileContentIndex = getUnitSize();
	for( int currentSectorIndex = 1; currentSectorIndex < directoryEntry.getFileSizeAsUnits(); ++currentSectorIndex) {
	    
	    // Get the next track and sector from the current sector.
	    int nextTrack = sectorBuffer.getNextTrack();
	    int nextSector = sectorBuffer.getNextSector();

	    // Add the next sector to the result.
	    System.arraycopy( ( sectorBuffer = (D64Sector)getSector( nextTrack, nextSector)).getDataBytes()
			      , 2
			      , fileContent
			      , currentFileContentIndex
			      , getUnitSize());

	    // Point to the next block in the result buffer.
	    currentFileContentIndex += getUnitSize();
	}

	// Now create an input stream from the file content.
	return new ByteArrayInputStream( fileContent);
    }

    /**
     * Write a new file to the disk image.
     *
     * @param directoryEntry The directory entry for the new file. Pass filename and filesize via this entry.
     *
     * @return An output stream to write to the image.
     */
    public OutputStream getOutputStream( DirectoryEntry directoryEntry) {

	// Check, if this directory entry is a directory itself.
	if( directoryEntry.isDirectory()) {

	    return null;  // Cannot create subdirectories yet.
	}

	// Start with a check, if there are still free directory entries to store a new file.
	if( getDirectory().getDirectoryEntries().size() >= 144) {

	    return null;  // No free directory entries, it seems.
	}

	// Now try get free filespace for the new file.
	int neededSectors = directoryEntry.getFileSizeAsBytes() / getUnitSize();
	if( ( neededSectors * getUnitSize()) < directoryEntry.getFileSizeAsBytes()) {  // If there are some bytes left for another sector.

	    ++neededSectors;  // Add them to another sector (plus some empty bytes for padding.
	}

	// Now try to allocate enough free sectors for the file.
	List<Sector> availableSectors = getFreeSectors( neededSectors);

	if( availableSectors == null) {  // Sectors cannot be allocated?

	    return null;  // => Cannot write file.
	}

	// Create directory entry for the file and the sectors to allocate.
	createD64DirectoryEntry( directoryEntry, availableSectors);

	// Link all the sectors of this file.
	Sector lastSector = null;
	for( Sector currentSector : availableSectors) {
	    
	    if( lastSector != null) {  // If this is not the first sector.

		// Set the pointer of the last sector to this sector.
		lastSector.setDataByte( 0, (byte)( currentSector.getTrackIndex()));
		lastSector.setDataByte( 1, (byte)( currentSector.getSectorIndex()));
	    }

	    // Set this sector as the new last sector.
	    lastSector = currentSector;
	}
	// Set the pointer of the last file sector to 0. Shouldn't be necessary, but anyway.
	lastSector.setDataByte( 0, (byte)0);
	lastSector.setDataByte( 1, (byte)0);
	
	// Set the image to modified.
	setModified( true);
	
	// Return OutputStream for file to write.
	return new D64FileOutputStream( availableSectors, directoryEntry, this);
    }


    /**
     * Get the size of the image as bytes.
     *
     * @return The size of the disk image as bytes.
     */
    public int getSize() {

	return getSectors().size() * getUnitSize();  // 254 byte sectors
    }

    /**
     * Get the name of the native unit, this format uses.
     *
     * @return The name of native unit, this format uses.
     */
    public String getUnitName() {

	return "Block(s)";
    }

    /**
     * Get the size of this unit as bytes.
     *
     * @return The size of this unit as bytes.
     */
    public int getUnitSize() {
	
	return 254; // A d64 block is 254 bytes (256 - pointer to the next sector).
    }

    /**
     * Parse the directory of this d64 image.
     *
     * @return A directory object.
     */
    public Directory parseDirectory() {

	// Create a buffer for the result.
	Directory result = new DirectoryImpl( this);

	// The track and sector pos of the currency directory sector.
	int trackIndex = 18;
	int sectorIndex = 1;

	// The directory starts at track 18, sector 1 and normally continues to sector 18.
	while( ( trackIndex != 0) && (sectorIndex != 0)) {

	    // Get the data of the current sector.
	    byte [] sectorData = getSector( trackIndex, sectorIndex).getDataBytes();

	    // Parse the directory entries in this sector.
	    for( int currentEntry = 0; currentEntry < 8; ++currentEntry) {

		// Calculate the byte, where the directory entry starts.
		int entryOffset = 32 * currentEntry;

		// Check, if this entry is a deleted file.
		if( sectorData[ entryOffset + 2] == 0) {  

		    continue;  // This entry is deleted.
		}

		// Get the name of the filename.
		StringBuffer filenameBuffer = new StringBuffer();

		for( int filenameIndex = 5; filenameIndex <= 14; ++filenameIndex) {

		    // Get the current byte of the filename.
		    byte currentFilenameChar = sectorData[ entryOffset + filenameIndex];

		    if( currentFilenameChar == 0xa0) {  // This is a padding byte. Filename is complete.
			
			break;

		    } else {  // Convert this petscii char and add it to the filename.
			
			filenameBuffer.append( CharsetUtils.getInstance().petscii2ascii( currentFilenameChar));
		    }
		}

		// Get the type of the file.
		byte currentFileType = (byte)( sectorData[ entryOffset + 2] & (byte)7);
		String currentFileTypeName = "";

		switch( currentFileType) {
		case 1: currentFileTypeName = "SEQ"; break;
		case 2: currentFileTypeName = "PRG"; break;
                case 3: currentFileTypeName = "USR"; break;
		case 4: currentFileTypeName = "REL"; break;
		}

		// Try to approximate the filesize. The c64 only stores blocks of 254 bytes and not 
		// the actual filesize in bytes.
		int filesize = sectorData[ entryOffset + 0x1e] + ( 256 * sectorData[ entryOffset + 0x1f]);
		
		// Create a directory entry and add it to the result.
		result.addDirectoryEntry( new D64DirectoryEntry( filenameBuffer.toString()
								 , filesize
								 , currentFileTypeName
								 , sectorData[ entryOffset + 3]  // Track index of the first sector.
								 , sectorData[ entryOffset + 4]  // Sector index of the first sector.
								 , result));
	    }

	    // Get the track and sector of the next directory sector from the current sector;
	    trackIndex = sectorData[ 0];
	    sectorIndex = sectorData[ 1];
	}

	// Return the parsed directory.
	return result;
    }

    /**
     * Translate a block index to a sector.
     *
     * @param blockIndex The index of the block (0 .. 682 for 35 tracks).
     *
     * @return The sector with the track/sector combination for this block index, if it exists. null otherwise.
     */
    private Sector getSectorForBlockIndex( int blockIndex) {

	// The block index of the first sector of each track.
	int [] trackStart = {    0,  21,  42,  63,  84, 105, 126, 147
			     , 168, 189, 210, 231, 252, 273, 294, 315
			     , 336, 357, 376, 395, 414, 433, 452, 471
			     , 490, 508, 526, 544, 562, 580, 598, 615
			     , 632, 649, 666, 683, 700, 717, 734, 751
			     , 768 };
	int currentTrackIndex = 0;
	for( int currentTrackStart : trackStart) {
	    
	    if( blockIndex < currentTrackStart) {
		break;
	    }

	    ++currentTrackIndex;  // Check the next track
	}

	// Check, if the track is within a reasonable range.
	if( ( currentTrackIndex < 1) || ( currentTrackIndex > 40)) {
	    
	    return null;  // Ignore tracks > 40 for now.
	}

	// Calculate the sector offset.
	int sectorIndex = blockIndex - trackStart[ currentTrackIndex - 1];
	
	// Try to find the sector with the given track and sector index in this image.
	return getSector( currentTrackIndex, sectorIndex);
    }
}
