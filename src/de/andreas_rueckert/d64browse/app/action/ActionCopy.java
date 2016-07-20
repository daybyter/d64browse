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

package de.andreas_rueckert.d64browse.app.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;


/**
 * Handle the copying of a file from or to a disk image.
 */
public class ActionCopy extends AbstractAction {

    // Inner classes


    // Static variables

    /**
     * The only instance of this action (singleton pattern).
     */
    private static ActionCopy _instance = null;


    // Instance variables


    // Constructors

    /**
     * Private constructor for singleton pattern.
     */
    private ActionCopy() {
        putValue( NAME, "Copy");
    }


    // Methods

    /**
     * The user wants to copy a file.
     *
     * @param e The action event.
     */
    public void actionPerformed( ActionEvent e) {

        // ...
    }

    /**
     * Get the only instance of this action.
     *
     * @return The only instance of this action.
     */
    public static ActionCopy getInstance() {

        if( _instance == null) {

            _instance = new ActionCopy();

        }

        return _instance;
    }
}