/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.example.time;
/*
 * Import required Java packages.
 */
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.joda.time.DateTime;

/** DateTimeBrowser is a Java Swing application which reads a file containing
 * strings and displays DateTime values in a JTable.<p>
 * The input strings must be suitable for instantiation
 * of DateTime objects.  The file is read, and an attempt is made
 * to instantiate a DateTimeObject from the input string on each file
 * line.<p>
 * Comments (beginning with '#') and blank lines may appear in
 * the file.<p>
 * Error messages may result from invalid input.<p>
 * Values calculated from any resulting DateTime objects are placed
 * in a JTable and displayed in a JFrame.<p>
 *
 * @author Guy Allard
 * @version 1.0
 */
public class DateTimeBrowser extends JFrame {
    //
    private String[] mainArgs = null;           // Copy of args[] reference.
    //
    private LoadedFile currFile = null;         // New ones possible at
                                                // runtime.
    private JScrollPane mainSP = null;          // Swapped around at runtime
    //
    /**
     * The getter view  menu item.
     */
    JMenuItem jmiGetter = null;
    /**
     * The hexadecimal view menu item.
     */
    JMenuItem jmiHex = null;
    /**
     * The Java Date view menu item.
     */
    JMenuItem jmiDate = null;
    /**
     * The java calendar menu item.
     */
    JMenuItem jmiCal = null;
    //
    // Done deals.
    //
    private final JFileChooser chooser = new JFileChooser();
    private final boolean debugf = false;       // debugging flag
    private final boolean debugt = true;        // debugging flag

    /**
     * This is the main swing application method.   It sets up and displays the
     * initial GUI, and controls execution thereafter.  Everything else in
     * this class is 'private', please read the code.
     */
    public static void main(String[] args) {
        /*
         * Developers Notes:
         *
         * -No corresponding Junit test class currently
         * provided.  Test by eyeball of the output.
         *
         * -Add a menu with Help(About)
         * --> TBD.
         *
         * -Figure out some sane way to set initial default
         * column sizes.
         *
         * -Lots of inner classes here, done in order to keep
         * all the .class files easily identifiable.  Some of
         * this code is pretty ugly, very procedural in nature.
         * Lots of very tight coupling between all the classes,
         * thinly disguised switch statements, etc ..... This
         * code written on the fly, with almost no thought given
         * to OO design.
         *
         * -Also, I'm not really a GUI guy, so forgive any
         * transgressions.
         *
         */
            if ( args.length < 1 ) {
                System.err.println("File name is required!");
                usage();
                System.exit(1);
            }
        /*
         * Instantiate a DateTimeBrowser and invoke it's go method,
         * passing the input argument list.
         */
        new DateTimeBrowser().go( args );
    } // main

    /*
     * usage A private static method to display usage information to
     * the user before an error exit.
     */
    private static void usage() {
            System.err.println("Usage:");
            System.err.print("java <options> ");
            System.err.print(DateTimeBrowser.class.getName());
            System.err.println(" <filename>");
            System.err.println("<filename> contains a list of Strings");
            System.err.println("\twhich are valid for DateTime instantiation.");
            System.err.println("<optons>");
            System.err.println("\t-Duse.time.zone=");
            System.err.println("\t\tA valid timezone name.  If not specified");
            System.err.println("\t\tthe OS/user default is used.  If sepcified");
            System.err.println("\t\tincorrectly, GMT is quietly used.");
            System.err.println("\t-Duse.view=");
            System.err.println("\t\tAn initial view to be displayed.");
            System.err.println("\t\tValid names are: getter, hex, date, cal");
            System.err.println("\t\tIf incorrectly specified, getter is used.");
            System.err.println("\t\tThis becomes the default view.");
    } // usage

