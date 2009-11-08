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

import junit.framework.TestCase;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import java.sql.Connection;
import java.sql.Statement;

public abstract class HibernateTestCase extends TestCase
{
	private SessionFactory factory;
    private Configuration cfg;

    protected SessionFactory getSessionFactory()
	{
		if (this.factory == null)
		{
                    cfg = new Configuration();

                    setupConfiguration(cfg);

			cfg.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
			cfg.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:hbmtest" + getClass().getName());
			cfg.setProperty("hibernate.dialect", HSQLDialect.class.getName());

			cfg.setProperty("hibernate.show_sql", "true");
			SessionFactory factory = cfg.buildSessionFactory();

			SchemaUpdate update = new SchemaUpdate(cfg);
			update.execute(true, true);

			this.factory = factory;
		}
		return factory;
	}

	protected void tearDown() throws Exception
	{
            final String[] dropSQLs = cfg.generateDropSchemaScript(new HSQLDialect());
            final Connection connection = getSessionFactory().openSession().connection();
            try {
                Statement stmt = connection.createStatement();
                for (int i = 0; i < dropSQLs.length; i++) {
                    //System.out.println("dropSQLs[i] = " + dropSQLs[i]);
                    stmt.executeUpdate(dropSQLs[i]);
                }
            } finally {
                connection.close();
            }

            if (this.factory != null)
		{
			this.factory.close();
			this.factory = null;
		}
	}

	protected abstract void setupConfiguration(Configuration cfg);
}
