/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-03 Stephen Colebourne.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.test.time ;
//
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.MutableDateTime;
import org.joda.time.chrono.iso.ISOChronology;
/**
 *  A {@link TestCase} that can define both simple and bulk test methods.<P>
 *
 *  A <I>simple test method</I> is the type of test traditionally
 *  supplied by by {@link TestCase}.  To define a simple test, create a public
 *  no-argument method whose name starts with "test".  You can specify the
 *  the name of simple test in the constructor of <Code>BulkTest</Code>;
 *  a subsequent call to {@link TestCase#run} will run that simple test.<P>
 *
 *  A <I>bulk test method</I>, on the other hand, returns a new instance
 *  of <Code>BulkTest</Code>, which can itself define new simple and bulk
 *  test methods.  By using the {@link #makeSuite} method, you can
 *  automatically create a hierarchal suite of tests and child bulk tests.<P>
 *
 *  For instance, consider the following two classes:
 *
 *  <Pre>
 *  public class TestSet extends BulkTest {
 *
 *      private Set set;
 *
 *      public TestSet(Set set) {
 *          this.set = set;
 *      }
 *
 *      public void testContains() {
 *          boolean r = set.contains(set.iterator().next()));
 *          assertTrue("Set should contain first element, r);
 *      }
 *
 *      public void testClear() {
 *          set.clear();
 *          assertTrue("Set should be empty after clear", set.isEmpty());
 *      }
 *  }
 *
 *
 *  public class TestHashMap extends BulkTest {
 *
 *      private Map makeFullMap() {
 *          HashMap result = new HashMap();
 *          result.put("1", "One");
 *          result.put("2", "Two");
 *          return result;
 *      }
 *
 *      public void testClear() {
 *          Map map = makeFullMap();
 *          map.clear();
 *          assertTrue("Map empty after clear", map.isEmpty());
 *      }
 *
 *      public BulkTest bulkTestKeySet() {
 *          return new TestSet(makeFullMap().keySet());
 *      }
 *
 *      public BulkTest bulkTestEntrySet() {
 *          return new TestSet(makeFullMap().entrySet());
 *      }
 *  }
 *  </Pre>
 *
 *  In the above examples, <Code>TestSet</Code> defines two
 *  simple test methods and no bulk test methods; <Code>TestHashMap</Code>
 *  defines one simple test method and two bulk test methods.  When
 *  <Code>makeSuite(TestHashMap.class).run</Code> is executed,
 *  <I>five</I> simple test methods will be run, in this order:<P>
 *
 *  <Ol>
 *  <Li>TestHashMap.testClear()
 *  <Li>TestHashMap.bulkTestKeySet().testContains();
 *  <Li>TestHashMap.bulkTestKeySet().testClear();
 *  <Li>TestHashMap.bulkTestEntrySet().testContains();
 *  <Li>TestHashMap.bulkTestEntrySet().testClear();
 *  </Ol>
 *
 *  In the graphical junit test runners, the tests would be displayed in
 *  the following tree:<P>
 *
 *  <UL>
 *  <LI>TestHashMap</LI>
 *      <UL>
 *      <LI>testClear
 *      <LI>bulkTestKeySet
 *          <UL>
 *          <LI>testContains
 *          <LI>testClear
 *          </UL>
 *      <LI>bulkTestEntrySet
 *          <UL>
 *          <LI>testContains
 *          <LI>testClear
 *          </UL>
 *      </UL>
 *  </UL>
 *
 *  A subclass can override a superclass's bulk test by
 *  returning <Code>null</Code> from the bulk test method.  If you only
 *  want to override specific simple tests within a bulk test, use the
 *  {@link #ignoredSimpleTests} method.<P>
 *
 *  Note that if you want to use the bulk test methods, you <I>must</I>
 *  define your <Code>suite()</Code> method to use {@link #makeSuite}.
 *  The ordinary {@link TestSuite} constructor doesn't know how to
 *  interpret bulk test methods.
 *
 *  Original implementation modified slightly for use with Joda ISO Time
 *  testing.
 *
 *  @author Paul Jack
 *  @author Guy Allard
 *
 */
