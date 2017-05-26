/*
 *  Copyright 2001-2013 Stephen Colebourne
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
package org.joda.time;

import java.util.Locale;
import java.util.concurrent.Callable;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Some tests to TimeMachine
 *
 * @author Luiz Real
 */
public class TestTimeMachine extends TestCase {

	long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365
			+ 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365
			+ 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365;

	// 2002-06-09
	private final long TEST_TIME_NOW =
			(y2002days + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;

	private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

	private static final Callable<DateTime> RETURN_CURRENT_DATE = new Callable<DateTime>() {
		public DateTime call() throws Exception {
			return new DateTime();
		}
	};

	private static final Callable<Void> THROW_EXCEPTION = new Callable<Void>() {
		public Void call() throws Exception {
			throw new Exception();
		}
	};

	private DateTimeZone zone = null;
	private Locale locale = null;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite() {
		return new TestSuite(TestTimeMachine.class);
	}

	public TestTimeMachine(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
		zone = DateTimeZone.getDefault();
		locale = Locale.getDefault();
		DateTimeZone.setDefault(LONDON);
		java.util.TimeZone.setDefault(LONDON.toTimeZone());
		Locale.setDefault(Locale.UK);
	}

	@Override
	protected void tearDown() throws Exception {
		DateTimeUtils.setCurrentMillisSystem();
		DateTimeZone.setDefault(zone);
		java.util.TimeZone.setDefault(zone.toTimeZone());
		Locale.setDefault(locale);
		zone = null;
	}

	public void testTimeMachine() throws Exception {
		DateTime destination = new DateTime().minusDays(1);
		DateTime beforeTravel = new DateTime();
		DateTime duringTravel = TimeMachine
									.goTo(destination)
									.andExecute(RETURN_CURRENT_DATE);
		DateTime afterTravel = new DateTime();

		assertEquals(destination, duringTravel);
		assertEquals(beforeTravel, afterTravel);
	}

	public void testTimeMachineCodeThrowsException() throws Exception {
		DateTime destination = new DateTime().minusDays(1);
		DateTime beforeTravel = new DateTime();
		try {
			TimeMachine.goTo(destination).andExecute(THROW_EXCEPTION);
			fail("Should have called code and thrown exception");
		} catch (Exception e) {
			// ok
			DateTime afterTravel = new DateTime();
			assertEquals("Should restore default DateTime", beforeTravel, afterTravel);
		}
	}
}
