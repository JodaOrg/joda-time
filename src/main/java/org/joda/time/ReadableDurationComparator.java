/*
* Copyright 2001-2013 Stephen Colebourne
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

/**
* Iterator for the ReadableDuration class
* <p>
* This is a stub iterator for the ReadableDuration class for more compatibility with the Java standard library.
* 
*
* @author Marius Spix
*/

package org.joda.time;

import java.util.Comparator;

public class ReadableDurationComparator implements Comparator<ReadableDuration> {

  @Override
	public int compare(ReadableDuration o1, ReadableDuration o2) {
		return 
				o1.isLongerThan(o2)  ? 1 :
			  o1.isShorterThan(o2) ? -1 :
			  0;
	}

}