public class BulkTest extends TestCase implements Cloneable {


    // Note:  BulkTest is Cloneable to make it easier to construct
    // BulkTest instances for simple test methods that are defined in
    // anonymous inner classes.  Basically we don't have to worry about
    // finding wierd constructors.  (And even if we found them, techinically
    // it'd be illegal for anyone but the outer class to invoke them).
    // Given one BulkTest instance, we can just clone it and reset the
    // method name for every simple test it defines.


    /**
     *  The full name of this bulk test instance.  This is the full name
     *  that is compared to {@link #ignoredSimpleTests} to see if this
     *  test should be ignored.  It's also displayed in the text runner
     *  to ease debugging.
     */
    String verboseName;


    /**
     *  Constructs a new <Code>BulkTest</Code> instance that will run the
     *  specified simple test.
     *
     *  @param name  the name of the simple test method to run
     */
    public BulkTest(String name) {
        super(name);
        this.verboseName = getClass().getName();
    }


    /**
     *  Creates a clone of this <Code>BulkTest</Code>.<P>
     *
     *  @return  a clone of this <Code>BulkTest</Code>
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(); // should never happen
        }
    }


    /**
     *  Returns an array of simple test names to ignore.<P>
     *
     *  If a simple test that's defined by this <Code>BulkTest</Code> or
     *  by one of its bulk test methods has a name that's in the returned
     *  array, then that simple test will not be executed.<P>
     *
     *  A simple test's name is formed by taking the class name of the
     *  root <Code>BulkTest</Code>, eliminating the package name, then
     *  appending the names of any bulk test methods that were invoked
     *  to get to the simple test, and then appending the simple test
     *  method name.  The method names are delimited by periods:
     *
     *  <Pre>
     *  TestHashMap.bulkTestEntrySet.testClear
     *  </Pre>
     *
     *  is the name of one of the simple tests defined in the sample classes
     *  described above.  If the sample <Code>TestHashMap</Code> class
     *  included this method:
     *
     *  <Pre>
     *  public String[] ignoredSimpleTests() {
     *      return new String[] { "TestHashMap.bulkTestEntrySet.testClear" };
     *  }
     *  </Pre>
     *
     *  then the entry set's clear method wouldn't be tested, but the key
     *  set's clear method would.
     *
     *  @return an array of the names of simple tests to ignore, or null if
     *   no tests should be ignored
     */
    public String[] ignoredSimpleTests() {
        return null;
    }


    /**
     *  Returns the display name of this <Code>BulkTest</Code>.
     *
     *  @return the display name of this <Code>BulkTest</Code>
     */
    public String toString() {
        return getName() + "(" + verboseName + ") ";
    }