    /*
     * go This method reads the file, creates the table to display,
     * the window to display it in, and displays the window.
     * @param fileName the name of the file to read.
     * @param tryLines An estimate of the number of lines in
     * the file.
     */
    private void go(String[] args) {

        mainArgs = args;
        setDefaultTimeZone();   // let user override if needed
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar( menuBar );
        addMenus( menuBar );
        /*
         * Add a fast close listener
         */

        addWindowListener( new WindowAdapter() {
                    public void windowClosing(WindowEvent e)
                    {
                        setVisible( false );
                        dispose();
                        System.exit(0);
                    }
                }
            );

        //
        // Load current file, prime tables and JFrame.
        //
        currFile = new LoadedFile( mainArgs[0] );
        TableView tView = getDefaultTableView();
        resetDefaults( tView );
        //
        // Set max size at start, and display the window.
        //
        Dimension screenMax = Toolkit.getDefaultToolkit().getScreenSize();
        setSize ( screenMax );
        setVisible(true);
    }

    //
    // --> Private implementation methods follow.
    //

    /*
     * getDefaultTableView
     */
    private TableView getDefaultTableView() {
        // No user input.
        String viewStr = System.getProperty("use.view");
        if ( viewStr == null ) {
            jmiGetter.setEnabled( false );
            return new GetterTableView( currFile );
        }
        // Valid user input.
        if ( viewStr.equalsIgnoreCase("hex") ) {
            jmiHex.setEnabled( false );
            return new HexTableView( currFile );
        }
        else if ( viewStr.equalsIgnoreCase("date") ) {
            jmiDate.setEnabled( false );
            return new DateTableView( currFile );
        }
        else if ( viewStr.equalsIgnoreCase("cal") ) {
            jmiCal.setEnabled( false );
            return new CalTableView( currFile );
        }
        else if ( viewStr.equalsIgnoreCase("getter") ) {
            jmiGetter.setEnabled( false );
            return new GetterTableView( currFile );
        }
        else { // error by user
            System.err.println("View name: " + viewStr + " invalid.");
            jmiGetter.setEnabled( false );
            return new GetterTableView( currFile );
        }
    }
    /*
    * setDefaultTableView
    */
    private void setDefaultTableView(String newView) {
        System.setProperty( "use.view", newView );
    }
    /*
     * setDefaultTimeZone
     */
    private void setDefaultTimeZone() {
        String tzName = System.getProperty("use.time.zone");
        if ( tzName == null ) return;   // Use OS/user default.
        //
        // If tzName is bogus, not understood by the JRE,
        // 'getTimeZone' returns GMT.
        //
        TimeZone toSet = TimeZone.getTimeZone( tzName );
        //
        // Set default to whatever was returned.
        //
        TimeZone.setDefault( toSet );
    }

    /*
     * addMenus
     */
    private void addMenus( JMenuBar menuBar) {
        //
        // Create all the menus.
        //
        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        //
        // Add them to the menubar in order.
        //
        menuBar.add( fileMenu );
        menuBar.add( viewMenu );
        //
        // Create action objects and menu items.
        //
        Action open = new OpenAction();
        JMenuItem jmiOpen = new JMenuItem( open );
        Action exit = new ExitAction();
        JMenuItem jmiExit = new JMenuItem( exit );
        //
        // Next Menu
        //
        Action getter = new GetterAction();
        jmiGetter = new JMenuItem( getter );
        getter.setEnabled( true );
        //
        Action hex = new HexAction();
        jmiHex = new JMenuItem( hex );
        hex.setEnabled( true );
        //
        Action date = new DateAction();
        jmiDate = new JMenuItem( date );
        date.setEnabled( true );
        //
        Action cal = new CalAction();
        jmiCal = new JMenuItem( cal );
        cal.setEnabled( true );
        //
        // Build the file menu.
        //
        fileMenu.add( jmiOpen );
        fileMenu.addSeparator();
        fileMenu.add( jmiExit );
        //
        // Build the view menu.
        //
        viewMenu.add( jmiGetter );
        viewMenu.add( jmiHex );
        viewMenu.add( jmiDate );
        viewMenu.add( jmiCal );
        //
        // *temp Developer's code
        //
        // jmiGetter.setEnabled( false );
        //
        // JMenuItem getter2 = new JMenuItem( "getter2" );
        // getter2.addActionListener( new myMouseListener() );
        // viewMenu.add( getter2 );
    } // end of addMenus

