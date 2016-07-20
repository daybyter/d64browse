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

package de.andreas_rueckert.d64browse.drive.format.cpm.ui;

import de.andreas_rueckert.d64browse.drive.format.cpm.CPM4D64DiskImage;
import de.andreas_rueckert.d64browse.drive.format.d64.D64DiskImage;
import de.andreas_rueckert.d64browse.drive.format.d64.ui.D64DiskImagePanel;
import java.awt.GridLayout;
import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;
import javax.swing.JPanel;


/**
 * A panel to display and browse the d64 and the cp/m side of a hybrid filesystem.
 */
public class D64HybridDiskImagePanel extends JPanel {

    // Inner classes


    // Static variables


    // Instance variables

    /**
     * The panel to browse the cpm side of the disk image.
     */
    private CPM4D64DiskImagePanel _cpmPanel;

    /**
     * The panel to browse the d64 side of the disk image.
     */
    private D64DiskImagePanel _d64Panel;

    /**
     * The disk image, we currently browse.
     */
    private D64DiskImage _diskImage;


    // Constructors

    /**
     * Create a new cpm4d64 disk image panel.
     */
    public D64HybridDiskImagePanel( D64DiskImage diskImage) {

	// Store the parameters in the instance.
	_diskImage = diskImage;

	// Set a grid layout to display both panels on top of each other.
	setLayout( new GridLayout( 2, 1));

	// Add the 2 panels for the filesystems.
	add( _d64Panel = new D64DiskImagePanel( diskImage));
	add( _cpmPanel = new CPM4D64DiskImagePanel( new CPM4D64DiskImage( diskImage)));

	// Add a border to the panel.
	setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED));
    }


    // Methods
}