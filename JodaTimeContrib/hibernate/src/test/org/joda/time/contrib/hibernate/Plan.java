package org.joda.time.contrib.hibernate;

import org.joda.time.Interval;

public class Plan
{
    private int id;
    private Interval period;

    public Plan()
    {
    }
    
    public Plan(int id)
    {
        setId(id);
    }

    private void setId(int id)
    {
        this.id = id;
    }
    
    public int getId()
    {
        return id;
    }
    
    public Interval getPeriod()
    {
        return period;
    }
    
    public void setPeriod(Interval period)
    {
        this.period = period;
    }
}
