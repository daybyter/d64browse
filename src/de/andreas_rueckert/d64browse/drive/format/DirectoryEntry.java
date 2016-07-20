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

import java.awt.datatransfer.Transferable;


/**
 * Interface for a directory entry.
 *
 * @see <a href="http://www.javaworld.com/article/2076656/java-se/unfurling-java-s-data-transfer-api.html">DnD explained</a>
 */
public interface DirectoryEntry extends Transferable {

    // Variables


    // Methods

    /**
     * Get the directory, that this entry belongs to.
     *
     * @return The directory, that this entry belongs to.
     */
    public Directory getDirectory();

    /**
     * Get the name of the file, that this entry represents.
     *
     * @return The name of the file, that this entry represents.
     */
    public String getFileName();

    /**
     * Get the size of the file, that this entry represents as bytes.
     *
     * @return the size of the file, that this entry represents as bytes.
     */ 
    public int getFileSizeAsBytes();

    /**
     * Get the size of the file, that this entry represents as native units.
     *
     * @return the size of the file, that this entry represents as native units.
     */ 
    public int getFileSizeAsUnits();

    /**
     * Check, if this entry is a directory.
     *
     * @return true, if this entry is a directory. False otherwise.
     */
    public boolean isDirectory();
}
