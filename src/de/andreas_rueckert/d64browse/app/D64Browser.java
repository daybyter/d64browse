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

package de.andreas_rueckert.d64browse.app;

import de.andreas_rueckert.d64browse.drive.format.cpm.ui.D64HybridDiskImagePanel;
import de.andreas_rueckert.d64browse.drive.format.d64.D64DiskImage;
import de.andreas_rueckert.d64browse.drive.format.d64.D64ImageParser;
import de.andreas_rueckert.d64browse.drive.format.filesystem.ui.FileSystemPanel;
import de.andreas_rueckert.d64browse.drive.format.DiskImageAnalyzer;
import de.andreas_rueckert.d64browse.drive.format.DiskImageType;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;


/**
 * Main class of the d64 browser.
 */
class D64Browser {

    // Inner classes


    // Static variables


    // Instance variables
    
    /**
     * The app instance.
     */
    private static D64Browser _app = null;

    /**
     * The main frame for the app.
     */
    private static JFrame _mainFrame;


    // Constructors

    /**
     * Create a new app instance.
     *
     * @args The commandline arguments.
     */
    public D64Browser( String [] args) {

	// Always create an UI at the moment.
	// Might be left out in script mode.
	createUI();

	// Parse the commandline parameters.
	parseCommandline( args);
    }


    // Methods

    /**
     * Create the user interface of the app.
     */
    private void createUI() {

	_mainFrame = new JFrame( "d64 browser");
	_mainFrame.setJMenuBar( BrowserMenuBar.getInstance());

	_mainFrame.getContentPane().add( new FileSystemPanel( new java.io.File( "." )));

        _mainFrame.pack();
        _mainFrame.setVisible(true);
    }

    /**
     * The main method to create the application.
     *
     * @param args The commandline arguments of the application.
     */
    public static void main( String [] args) {

	_app = new D64Browser( args);
    }

    /**
     * Parse the command line parameters.
     *
     * @param parameters The command line parameters.
     */
    private void parseCommandline( String [] parameters) {

        for( String currentParameter : parameters) {

	    // Check, if this parameter is a switch.
	    if( currentParameter.startsWith( "-")) {

	    } else {

		// Try to create a file from the parameter.
		File fileParameter = new File( currentParameter); 

		// Check, if this is a file of a known image type.
		DiskImageType guessedType = DiskImageAnalyzer.guessFormat( fileParameter);

		if( guessedType == DiskImageType.D64) {

		    // Just try to parse the directory for now.
		    // System.out.println( "Format is d64");

		    try {
			D64DiskImage currentImage = D64ImageParser.parse( currentParameter);

			// Display a file system browser for the image.
			_mainFrame.getContentPane().setLayout( new GridLayout( 1, 2));
			_mainFrame.getContentPane().add( new D64HybridDiskImagePanel( currentImage));
			_mainFrame.pack();

		    } catch( IOException ioe) {

			System.err.println( "Error parsing image '" + currentParameter + "' :" + ioe);
		    }
		}
	    }
        }
    }
}
