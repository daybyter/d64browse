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

import java.util.ArrayList;
import java.util.List;


/**
 * Class to hold info on a directory.
 */
public class DirectoryImpl implements Directory {

    // Inner classes


    // Static variables


    // Instance variables

    /**
     * The disk image, that this directory belongs to.
     */
    private DiskImage _diskImage;

    /**
     * A list of directory entries.
     */
    private List<DirectoryEntry> _entries = new ArrayList<DirectoryEntry>();


    // Constructors

    /**
     * Create a new directory for a given disk image.
     *
     * @param diskImage The disk image, this directory belongs to.
     */
    public DirectoryImpl( DiskImage diskImage) {

	_diskImage = diskImage;
    }


    // Methods

    /**
     * Add a new directory entry to this directory.
     *
     * @param entry The new directoy entry to add.
     */
    public void addDirectoryEntry( DirectoryEntry entry) {

	// Add the new entry to the list of entries.
	_entries.add( entry);
    }

    /**
     * Get the directoy entries.
     *
     * @return A list of directory entries.
     */
    public List<DirectoryEntry> getDirectoryEntries() {

	return _entries;
    }

    /**
     * Get the disk image, that this directory belongs to.
     *
     * @return The disk image, this directory belongs to.
     */
    public DiskImage getDiskImage() {

	return _diskImage;
    }

    /**
     * Remove all directory entries.
     */
    public void removeAllDirectoryEntries() {

	// Empty the list of directory entries.
	_entries.clear();
    }
}
