package org.joda.time.contrib.hibernate.testmodel;

import org.joda.time.Period;

import java.io.Serializable;

/**
 *
 * @author gjoseph
 * @author $Author: $ (last edit)
 * @version $Revision: $
 */
public class SomethingThatHappens implements Serializable {
    private long id;
    private String name;
    private Period thePeriod;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Period getThePeriod() {
        return thePeriod;
    }

    public void setThePeriod(Period thePeriod) {
        this.thePeriod = thePeriod;
    }
}
