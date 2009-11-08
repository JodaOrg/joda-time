package org.joda.time.contrib.hibernate;

import junit.framework.Assert;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.joda.time.Period;
import org.joda.time.contrib.hibernate.testmodel.SomethingThatHappens;

import java.io.IOException;
import java.io.File;
import java.sql.SQLException;

/**
 * @author gjoseph
 * @author $Author: $ (last edit)
 * @version $Revision: $
 */
public class TestPersistentPeriod extends HibernateTestCase {
    protected void setupConfiguration(Configuration cfg) {
        cfg.addFile(new File("src/test/java/org/joda/time/contrib/hibernate/testmodel/SomethingThatHappens.hbm.xml"));
    }

    private Period[] periods = new Period[]{
            Period.days(2), Period.seconds(30), Period.months(3),
            new Period(30), new Period(4, 35, 40, 141),
            new Period(28, 10, 2, 2, 4, 35, 40, 141), new Period(28, 10, 0, 16, 4, 35, 40, 141),
            // new Period(new DateTime()),
            // new Period(new YearMonthDay()),
            // new Period(new YearMonthDay(2005, 12, 25)),
            // new Period(new YearMonthDay(2005, 0, 25)), new Period(new YearMonthDay(2005, 13, 25)),
            // new Period(new DateTime(CopticChronology.getInstance())            )
    };

    public void testSimpleStore() throws SQLException, IOException {
        Session session = getSessionFactory().openSession();

        for (int i = 0; i < periods.length; i++) {
            SomethingThatHappens thing = new SomethingThatHappens();
            thing.setId(i);
            thing.setName("test_" + i);
            thing.setThePeriod(periods[i]);
            session.save(thing);
        }

        session.flush();
        session.connection().commit();
        session.close();

        for (int i = 0; i < periods.length; i++) {
            session = getSessionFactory().openSession();
            SomethingThatHappens happeningThing = (SomethingThatHappens) session.get(SomethingThatHappens.class, new Long(i));

            Assert.assertNotNull(happeningThing);
            Assert.assertEquals(i, happeningThing.getId());
            Assert.assertEquals("test_" + i, happeningThing.getName());
            Assert.assertEquals(periods[i], happeningThing.getThePeriod());

            session.close();
        }

        // printSqlQueryResults("SELECT * FROM happening");
    }

}