    /*
     * A private method to dump the arrays of Object[][]
     * if desired by the developer
     * @param objs The array of arrays to be dumped.
     */
    private void dumpObjs(Object[][] objs, PrintStream out ) {
        for (int i = 0; i < objs.length; ++i) {
            for (int j = 0; j < objs[i].length; ++j) {
                out.println(i + " " + j + " "
                    + objs[i][j]);
            } // for j
        } // for i
    }

    /*
     * enableAll
     */
    private void enableAllViews() {
        jmiGetter.setEnabled( true );
        jmiHex.setEnabled( true );
        jmiDate.setEnabled( true );
        jmiCal.setEnabled( true );
    } // end of enableAllViews

    /*
     * getADate Returns a new DateTime object reference if possible,
     * otherwise null.
     * @return retDT A DateTime object reference.
     */
    private DateTime getADate(String s) {
        DateTime retDT = null;
        try
        {
            retDT = new DateTime( s );
        } // the try
        catch(IllegalArgumentException pe)
        {
            // ignore it here, caller sees null
        } // the catch
        return retDT;
    } // getADate
    //
    private static final String PADCHARS = "00000000000000000000000000000000";

    /*
     * LPad Return a String, left padded with '0's as specified
     * by the caller.
     */
    private String LPad(String inStr, int maxLen) {
        if (inStr.length() >= maxLen) return inStr.toUpperCase();
        String zeroes = PADCHARS.substring(0, maxLen - inStr.length());
        String retVal = zeroes + inStr;
        return retVal.toUpperCase();
    }

    /*
     * resetDefaults
     */
    private void resetDefaults( TableView tView ) {
        Object[] colNames = tView.getColNames();
        Object[][] tableValues = tView.getCalcdValues();
        // dumpObjs( tableValues, System.out);
        JTable table = new JTable( tableValues, colNames );
        tView.setViewColumnsWidth( table );
        setTitle( tView.getViewTitle() );
        //
        if ( mainSP != null ) getContentPane().remove( mainSP );
        mainSP = new JScrollPane( table );
        getContentPane().add( mainSP, "Center" );
        validate();
    } // end of resetDefaults

    //
    // ----> Private internal classes follow.
    //

    /*
     * LoadedFile This class represents a file that has been loaded
     * for viewing.
     */
    private class LoadedFile {
        // Instance variables
        String fileName = null;
        ArrayList fileStrings = null;
        ArrayList dtObjects = null;
        int lineGuess = 0;

        /*
         * LoadedFile constructor.
         */
        LoadedFile(String fileName) {
            validateFile( fileName );
            this.fileName = fileName;
            //
            fileStrings = new ArrayList( lineGuess );
            dtObjects = new ArrayList( lineGuess );

            try
            {
                BufferedReader  rdr =
                    new BufferedReader( new FileReader( fileName ) );
                String inputLine = null;
                DateTime calculatedDT = null;
                int currLine = 0;
                while( (inputLine = rdr.readLine()) != null ) {
                    currLine++;
                    inputLine = inputLine.trim();
                    // Ignore blank and comment lines
                    if ( inputLine.length() == 0 ) continue;
                    if ( inputLine.charAt(0) == '#' ) continue;
                    // Ignore lines which fail DateTime construction
                    if ( (calculatedDT = getADate(inputLine)) == null ) {
                        System.err.println("Parse failed for: " + inputLine
                            + " at line number " + currLine);
                        continue;
                    }
                    // Add the input file string and DateTime to lists
                    fileStrings.add( inputLine );
                    dtObjects.add( calculatedDT );
                }
                rdr.close();
            }
            catch(IOException ioe)
            {
                System.err.println("Load of file: "
                    + fileName + " failed!");
                ioe.printStackTrace();
                System.exit(100);
            }

            // Try to be efficient (?really?)
            fileStrings.trimToSize();
            dtObjects.trimToSize();
            } // end of LoadedFile() constructor
        /*
         * General getters.
         */
        public String getFileName() { return fileName; }
        public int getLineGuess() { return lineGuess; }
        public ArrayList getFileStrings() { return fileStrings; }
        public ArrayList getDtObjects() { return dtObjects; }
        public int getLoadedFileSize() {
            if ( dtObjects == null ) return 0;
            return dtObjects.size();
        }
        /*
         * validateFile
         */
        private void validateFile(String fileName) {
            /*
             * Verify the user specified file exists and can
             * be read.
             */
            File f = new File( fileName );
            if ( !f.exists() || !f.canRead() ) {
                System.err.println("File: " + mainArgs[0]
                    + " does not exist or cannot be read!");
                usage();
                System.exit(2);
            }
            /*
             * Try to get a reasonable estimate of the number of lines
             * in the file.
             */
            // Java does not do this right IMO.  The number of bytes in a
            // file is a
            // long, but the length of a string is an int.  Why?
            lineGuess = (int)(f.length() / (long)"YYYY-MM-DDTHH:MM:SS".length());
            lineGuess += (lineGuess / 10);
            //
            // Debugging
            //
            if ( false ) {
                System.out.println("Line guess is: " + lineGuess);
            }
        } // end of validateFile(String)
    } // end of class LoadedFile class