    /**
     *  Returns a {@link TestSuite} for testing all of the simple tests
     *  <I>and</I> all the bulk tests defined by the given class.<P>
     *
     *  The class is examined for simple and bulk test methods; any child
     *  bulk tests are also examined recursively; and the results are stored
     *  in a hierarchal {@link TestSuite}.<P>
     *
     *  The given class must be a subclass of <Code>BulkTest</Code> and must
     *  not be abstract.<P>
     *
     *  @param c  the class to examine for simple and bulk tests
     *  @return  a {@link TestSuite} containing all the simple and bulk tests
     *    defined by that class
     */
    public static TestSuite makeSuite(Class c) {
        if (Modifier.isAbstract(c.getModifiers())) {
            throw new IllegalArgumentException("Class must not be abstract.");
        }
        if (!BulkTest.class.isAssignableFrom(c)) {
            throw new IllegalArgumentException("Class must extend BulkTest.");
        }
        return new BulkTestSuiteMaker(c).make();
    }
    //
    // Methods added by the Joda project for testing of the
    // org.joda.time package and it's subpackages.
    //
    /**
     * Build an ISODateTime string from a Gregorian calendar.
     * @param gc a Gregorian Calendar instance.
     * @return a String suitable for ISODateTime instantiation.
     */
    protected String getDateTimeString(GregorianCalendar gc) {
        int year = gc.get(Calendar.YEAR);
        StringBuffer retVal = new StringBuffer();
        if ( gc.get(Calendar.ERA) == GregorianCalendar.BC ) {
            if ( year > 1 ) retVal.append("-");
            year = year - 1;
        }
        //
        retVal.append( padNumberToLen( Math.abs(year), 4 ) );
        retVal.append("-");
        retVal.append( padNumberToLen(gc.get(Calendar.MONTH)+1, 2) );
        retVal.append("-");
        retVal.append( padNumberToLen(gc.get(Calendar.DATE), 2) );
        retVal.append("T");
        retVal.append(  padNumberToLen(gc.get(Calendar.HOUR), 2) );
        retVal.append(":");
        retVal.append( padNumberToLen(gc.get(Calendar.MINUTE), 2) );
        retVal.append(":");
        retVal.append( padNumberToLen(gc.get(Calendar.SECOND), 2) );
        retVal.append(".");
        retVal.append( padNumberToLen(gc.get(Calendar.MILLISECOND), 3) );
        retVal.append("Z");
        return retVal.toString();
    }
    /**
     * Create a string from an integer, pad it on the left with
     * '0's to the length specified.
     * @param num The number to use.
     * @param len The length to pad to.
     * @return The created string.
     */
    protected String padNumberToLen(int num, int len) {
        StringBuffer retVal = new StringBuffer( len );
        retVal.insert(0, "" + num);
        if ( retVal.length() >= len ) return retVal.toString();
        retVal.insert(0, copiesOf("0", len - retVal.length()));
        if (num < 0) {
            retVal.insert(0,"-");
        }
        return retVal.toString();
    }
    /**
     * Generate the specified number of copies of a String and
     * return it.
     * @param s The String to copy.
     * @param c The number of copies.
     * @return The generated String.
     */
    protected String copiesOf(String s, int c) {
        if ( c == 0 ) return s;
        StringBuffer retBuff = new StringBuffer( s.length() * c );
        for (int i = 1; i <= c; ++i) {
            retBuff.insert( retBuff.length(), s );
        }
        return retBuff.toString();
    }
    /**
     * Convert a String to an Integer, and return the int
     * value.
     * @param s The string to convert.
     * @return The int to return.
     * @throws A NullPointerException if the string fails conversion.
     */
    protected int getPartValue(String s) {
        Integer iVal = null;
        try
        {
            iVal = new Integer( s );
        }
        catch(NumberFormatException nfe)
        {
        }
        return iVal.intValue();
    }
    /**
     *
     * @param s An ISO format Date/Time string.
     * @return The left hand side of the string.
     */
    protected String lhsDT(String s) {
        return s.substring(0,s.indexOf('T'));
    }
    /**
     *
     * @param s An ISO format Date/Time string.
     * @return The right hand side of the string.
     */
    protected String rhsDT(String s) {
        return s.substring(s.indexOf('T')+1);
    }
    /**
     *
     * @param s An ISO format Date/Time string.
     * @return An array list containing all the subelements
     * of the input string.
     */
    protected ArrayList getDTA(String s) {
        ArrayList al = new ArrayList();
        String dateSide = lhsDT( s );
        String timeSide = rhsDT( s );
        // Date Side
        if ( dateSide.substring(0,1).equals("-") )
        {
            al.add("-");
            dateSide = dateSide.substring(1);
        }
        else
        {
            al.add("");
        }
        int pos = dateSide.indexOf('-');
        al.add( dateSide.substring(0, pos) );
        al.add("-");
        pos += 1;
        dateSide = dateSide.substring( pos );
        //
        pos = dateSide.indexOf('-');
        al.add( dateSide.substring(0, pos) );
        al.add("-");
        al.add( dateSide.substring( ++pos ) );
        al.add("T");
        //
        // Time Side
        //
        pos = timeSide.indexOf(':');
        al.add( timeSide.substring(0, pos) );
        al.add(":");
        pos += 1;
        timeSide = timeSide.substring( pos );
        //
        pos = timeSide.indexOf(':');
        al.add( timeSide.substring(0, pos) );
        al.add(":");
        pos += 1;
        timeSide = timeSide.substring( pos );
        //
        pos = timeSide.indexOf('.');
        if ( pos == -1 ) {
            al.add( timeSide );
            return al;
        }
        al.add( timeSide.substring(0,pos) );
        al.add(".");
        pos += 1;
        timeSide = timeSide.substring(pos);
        pos = timeSide.indexOf('Z');
        al.add( timeSide.substring(0,pos) );
        al.add("Z");
        //
        return al;
    }

