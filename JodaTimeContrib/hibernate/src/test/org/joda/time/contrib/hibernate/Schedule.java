package org.joda.time.contrib.hibernate;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import java.io.Serializable;

/**
 * PO to be stored using hibernate
 */
public class Schedule implements Serializable
{
    private int id;
    private YearMonthDay startDate;

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
}
