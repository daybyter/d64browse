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

import de.andreas_rueckert.d64browse.drive.format.ImageParser;
import de.andreas_rueckert.d64browse.drive.format.Sector;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Class to parse d64 files.
 */
public class D64ImageParser extends ImageParser {

    // Inner classes

    
    // Static variables


    // Instance variables


    // Constructors


    // Methods

    /**
     * Parse a file with a given filename.
     *
     * @param filename The name of the image file.
     *
     * @return A list of sectors representing the file.
     *
     * @throws IOException if the file cannot be parsed.
     */
    public static D64DiskImage parse( String filename) throws IOException {

	// Get a path to the file.
	Path path = Paths.get( filename);

	// Read all bytes from the files.
	byte [] data = Files.readAllBytes(path);

	// Check, if this is a 1541 image with 683 sectors.
	if( data.length != (683 * 256)) {

	    throw new IOException( "D64ImageParser.parse() : This is not a 683 sector 1541 image. Cannot parse other files at the moment.");
	}

	// Now generate the sectors for the file.
	int sectorsPerTrack = 21;
	int currentTrackIndex = 1;
	int currentSectorIndex = 0;
	int currentByteIndex = 0;
	List<Sector> resultBuffer = new ArrayList<Sector>();
	for( int sectorCount = 0; sectorCount < 683; ++sectorCount) {
	    
	    // Create a new sector and add it to the result.
	    resultBuffer.add( new D64Sector( data, currentByteIndex, currentTrackIndex, currentSectorIndex));

	    // Compute the new sector, track etc.
	    currentByteIndex += 256;
	    if( ++currentSectorIndex == sectorsPerTrack) {

		// Start a new track.                                             .
		currentSectorIndex = 0;
		++currentTrackIndex;

		// Adjust the sectors per track if necessary.
		if( currentTrackIndex == 18) { 

		    sectorsPerTrack = 19;

		} else if( currentTrackIndex == 25) {

		    sectorsPerTrack = 18;

		} else if( currentTrackIndex == 31) {

		    sectorsPerTrack = 17;
		}
	    }
	}

	// Now create an image from the parsed sectors and return it.
	return new D64DiskImage( filename, resultBuffer);
    }
}