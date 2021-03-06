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

import de.andreas_rueckert.d64browse.app.action.ActionExit;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


/**
 * Menu bar for the browser app.
 */
class BrowserMenuBar extends JMenuBar {

    // Inner classes


    // Static variables

    /**
     * The only instance of this class => singleton pattern.
     */
    private static BrowserMenuBar _instance = null;


    // Instance variables


    // Constructors

    /**
     * Private constructor for singleton pattern.
     */
    private BrowserMenuBar() {

	JMenu currentMenu;

	add( currentMenu = new JMenu( "File"));
        currentMenu.add( new JMenuItem( ActionExit.getInstance()));

    }


    // Methods

    /**
     * Get the only instance of the menu bar.
     *
     * @return The only instance of the menu bar (singleton pattern).
     */
    public static BrowserMenuBar getInstance() {

        if( _instance == null) {
     
	    _instance = new BrowserMenuBar();
        }
        
        return _instance;
    }
}