    /*
     * TableView This abstract class defines the operations
     * necessary to create and access the Object arrays
     * required to create a JTable.
     */
    private abstract class TableView {
        protected Object[] colNames = null;
        protected Object[][] calcdValues = null;
        protected LoadedFile lddFile = null;
        //
        TableView(LoadedFile lddFile) {
            this.lddFile = lddFile;
        }
        //
        public Object[] getColNames() {
            return colNames;
        }
        public Object[][] getCalcdValues() {
            return calcdValues;
        }
        //
        abstract Object[] genColNames();
        abstract Object[][] genCalcdValues();
        abstract String getViewTitle();
        abstract void setViewColumnsWidth(JTable jt);
        //
    } // end of abstract class TableView

    /*
     * GetterTableView This class implements the operations
     * for the GetterView of the Jtable.
     */
    private class GetterTableView extends TableView {
        //
        GetterTableView(LoadedFile lddFile) {
            super(lddFile);
            setDefaultTableView( "getter" );
            colNames = genColNames();
            calcdValues = genCalcdValues();
        }

        /*
         * genCalcdValues is required by the base class.
         */
        Object[][] genCalcdValues() {
            Object[][] retValues = null;
            /*
             * Create an array of Objects that will contain
             * other arrays of Objects. (This is the 'column'
             * array).
             */
            ArrayList fileStrings = lddFile.getFileStrings();
            ArrayList dtObjects = lddFile.getDtObjects();
            int numRows = fileStrings.size();
            retValues = new Object[numRows][];
            int numCols = colNames.length;
            // System.err.println("NumCols : " + numCols);
            /*
             * Prime the array of arrays of Objects, allocating a new
             * secondary array for each of the primary array's
             * elements.
             */
            for (int nextStrNum = 0; nextStrNum < fileStrings.size(); ++ nextStrNum) {
                retValues[nextStrNum] = new Object[numCols]; // get the 'col' array
                //****
                //* This needs to be sync'd with the colNames array.
                //****
                // Current row, 1st column
                int column = 0; // working row value
                String fileString = (String)fileStrings.get(nextStrNum);
                retValues[nextStrNum][column++] = fileString;
                // Current row, 2nd column
                DateTime adt = (DateTime)dtObjects.get(nextStrNum);
                String adtStr = adt.toString();
                retValues[nextStrNum][column++] = adtStr;
                // Current row, other columns.
                // Order here must match that specified in the colNames
                // array.
                retValues[nextStrNum][column++]  = new Integer( adt.getMillisOfSecond() );
                retValues[nextStrNum][column++]  = new Integer( adt.getSecondOfMinute() );
                retValues[nextStrNum][column++]  = new Integer( adt.getMinuteOfHour() );
                retValues[nextStrNum][column++]  = new Integer( adt.getHourOfDay() );
                retValues[nextStrNum][column++]  = new Integer( adt.getDayOfWeek() );
                retValues[nextStrNum][column++]  = new Integer( adt.getDayOfMonth() );
                retValues[nextStrNum][column++]  = new Integer( adt.getDayOfYear() );
                retValues[nextStrNum][column++]  = new Integer( adt.getWeekOfWeekyear() );
                retValues[nextStrNum][column++] = new Integer( adt.getWeekyear() );
                retValues[nextStrNum][column++] = new Integer( adt.getMonthOfYear() );
                retValues[nextStrNum][column++] = new Integer( adt.getYear() );
                //
            } // the for
            if ( debugf ) dumpObjs( retValues, System.err );
            return retValues;
        } // end of genTBValues

