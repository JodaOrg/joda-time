/*
 *  Copyright 2001-2007 Stephen Colebourne
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
package org.joda.time.contrib.hibernate;

import java.io.Serializable;

import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

/**
 * PO to be stored using hibernate
 */
public class Schedule implements Serializable
{
    private int id;
    private YearMonthDay startDate;
	private TimeOfDay nextTime;
	private TimeOfDay nextTimeMillis;

	public Schedule()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

	public YearMonthDay getStartDate()
	{
		return startDate;
	}

	public void setStartDate(YearMonthDay startDate)
	{
		this.startDate = startDate;
	}

	public TimeOfDay getNextTime()
	{
		return nextTime;
	}

	public void setNextTime(TimeOfDay nextTime)
	{
		this.nextTime = nextTime;
	}

	public TimeOfDay getNextTimeMillis()
	{
		return nextTimeMillis;
	}

	public void setNextTimeMillis(TimeOfDay nextTimeMillis)
	{
		this.nextTimeMillis = nextTimeMillis;
	}
}
