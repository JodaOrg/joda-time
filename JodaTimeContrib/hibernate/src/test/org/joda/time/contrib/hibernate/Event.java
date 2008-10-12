/*
 *  Copyright 2001-2008 Stephen Colebourne
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

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

/**
 * PO to be stored using hibernate
 */
public class Event implements Serializable
{
    private int id;
    private DateTime dateTime;
	private LocalDate localDate;
	private LocalTime localTime;
	private LocalTime localTime2;
	private LocalTime localTime3;
	private LocalDateTime localDateTime;

	public Event()
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

    public DateTime getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime)
    {
        this.dateTime = dateTime;
    }

	public LocalDate getLocalDate()
	{
		return localDate;
	}

	public void setLocalDate(LocalDate localDate)
	{
		this.localDate = localDate;
	}

	public LocalTime getLocalTime()
	{
		return localTime;
	}

	public void setLocalTime(LocalTime localTime)
	{
		this.localTime = localTime;
	}

	public LocalTime getLocalTime2()
	{
		return localTime2;
	}

	public void setLocalTime2(LocalTime localTime2)
	{
		this.localTime2 = localTime2;
	}

	public LocalTime getLocalTime3()
	{
		return localTime3;
	}

	public void setLocalTime3(LocalTime localTime3)
	{
		this.localTime3 = localTime3;
	}

    public LocalDateTime getLocalDateTime()
    {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime)
    {
        this.localDateTime = localDateTime;
    }
}
