package org.joda.time.contrib.hibernate.testmodel;

import org.joda.time.Duration;

import java.io.Serializable;

/**
 *
 * @author gjoseph
 * @author $Author: $ (last edit)
 * @version $Revision: $
 */
public class SomethingThatLasts implements Serializable {
    private long id;
    private String name;
    private Duration theDuration;

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

    public Duration getTheDuration() {
        return theDuration;
    }

    public void setTheDuration(Duration theDuration) {
        this.theDuration = theDuration;
    }

}