        /*
         * genColNames is required by the base class.
         */
        Object[] genColNames() {
            Object[] retVal = {
                "FileString",
                "toString()",
                "MillisOfSec",
                "SecOfMin",
                "MinOfHr",
                "HrOfDay",
                "DayOfWk",
                "DayOfMon",
                "DayOfYr",
                "WeekOfWY",
                "Weekyear",
                "MonOfYr",
                "Year"
            };
            return retVal;
        }

        /*
         * getViewTitle
         */
        String getViewTitle() {
            return "DateTime.getXXX() Method Calculations"
                + " : "
                + TimeZone.getDefault().getDisplayName()
                + " : "
                + " Record Count "
                + currFile.getLoadedFileSize();
        }
        /*
         * setViewColumnLengths
         */
        void setViewColumnsWidth(JTable jt) {
            /*
             * Resize column 0, 1
             */
            TableColumnModel colmodel = jt.getColumnModel();
            TableColumn col0 = colmodel.getColumn(0);
            col0.setPreferredWidth(200);
            TableColumn col1 = colmodel.getColumn(1);
            col1.setPreferredWidth(200);
            return;
        }

    } // end of class getterTableView

    /*
     * HexView This class implements the operations for
     * the HexView of the file.
     */
    private class HexTableView extends TableView {
        //
        HexTableView(LoadedFile lddFile) {
            super(lddFile);
            setDefaultTableView( "hex" );
            colNames = genColNames();
            calcdValues = genCalcdValues();
        }

        /*
         * genCalcdValues is required by the base class.
         */
        Object[][] genCalcdValues() {
            Object[][] retValues = null;
            /*
             * Create an array of Objects that will contain
             * other arrays of Objects. (This is the 'column'
             * array).
             */
            ArrayList fileStrings = lddFile.getFileStrings();
            ArrayList dtObjects = lddFile.getDtObjects();
            int numRows = fileStrings.size();
            retValues = new Object[numRows][];
            int numCols = colNames.length;
            // System.err.println("NumCols : " + numCols);
            String fs = "yyyy-MM-dd'T'HH:mm:ss";
            DateFormat df = new SimpleDateFormat( fs );
            /*
             * Prime the array of arrays of Objects, allocating a new
             * secondary array for each of the primary array's
             * elements.
             */
            for (int nextStrNum = 0; nextStrNum < fileStrings.size(); ++ nextStrNum) {
                retValues[nextStrNum] = new Object[numCols]; // get the 'col' array
                //****
                //* This needs to be sync'd with the colNames array.
                //****
                // Current row, 1st column
                int column = 0;
                String fileString = (String)fileStrings.get(nextStrNum);
                retValues[nextStrNum][column++] = fileString;
                // Current row, 2nd column
                DateTime adt = (DateTime)dtObjects.get(nextStrNum);
                String adtStr = adt.toString();
                retValues[nextStrNum][column++] = adtStr;
                // Current row, other columns.
                // Order here must match that specified in the colNames
                // array.
                long lVal = adt.getMillis();
                Long millis = new Long( lVal );
                retValues[nextStrNum][column++]  = millis;
                String hexVal = Long.toHexString( lVal );
                String octalVal = Long.toOctalString( lVal );
                retValues[nextStrNum][column++]  = "0"+ LPad(octalVal,22);
                retValues[nextStrNum][column++]  = "0x" + LPad(hexVal,16);
                //
                Date javaDate = null;
                try
                {
                    javaDate = df.parse( fileString );
                }
                catch(ParseException e)
                {
                    System.err.println("Parse failed for : " + fileString);
                    // pe.printStackTrace();
                }
                //
                lVal = javaDate.getTime();
                millis = new Long( lVal );
                hexVal = Long.toHexString( lVal );
                octalVal = Long.toOctalString( lVal );
                retValues[nextStrNum][column++]  = millis;
                retValues[nextStrNum][column++]  = "0"+ LPad(octalVal,22);
                retValues[nextStrNum][column++]  = "0x" + LPad(hexVal,16);
                //
            } // the for
            if ( debugf ) dumpObjs( retValues, System.err );
            return retValues;
        } // end of genTBValues

