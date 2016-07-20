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
import de.andreas_rueckert.d64browse.drive.format.DirectoryEntryImpl;


/**
 * This class holds the info on a directory entry of a d64 directory.
 */
public class D64DirectoryEntry extends DirectoryEntryImpl {

    // Inner classes 


    // Static variables

    
    // Instance variables

    /**
     * The name of the file type.
     */
    private String _fileTypeName;

    /**
     * The sector index of the first sector of the file.
     */
    private int _startSectorIndex = -1;

    /**
     * The track of the first sector of the file.
     */
    private int _startTrackIndex = -1;


    // Constructors

    /**
     * Create an entry for a (non-directory) file.
     *
     * @param filename The name of the file.
     * @param nativeFilesize The size of the file as blocks(!)
     * @param fileTypeName The name of the file type (i.e. PRG).
     * @param startTrackIndex The track index of the first sector of the file.
     * @param startSectorIndex The sector index of the first sector of the file.
     * @param directory The directory, this entry belongs to.
     */
    public D64DirectoryEntry( String filename
			      , int nativeFileSize
			      , String fileTypeName
			      , int startTrackIndex
			      , int startSectorIndex
			      , Directory directory) {

	// Store the values in the instance.
	super( filename, nativeFileSize, directory);

	// Store the type of the file (PRG, SEQ, etc).
	_fileTypeName = fileTypeName;

	// Store the position of the first sector of the file.
	_startTrackIndex = startTrackIndex;
	_startSectorIndex = startSectorIndex;
    }

    // Methods

    /**
     * Get the file name of a d64 file.
     *
     * @return The name of a d64 file.
     */
    public String getFileName() {

	// Just concat name and type...i.e. "PROG1" + "." + "PRG".
	return super.getFileName() + "." + getFileTypeName();
    }

    /**
     * Get the name of the file type.
     *
     * @return The name of the file type.
     */
    public String getFileTypeName() {

	return _fileTypeName;
    }

    /**
     * Get the sector index of the first file sector.
     *
     * @return The sector index of the first file sector.
     */
    public int getStartSector() {
	
	return _startSectorIndex;
    }
    
    /**
     * Get the track index of the first file sector.
     *
     * @return The track index of the first file sector.
     */
    public int getStartTrack() {

	return _startTrackIndex;
    }
}
