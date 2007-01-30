package org.joda.time.contrib.hibernate;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.Serializable;

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
}
