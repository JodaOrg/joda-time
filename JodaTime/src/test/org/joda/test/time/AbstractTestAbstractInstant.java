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
package org.joda.test.time;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.joda.time.AbstractInstant;
/**
 * This class is a Junit unit test base class for
 * Instant implementations.
 *
 * @author Stephen Colebourne
 * @author Guy Allard
 */
public abstract class AbstractTestAbstractInstant
	extends AbstractTestReadableInstant {

    private static Class cls = AbstractInstant.class;

	/**
	 * AbstractTestAbstractInstant constructor.
	 * A constructor with this signature
	 * is required for Junit testing.
	 * @param name
	 */
	public AbstractTestAbstractInstant(String name) {
		super(name, cls);
	}
	/**
	 * AbstractTestAbstractInstant constructor.
	 * A constructor with this signature
	 * is required for mapping the system inheritance tree to the
	 * test class inheritance tree.
	 * @param name The human readable name of the class.
	 * @param cls A reference to the Class being tested.
	 */
	public AbstractTestAbstractInstant(String name, Class cls) {
		super(name, cls);
        AbstractTestAbstractInstant.cls = cls;
	}

	/**
	 * Junit <code>setUp()</code> method.
	 */
	protected void setUp() /* throws Exception */ {
        super.setUp();
	}

	/**
	 * Junit <code>tearDown()</code> method.
	 */
	protected void tearDown() /* throws Exception */ {
        super.tearDown();
	}

    /**
     * Create a AbstractInstant by reflection
     */
    private AbstractInstant createAI(Class reflectClass, Class[] types, Object[] args) throws Throwable {
        return (AbstractInstant) create(reflectClass, types, args);
    }

	//
	// Tests of non-CTORs
	//
    public void testToDate() throws Throwable {
        AbstractInstant ri = createAI(cls, null, null);
        Date date = ri.toDate();
        assertEquals(ri.getMillis(), date.getTime());
    }

    public void testToCalendar() throws Throwable {
        AbstractInstant ri = createAI(cls, null, null);
        Calendar cal = ri.toCalendar(Locale.UK);
        assertEquals(ri.getMillis(), cal.getTime().getTime());
    }

    public void testToCalendarEx() throws Throwable {
        try {
            AbstractInstant ri = createAI(cls, null, null);
            Calendar cal = ri.toCalendar(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testToGregorianCalendar() throws Throwable {
        AbstractInstant ri = createAI(cls, null, null);
        GregorianCalendar cal = ri.toGregorianCalendar();
        assertEquals(ri.getMillis(), cal.getTime().getTime());
    }

}
