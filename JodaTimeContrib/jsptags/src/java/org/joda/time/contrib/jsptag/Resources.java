/*
 * Copyright 1999-2004 The Apache Software Foundation.
 * Modifications, Copyright 2005 Stephen Colebourne
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package org.joda.time.contrib.jsptag;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <p>Provides locale-neutral access to string resources.  Only the
 * documentation and code are in English. :-)
 *
 * <p>The major goal, aside from globalization, is convenience.
 * Access to resources with no parameters is made in the form:</p>
 * <pre>
 *     Resources.getMessage(MESSAGE_NAME);
 * </pre>
 *
 * <p>Access to resources with one parameter works like</p>
 * <pre>
 *     Resources.getMessage(MESSAGE_NAME, arg1);
 * </pre>
 *
 * <p>... and so on.</p>
 *
 * @author Shawn Bayern
 */
public class Resources {

    //*********************************************************************
    // Static data

    /** The location of our resources. */
    private static final String RESOURCE_LOCATION = "org.joda.time.contrib.jsptag.Resources";

    /** Our class-wide ResourceBundle. */
    private static ResourceBundle rb = ResourceBundle.getBundle(RESOURCE_LOCATION);


    //*********************************************************************
    // Public static methods

    /** Retrieves a message with no arguments. */
    public static String getMessage(String name)
      throws MissingResourceException {
        return rb.getString(name);
    }

    /** Retrieves a message with arbitrarily many arguments. */
    public static String getMessage(String name, Object[] a)
      throws MissingResourceException {
        String res = rb.getString(name);
        return MessageFormat.format(res, a);
    }

    /** Retrieves a message with one argument. */
    public static String getMessage(String name, Object a1)
      throws MissingResourceException {
        return getMessage(name, new Object[] { a1 });
    }

    /** Retrieves a message with two arguments. */
    public static String getMessage(String name, Object a1, Object a2)
      throws MissingResourceException {
        return getMessage(name, new Object[] { a1, a2 });
    }

    /** Retrieves a message with three arguments. */
    public static String getMessage(String name,
            Object a1,
            Object a2,
            Object a3)
      throws MissingResourceException {
        return getMessage(name, new Object[] { a1, a2, a3 });
    }

    /** Retrieves a message with four arguments. */
    public static String getMessage(String name,
             Object a1,
            Object a2,
            Object a3,
            Object a4)
      throws MissingResourceException {
        return getMessage(name, new Object[] { a1, a2, a3, a4 });
    }

    /** Retrieves a message with five arguments. */
    public static String getMessage(String name,
            Object a1,
            Object a2,
            Object a3,
            Object a4,
            Object a5)
      throws MissingResourceException {
        return getMessage(name, new Object[] { a1, a2, a3, a4, a5 });
    }

    /** Retrieves a message with six arguments. */
    public static String getMessage(String name,
            Object a1,
            Object a2,
            Object a3,
            Object a4,
            Object a5,
            Object a6)
      throws MissingResourceException {
        return getMessage(name, new Object[] { a1, a2, a3, a4, a5, a6 });
    }

}
