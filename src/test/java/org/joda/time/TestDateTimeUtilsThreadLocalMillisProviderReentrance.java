package org.joda.time;

import junit.framework.TestCase;

import org.joda.time.DateTimeUtils.MillisProvider;

public class TestDateTimeUtilsThreadLocalMillisProviderReentrance extends TestCase {

	private final MillisProvider mainThreadMillisProvider = new FixedMillisProvider(2L);

	public void testThatThreadLocalMillisProviderIsInheritedByChildThreads() throws InterruptedException {
		setMillisProviderInMainThread();
		assertThatMillisProviderIsInheritedByChildThread();
	}

	private void setMillisProviderInMainThread() {
		DateTimeUtils.setThreadLocalMillisProvider(mainThreadMillisProvider);
	}

	private void assertThatMillisProviderIsInheritedByChildThread() {
		Thread childThread = new Thread() {
			@Override
			public void run() {
				assertThatMillisProviderIsInherited();
			}
			private void assertThatMillisProviderIsInherited() {
				long currentTimeMillis = DateTimeUtils.currentTimeMillis();
				assertEquals(mainThreadMillisProvider.getMillis(), currentTimeMillis);
			}
		};
		childThread.start();
	}


}
