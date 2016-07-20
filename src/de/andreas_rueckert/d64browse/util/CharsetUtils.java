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

package de.andreas_rueckert.d64browse.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to convert character sets.
 */
public class CharsetUtils {

    // Inner classes 


    // Static variables


    // Instance variables

    /**
     * A mapping from ascii to petscii.
     */
    Map< Character, Byte> _asciiPetsciiMapping = new HashMap< Character, Byte>();

    /**
     * The only instance of this class (singleton pattern).
     */
    private static CharsetUtils _instance = null;

    /**
     * Mapping from petscii to ascii.
     */
    List< String> _petsciiAsciiMapping = new ArrayList< String>();


    // Constructors

    /**
     * Create a new character set conversion instance.
     *
     * @see <a href="http://www.c64-wiki.com/index.php/PETSCII">Petscii charset</a>
     */
    private CharsetUtils() {

	// Init the petscii mapping.
	for( int i = 0; i < 256; ++i) {

	    _petsciiAsciiMapping.add( "<petscii char " + i + " >");
	}

	// Now set the known characters.
	_petsciiAsciiMapping.set( 32, " ");
	_petsciiAsciiMapping.set( 65, "A");
	_petsciiAsciiMapping.set( 66, "B");
	_petsciiAsciiMapping.set( 67, "C");
	_petsciiAsciiMapping.set( 68, "D");
	_petsciiAsciiMapping.set( 69, "E");
	_petsciiAsciiMapping.set( 70, "F");
	_petsciiAsciiMapping.set( 71, "G");
	_petsciiAsciiMapping.set( 72, "H");
	_petsciiAsciiMapping.set( 73, "I");
	_petsciiAsciiMapping.set( 74, "J");
	_petsciiAsciiMapping.set( 75, "K");
	_petsciiAsciiMapping.set( 76, "L");
	_petsciiAsciiMapping.set( 77, "M");
	_petsciiAsciiMapping.set( 78, "N");
	_petsciiAsciiMapping.set( 79, "O");
	_petsciiAsciiMapping.set( 80, "P");
	_petsciiAsciiMapping.set( 81, "Q");
	_petsciiAsciiMapping.set( 82, "R");
	_petsciiAsciiMapping.set( 83, "S");
	_petsciiAsciiMapping.set( 84, "T");
	_petsciiAsciiMapping.set( 85, "U");
	_petsciiAsciiMapping.set( 86, "V");
	_petsciiAsciiMapping.set( 87, "W");
	_petsciiAsciiMapping.set( 88, "X");
	_petsciiAsciiMapping.set( 89, "Y");
	_petsciiAsciiMapping.set( 90, "Z");
	_petsciiAsciiMapping.set( 160, " ");

	// Revert the mapping for ascii => petscii conversions.
	_asciiPetsciiMapping = revertMapping( _petsciiAsciiMapping);
    }


    // Methods

    /**
     * Convert a character into a petscii byte.
     *
     * @param character The character to convert.
     *
     * @return The character as a petscii character, if it exists in the mapping.
     */
    public byte ascii2petscii( char character) {

	// Get the mapped character from the ascii => petscii mapping.
	Byte mappedByte =  _asciiPetsciiMapping.get( new Character( character));
	
	if( mappedByte == null) {

	    return (byte)37;  // The '%' character as a placeholder.

	} else {

	    return mappedByte.byteValue();  // Returned the mapped character.
	}
    }

    /**
     * Get the only instance of this class (singleton pattern).
     *
     * @return The only instance of this class (singleton pattern).
     */
    public static CharsetUtils getInstance() {

	if( _instance == null) {  // If there is no instance yet,

	    _instance = new CharsetUtils();
	}

	return _instance;
    }

    /**
     * Convert a petscii byte to a java character/string.
     *
     * @param petsciiByte A petscii character as a byte.
     *
     * @return The java representation of this petscii character.
     */
    public String petscii2ascii( byte petsciiByte) {
	
	// Convert the signed byte to an int and remove the byte sign.
	return _petsciiAsciiMapping.get( petsciiByte & 0xFF);
    }	

    /**
     * Revert the petscii mapping to an ascii mapping.
     *
     * @param orgMapping The original mapping.
     *
     * @return The reverted mapping.
     */
    Map< Character, Byte> revertMapping( List<String> orgMapping) {

	// Create a mapping buffer for the result.
	Map< Character, Byte> result = new HashMap< Character, Byte>();

	// Loop over the original mapping.
	for( int i = 0; i < orgMapping.size(); ++i) {

	    // If this entry is not empty,
	    if( ! orgMapping.get( i).equals( "<petscii char " + i + " >")) {

		// map it to an entry in the reverted mapping (just the 1st character).
		result.put( orgMapping.get( i).charAt(0), new Byte( (byte)i));
	    }
	}

	return result;  // Return the reverted mapping.
    }
}