    /**
     * An array of the short names of the days of the week.
     */
    protected final String[] dowShort = {
        "N/A","Mon","Tue","Wed","Thu","Fri","Sat","Sun"
    };
    /**
     * An array of the long names of the days of the week.
     */
    protected final String[] dowLong = {
        "N/A","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"
    };
    /**
     * An array of the short names of the month.
     */
    protected final String[] moyShort = {
        "N/A","Jan","Feb","Mar","Apr","May","Jun",
            "Jul","Aug","Sep","Oct","Nov","Dec"
    };
    /**
     * An array of the long names of the month.
     */
    protected final String[] moyLong = {
        "N/A","January","February","March","April","May","June",
            "July","August","September","October","November","December"
    };

    /**
     *
     * @param s An ISO format Date/Time string.
     * @return A Joda MutableDateTime object, or null if
     * parsing fails.
     */
    MutableDateTime getMDTFromString(String s) {
        MutableDateTime retVal = null;
        try
        {
            retVal = new MutableDateTime( s,
                ISOChronology.getInstanceUTC() );
        }
        catch(IllegalArgumentException pe)
        {
            System.err.println("IllegalArgumentException Detected");
            pe.printStackTrace();
        }
        return retVal;
    }

} // End of BulkTest class


// It was easier to use a separate class to do all the reflection stuff
// for making the TestSuite instances.  Having permanent state around makes
// it easier to handle the recursion.
class BulkTestSuiteMaker {


    /** The class that defines simple and bulk tests methods. */
    private Class startingClass;


    /** List of ignored simple test names. */
    private List ignored;


    /** The TestSuite we're currently populating.  Can change over time. */
    private TestSuite result;


    /**
     *  The prefix for simple test methods.  Used to check if a test is in
     *  the ignored list.
     */
    private String prefix;


    /**
     *  Constructor.
     *
     *  @param startingClass  the starting class
     */
    public BulkTestSuiteMaker(Class startingClass) {
        this.startingClass = startingClass;
    }


    /**
     *  Makes a hierarchal TestSuite based on the starting class.
     *
     *  @return  the hierarchal TestSuite for startingClass
     */
    public TestSuite make() {
         this.result = new TestSuite();
         this.prefix = getBaseName(startingClass);
         result.setName(prefix);

         BulkTest bulk = makeFirstTestCase(startingClass);
         ignored = new ArrayList();
         String[] s = bulk.ignoredSimpleTests();
         if (s != null) {
             ignored.addAll(Arrays.asList(s));
         }
         make(bulk);
         return result;
    }


    /**
     *  Appends all the simple tests and bulk tests defined by the given
     *  instance's class to the current TestSuite.
     *
     *  @param bulk  An instance of the class that defines simple and bulk
     *    tests for us to append
     */
    void make(BulkTest bulk) {
        Class c = bulk.getClass();
        Method[] all = c.getMethods();
        for (int i = 0; i < all.length; i++) {
            if (isTest(all[i])) addTest(bulk, all[i]);
            if (isBulk(all[i])) addBulk(bulk, all[i]);
        }
    }


