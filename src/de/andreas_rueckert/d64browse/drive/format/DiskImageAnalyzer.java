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

import java.io.File;


/**
 * Class to check disk images for format.
 */
public class DiskImageAnalyzer {

    // Inner classes


    // Static variables


    // Instance variables


    // Constructors


    // Methods

    /**
     * Try to guess the format of some given image.
     *
     * @param file The given disk image as a file.
     *
     * @return The best guess for an image.
     */
    public static DiskImageType guessFormat( File file) {

	// For now, I just check the length of the file and the name. Should be improved!
	if( file.getName().endsWith( ".d64")) {

	    if( file.length() == ( 683 * 256)) {  // 35 track d64 image ?

		return DiskImageType.D64;

	    } else if( file.length() == ( 768 * 256)) {  // 40 track d64 image ?

		return DiskImageType.D64_40;

	    }
	}

	// Default is, that no image format is recognized.
	return DiskImageType.UNKNOWN;
    }
}