        /*
         * genColNames is required by the base class.
         */
        Object[] genColNames() {
            Object[] retVal = {
                "FileString",
                "toString()",
                "JDT-millis",
                "JDT-Oct",
                "JDT-Hex",
                "Date-millis",
                "Date-Oct",
                "Date-Hex"
            };
            return retVal;
        }

        /*
         * getViewTitle
         */
        String getViewTitle() {
            return "View the long values"
                + " : "
                + TimeZone.getDefault().getDisplayName()
                + " : "
                + " Record Count "
                + currFile.getLoadedFileSize();
        }
        /*
         * setViewColumnLengths
         */
        void setViewColumnsWidth(JTable jt) {
            return;
        }

    } // end of class HexTableView

    /*
     * DateTableView This class implements the operations for
     * the java.util.Date of the file.
     */
    private class DateTableView extends TableView {
        //
        DateTableView(LoadedFile lddFile) {
            super(lddFile);
            setDefaultTableView( "date" );
            colNames = genColNames();
            calcdValues = genCalcdValues();
        }

        /*
         * genCalcdValues is required by the base class.
         */
        Object[][] genCalcdValues() {
            Object[][] retValues = null;
            /*
             * Create an array of Objects that will contain
             * other arrays of Objects. (This is the 'column'
             * array).
             */
            ArrayList fileStrings = lddFile.getFileStrings();
            ArrayList dtObjects = lddFile.getDtObjects();
            int numRows = fileStrings.size();
            retValues = new Object[numRows][];
            int numCols = colNames.length;
            // System.err.println("NumCols : " + numCols);
            /*
             * Prime the array of arrays of Objects, allocating a new
             * secondary array for each of the primary array's
             * elements.
             */
            for (int nextStrNum = 0; nextStrNum < fileStrings.size(); ++ nextStrNum) {
                retValues[nextStrNum] = new Object[numCols]; // get the 'col' array
                //****
                //* This needs to be sync'd with the colNames array.
                //****
                // Current row, 1st column
                int column = 0;
                String fileString = (String)fileStrings.get(nextStrNum);
                retValues[nextStrNum][column++] = fileString;
                // Current row, 2nd column
                DateTime adt = (DateTime)dtObjects.get(nextStrNum);
                String adtStr = adt.toString();
                retValues[nextStrNum][column++] = adtStr;
                // Current row, other columns.
                // Order here must match that specified in the colNames
                // array.
                long lVal = adt.getMillis();
                java.util.Date jDate = new java.util.Date( lVal );
                retValues[nextStrNum][column++] = new Integer( jDate.getSeconds() );
                retValues[nextStrNum][column++] = new Integer( jDate.getMinutes() );
                retValues[nextStrNum][column++] = new Integer( jDate.getHours() );
                retValues[nextStrNum][column++] = new Integer( jDate.getDay() );
                retValues[nextStrNum][column++] = new Integer( jDate.getDate() );
                retValues[nextStrNum][column++] = new Integer( jDate.getMonth() );
                retValues[nextStrNum][column++] = new Integer( jDate.getYear() );
                //
            } // the for
            if ( debugf ) dumpObjs( retValues, System.err );
            return retValues;
        } // end of genTBValues

        /*
         * genColNames is required by the base class.
         */
        Object[] genColNames() {
            Object[] retVal = {
                "FileString",   // 0
                "toString()",   // 1
                "Seconds",      // 2
                "Minutes",      // 3
                "Hours",        // 4
                "Day Of Week",          // 5
                "Day Of Month",         // 6
                "Month",        // 7
                "Year"          // 8
            };
            return retVal;
        }

