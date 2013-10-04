package org.joda.time;

import java.util.concurrent.CountDownLatch;

import junit.framework.TestCase;

import org.joda.time.DateTimeUtils.MillisProvider;

public class TestDateTimeUtilsThreadLocalMillisProvider extends TestCase {

	private final ThreadCoordinator threadCoordinator = new ThreadCoordinator();

	private final MillisProvider mainThreadMillisProvider = new FixedMillisProvider(2L);

	private final MillisProvider separateThreadMillisProvider = new FixedMillisProvider(9L);

	public void testThatEachThreadHasItsOwnThreadLocalMillisProvider() {
		setMillisProviderInMainThread();

		setMillisProviderInSeparateThread();

		waitUntilSeparateThreadSetsMillisProvider();
		assertThatMillisProviderInMainThreadHasNotBeenOverridden();
	}

	private void setMillisProviderInMainThread() {
		DateTimeUtils.setThreadLocalMillisProvider(mainThreadMillisProvider);
	}

	private void setMillisProviderInSeparateThread() {
		Thread separateThread = new Thread() {
			@Override
			public void run() {
				DateTimeUtils.setThreadLocalMillisProvider(separateThreadMillisProvider);
				assertThatMillisProviderInSeparateThreadHasBeenSet();
				threadCoordinator.notifyThatThreadLocalMillisProviderHasBeenSet();
			}

			private void assertThatMillisProviderInSeparateThreadHasBeenSet() {
				long currentTimeMillis = DateTimeUtils.currentTimeMillis();
				assertEquals(separateThreadMillisProvider.getMillis(), currentTimeMillis);
			}
		};
		separateThread.start();
	}

	private void waitUntilSeparateThreadSetsMillisProvider() {
		threadCoordinator.waitUntilSeparateThreadSetsMillisProvider();
	}

	private void assertThatMillisProviderInMainThreadHasNotBeenOverridden() {
		long currentTimeMillis = DateTimeUtils.currentTimeMillis();
		assertEquals(mainThreadMillisProvider.getMillis(), currentTimeMillis);
	}

	private static class ThreadCoordinator {

		private final CountDownLatch latch = new CountDownLatch(1);

		public void waitUntilSeparateThreadSetsMillisProvider() {
			try {
				latch.await();
			} catch (InterruptedException e) {
			}
		}

		public void notifyThatThreadLocalMillisProviderHasBeenSet() {
			latch.countDown();
		}
	}

}
