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

import java.io.File;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.joda.time.Interval;

public class TestPersistentIntervalNull extends HibernateTestCase
{
    private SessionFactory factory;
    private Session session;
    private Transaction transaction;
    
    protected void setUp() throws SQLException
    {
        factory = getSessionFactory();
        store();
    }
    
    private void store() throws SQLException
    {
        openAndBegin();
        
        Plan plan = new Plan(1);
        plan.setPeriod(null);
        
        session.save(plan);
        session.flush();
        
        commitAndClose();
    }

    private void openAndBegin()
    {
        session = factory.openSession();
        transaction = session.beginTransaction();
    }
    
    private void commitAndClose()
    {
        transaction.commit();
        session.close();
    }
    
    public void testQueryById() throws SQLException
    {
        openAndBegin();
        Interval persistedPeriod = queryPlan().getPeriod();
        commitAndClose();
        assertPlanPeriod(persistedPeriod);
    }

    private void assertPlanPeriod(Interval period)
    {
        assertNull(period);
    }

    private Plan queryPlan()
    {
        return (Plan) session.get(Plan.class, new Integer(1));
    }
    
    protected void setupConfiguration(Configuration cfg)
    {
        cfg.addFile(new File("src/test/java/org/joda/time/contrib/hibernate/plan.hbm.xml"));
    }
    
    protected void tearDown() throws Exception
    {
        remove();
        super.tearDown();
    }

    private void remove()
    {
        openAndBegin();
        session.delete(queryPlan());
        commitAndClose();
    }
}