        /*
         * getViewTitle
         */
        String getViewTitle() {
            return "java.util.Date getXXX"
                + " : "
                + TimeZone.getDefault().getDisplayName()
                + " : "
                + " Record Count "
                + currFile.getLoadedFileSize();
        }
        /*
         * setViewColumnLengths
         */
        void setViewColumnsWidth(JTable jt) {
            /*
             * Resize column 0, 1
             */
            TableColumnModel colmodel = jt.getColumnModel();
            TableColumn col0 = colmodel.getColumn(0);
            col0.setPreferredWidth(150);
            TableColumn col1 = colmodel.getColumn(1);
            col1.setPreferredWidth(150);
            return;
        }

    } // end of class DateTableView

    /*
     * CalTableView This class implements the operations for
     * the java.util.Date of the file.
     */
    private class CalTableView extends TableView {
        //
        CalTableView(LoadedFile lddFile) {
            super(lddFile);
            setDefaultTableView( "cal" );
            colNames = genColNames();
            calcdValues = genCalcdValues();
        }

        /*
         * genCalcdValues is required by the base class.
         */
        Object[][] genCalcdValues() {
            Object[][] retValues = null;
            /*
             * Create an array of Objects that will contain
             * other arrays of Objects. (This is the 'column'
             * array).
             */
            ArrayList fileStrings = lddFile.getFileStrings();
            ArrayList dtObjects = lddFile.getDtObjects();
            int numRows = fileStrings.size();
            retValues = new Object[numRows][];
            int numCols = colNames.length;
            // System.err.println("NumCols : " + numCols);
            /*
             * Prime the array of arrays of Objects, allocating a new
             * secondary array for each of the primary array's
             * elements.
             */
            for (int nextStrNum = 0; nextStrNum < fileStrings.size(); ++ nextStrNum) {
                retValues[nextStrNum] = new Object[numCols]; // get the 'col' array
                //****
                //* This needs to be sync'd with the colNames array.
                //****
                // Current row, 1st column
                int column = 0;
                String fileString = (String)fileStrings.get(nextStrNum);
                retValues[nextStrNum][column++] = fileString;
                // Current row, 2nd column
                DateTime adt = (DateTime)dtObjects.get(nextStrNum);
                String adtStr = adt.toString();
                retValues[nextStrNum][column++] = adtStr;
                // Current row, other columns.
                // Order here must match that specified in the colNames
                // array.
                long lVal = adt.getMillis();
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime( new Date( lVal ) );
                cal.setMinimalDaysInFirstWeek(4);
                retValues[nextStrNum][column++]  = new Integer( cal.get(
                    Calendar.MILLISECOND ) );
                retValues[nextStrNum][column++]  = new Integer( cal.get(
                    Calendar.SECOND ) );
                retValues[nextStrNum][column++]  = new Integer( cal.get(
                    Calendar.MINUTE ) );
                retValues[nextStrNum][column++]  = new Integer( cal.get(
                    Calendar.HOUR_OF_DAY ) );
                retValues[nextStrNum][column++]  = new Integer( cal.get(
                    Calendar.DAY_OF_WEEK ) );
                retValues[nextStrNum][column++]  = new Integer( cal.get(
                    Calendar.DAY_OF_MONTH ) );
                retValues[nextStrNum][column++]  = new Integer( cal.get(
                    Calendar.DAY_OF_YEAR ) );
                retValues[nextStrNum][column++]  = new Integer( cal.get(
                    Calendar.WEEK_OF_YEAR ) );
                retValues[nextStrNum][column++]  = new Integer( cal.get(
                    Calendar.MONTH ) );
                retValues[nextStrNum][column++]  = new Integer( cal.get(
                    Calendar.YEAR ) );
                //
            } // the for
            if ( debugf ) dumpObjs( retValues, System.err );
            return retValues;
        } // end of genTBValues

        /*
         * genColNames is required by the base class.
         */
        Object[] genColNames() {
            Object[] retVal = {
                "FileString",   // 0
                "toString()",   // 1
                "Millis",       // 2
                "Sec",          // 3
                "Min",          // 4
                "HrOfDay",      // 5
                "DayOfWeek",    // 6
                "DayOfMon",     // 7
                "DayOfYr",      // 8
                "WkofYr",       // 9
                "MonOfYr",      // 10
                "Year"          // 11
            };
            return retVal;
        }

