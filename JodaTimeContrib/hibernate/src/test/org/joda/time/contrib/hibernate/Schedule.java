package org.joda.time.contrib.hibernate;

import org.joda.time.YearMonthDay;
import org.joda.time.TimeOfDay;

import java.io.Serializable;

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
