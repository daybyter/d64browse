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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * Interface to describe a disk image.
 */
public interface DiskImage {

    // Variables

    
    // Methods
    
    /**
     * Get the current root directory of this image.
     *
     * @return The current root directory.
     */
    public Directory getDirectory();

    /**
     * Get the filename of this disk image.
     *
     * @return The filename of this disk image.
     */
    public String getFileName();

    /**
     * Get the free disk space as bytes.
     *
     * @return The free disk space as bytes.
     */
    public long getFreeDiskSpace();

    /**
     * Get a given number of free sector (to store a new file).
     *
     * @param num The number of requested sectors or -1 if all free sectors should be returned.
     *
     * @return A list of free sectors or null, if there are not enough free sectors available.
     */
    public List<Sector> getFreeSectors( int num);

    /**
     * Get an input stream for a given directory entry.
     */
    public InputStream getInputStream( DirectoryEntry directoryEntry);
    
    /**
     * Get the maximum track index.
     *
     * @return The highest track number of the file system, or -1, if no sectors are available.
     */
    public int getMaxTrackIndex();

    /**
     * Get the minimum track number.
     *
     * @return The start track for the filesystem, or -1, if no sectors are available.
     */
    public int getMinTrackIndex();

    /**
     * Write a new file to the disk image.
     *
     * @param directoryEntry The directory entry for the new file. Pass filename and filesize via this entry.
     *
     * @return An output stream to write to the image.
     */
    public OutputStream getOutputStream( DirectoryEntry directoryEntry);

    /**
     * Get all the sectors of this image.
     *
     * @return The sectors of this image.
     */
    public List<Sector> getSectors();

    /**
     * Get the size of the image as bytes.
     *
     * @return The size of the disk image as bytes.
     */
    public int getSize();

    /**
     * Get the name of the native unit, this format uses.
     *
     * @return The name of native unit, this format uses.
     */
    public String getUnitName();

    /**
     * Get the size of this unit as bytes.
     *
     * @return The size of this unit as bytes.
     */
    public int getUnitSize();

    /**
     * Check, if this disk image was modified.
     *
     * @return true, if this disk image was modified. False otherwise.
     */
    public boolean isModified();

    /**
     * Set the modified flag of this disk image.
     *
     * @param modified The new value of the modified flag.
     */
    public void setModified( boolean modified);
}
