package org.joda.time;

import static org.joda.time.DateTimeUtils.currentTimeMillis;
import static org.joda.time.DateTimeUtils.removeThreadLocalMillisProvider;
import static org.joda.time.DateTimeUtils.setCurrentMillisProvider;
import static org.joda.time.DateTimeUtils.setCurrentMillisSystem;
import junit.framework.TestCase;

import org.joda.time.DateTimeUtils.MillisProvider;

public class TestDateTimeUtilsThreadLocalMillisProviderResetting extends TestCase {

	private static final MillisProvider staticMillisProvider = new FixedMillisProvider(888);

	private static final MillisProvider threadLocalMillisProvider = new FixedMillisProvider(888);

	public void setUp() {
		setCurrentMillisProvider(staticMillisProvider);
	}

	public void testResettingOfThreadLocalMillisProvider() {
		setThreadLocalMillisProvider();
		assertThatThreadLocalMillisProviderIsInUse();

		removeThreadLocalMillisProvider();
		assertThatStaticMillisProviderIsInUse();
	}

	private void setThreadLocalMillisProvider() {
		DateTimeUtils.setThreadLocalMillisProvider(threadLocalMillisProvider);
	}

	private void assertThatThreadLocalMillisProviderIsInUse() {
		assertEquals(threadLocalMillisProvider.getMillis(), currentTimeMillis());
	}

	private void assertThatStaticMillisProviderIsInUse() {
		assertEquals(staticMillisProvider.getMillis(), currentTimeMillis());
	}

	public void tearDown() {
		setCurrentMillisSystem();
	}

}