        /*
         * getViewTitle
         */
        String getViewTitle() {
            return "java.util.Calendar.get(int)"
                + " : "
                + TimeZone.getDefault().getDisplayName()
                + " : "
                + " Record Count "
                + currFile.getLoadedFileSize();
        }
        /*
         * setViewColumnLengths
         */
        void setViewColumnsWidth(JTable jt) {
            /*
             * Resize column 0, 1
             */
            TableColumnModel colmodel = jt.getColumnModel();
            TableColumn col0 = colmodel.getColumn(0);
            col0.setPreferredWidth(175);
            TableColumn col1 = colmodel.getColumn(1);
            col1.setPreferredWidth(175);
            return;
        }

    } // end of class CalTableView

    /*
     * OpenAction
     */
    private class OpenAction extends AbstractAction {
        /*
         * Constructor
         */
        public OpenAction() {
            super("Open");
        } // end of ctor

        /*
         * actionPerformed
         */
        public void actionPerformed(ActionEvent e) {
            int result = chooser.showOpenDialog( DateTimeBrowser.this );
            String canPath = null;
            if ( result == JFileChooser.APPROVE_OPTION ) {
                File chosenFile = chooser.getSelectedFile();
                try
                {
                    canPath = chosenFile.getCanonicalPath();
                }
                catch(IOException ioe)
                {
                    System.err.println( "I/O Error on file: "
                        + chosenFile );
                    // Ignore it for now.
                }
                enableAllViews();
                currFile = new LoadedFile( canPath );
                TableView tView = getDefaultTableView();
                resetDefaults( tView );
            } // end of if a file actually chosen.
        } // end of actionPerformed
    } // end of class OpenAction

    /*
     * ExitAction
     */
    private class ExitAction extends AbstractAction {
        /*
         * Constructor
         */
        public ExitAction() {
            super("Exit");
        } // end of ctor

        /*
         * actionPerformed
         */
        public void actionPerformed(ActionEvent e) {
            DateTimeBrowser.this.setVisible( false );
            DateTimeBrowser.this.dispose();
            System.exit(0);
        } // end of actionPerformed
    } // end of class OpenAction

    /*
     * GetterAction
     */
    private class GetterAction extends AbstractAction {
        /*
         * Constructor
         */
        public GetterAction() {
            super("Getter");
        } // end of ctor

        /*
         * actionPerformed
         */
        public void actionPerformed(ActionEvent e) {
            TableView tView = new GetterTableView( currFile );
            resetDefaults( tView );
            enableAllViews();
            jmiGetter.setEnabled( false );
        } // end of actionPerformed
    } // end of class OpenAction

    /*
     * HexAction
     */
    private class HexAction extends AbstractAction {
        /*
         * Constructor
         */
        public HexAction() {
            super("Hex");
        } // end of ctor

        /*
         * actionPerformed
         */
        public void actionPerformed(ActionEvent e) {
            TableView tView = new HexTableView( currFile );
            resetDefaults( tView );
            enableAllViews();
            jmiHex.setEnabled( false );
        } // end of actionPerformed
    } // end of class OpenAction

    /*
     * DateAction
     */
    private class DateAction extends AbstractAction {
        /*
         * Constructor
         */
        public DateAction() {
            super("Date");
        } // end of ctor

        /*
         * actionPerformed
         */
        public void actionPerformed(ActionEvent e) {
            TableView tView = new DateTableView( currFile );
            resetDefaults( tView );
            enableAllViews();
            jmiDate.setEnabled( false );
        } // end of actionPerformed
    } // end of class DateAction

    /*
     * CalAction
     */
    private class CalAction extends AbstractAction {
        /*
         * Constructor
         */
        public CalAction() {
            super("Calendar");
        } // end of ctor

        /*
         * actionPerformed
         */
        public void actionPerformed(ActionEvent e) {
            TableView tView = new CalTableView( currFile );
            resetDefaults( tView );
            enableAllViews();
            jmiCal.setEnabled( false );
        } // end of actionPerformed
    } // end of class CalAction

} // class DateTimeBrowser
