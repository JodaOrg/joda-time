package org.joda.time;

import org.joda.time.DateTimeUtils.MillisProvider;

class FixedMillisProvider implements MillisProvider {

	private final long fixedMillis;

	public FixedMillisProvider(long fixedMillis) {
		this.fixedMillis = fixedMillis;
	}

	@Override
	public long getMillis() {
		return fixedMillis;
	}

}