    /**
     *  Adds the simple test defined by the given method to the TestSuite.
     *
     *  @param bulk  The instance of the class that defined the method
     *   (I know it's wierd.  But the point is, we can clone the instance
     *   and not have to worry about constructors.)
     *  @param m  The simple test method
     */
    void addTest(BulkTest bulk, Method m) {
        BulkTest bulk2 = (BulkTest)bulk.clone();
        bulk2.setName(m.getName());
        bulk2.verboseName = prefix + "." + m.getName();
        if (ignored.contains(bulk2.verboseName)) return;
        result.addTest(bulk2);
    }


    /**
     *  Adds a whole new suite of tests that are defined by the result of
     *  the given bulk test method.  In other words, the given bulk test
     *  method is invoked, and the resulting BulkTest instance is examined
     *  for yet more simple and bulk tests.
     *
     *  @param bulk  The instance of the class that defined the method
     *  @param m  The bulk test method
     */
    void addBulk(BulkTest bulk, Method m) {
        BulkTest bulk2;
        try {
            bulk2 = (BulkTest)m.invoke(bulk, null);
            if (bulk2 == null) return;
        } catch (InvocationTargetException e) {
            throw new Error(); // FIXME;
        } catch (IllegalAccessException e) {
            throw new Error(); // FIXME;
        }

        // Save current state on the stack.
        String oldPrefix = prefix;
        TestSuite oldResult = result;

        prefix = prefix + "." + m.getName();
        result = new TestSuite();
        result.setName(m.getName());

        make(bulk2);

        oldResult.addTest(result);

        // Restore the old state
        prefix = oldPrefix;
        result = oldResult;
    }


    /**
     *  Returns the base name of the given class.
     *
     *  @param c  the class
     *  @return the name of that class, minus any package names
     */
    private static String getBaseName(Class c) {
        String name = c.getName();
        int p = name.lastIndexOf('.');
        if (p > 0) {
            name = name.substring(p + 1);
        }
        return name;
    }


    // These three methods are used to create a valid BulkTest instance
    // from a class.

    private static Constructor getTestCaseConstructor(Class c) {
        try {
            return c.getConstructor(new Class[] { String.class });
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(c + " must provide " +
             "a (String) constructor");
        }
    }


    private static BulkTest makeTestCase(Class c, Method m) {
        Constructor con = getTestCaseConstructor(c);
        try {
            return (BulkTest)con.newInstance(new String[] { m.getName() });
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(); // FIXME;
        } catch (IllegalAccessException e) {
            throw new Error(); // should never occur
        } catch (InstantiationException e) {
            throw new RuntimeException(); // FIXME;
        }
    }


    private static BulkTest makeFirstTestCase(Class c) {
        Method[] all = c.getMethods();
        for (int i = 0; i < all.length; i++) {
            if (isTest(all[i])) return makeTestCase(c, all[i]);
        }
        throw new IllegalArgumentException(c.getName() + " must provide "
          + " at least one test method.");
    }


    /**
     *  Returns true if the given method is a simple test method.
     */
    private static boolean isTest(Method m) {
        if (!m.getName().startsWith("test")) return false;
        if (m.getReturnType() != Void.TYPE) return false;
        if (m.getParameterTypes().length != 0) return false;
        int mods = m.getModifiers();
        if (Modifier.isStatic(mods)) return false;
        if (Modifier.isAbstract(mods)) return false;
        return true;
    }


    /**
     *  Returns true if the given method is a bulk test method.
     */
    private static boolean isBulk(Method m) {
        if (!m.getName().startsWith("bulkTest")) return false;
        if (m.getReturnType() != BulkTest.class) return false;
        if (m.getParameterTypes().length != 0) return false;
        int mods = m.getModifiers();
        if (Modifier.isStatic(mods)) return false;
        if (Modifier.isAbstract(mods)) return false;
        return true;
    }

} // end of BulkTestSuiteMaker class

