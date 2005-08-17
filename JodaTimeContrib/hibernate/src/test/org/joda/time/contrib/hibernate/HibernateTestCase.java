package org.joda.time.contrib.hibernate;

import junit.framework.TestCase;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

public abstract class HibernateTestCase extends TestCase
{
	private SessionFactory factory;

	protected SessionFactory getSessionFactory()
	{
		if (this.factory == null)
		{
			Configuration cfg = new Configuration();

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
		this.factory.close();
		this.factory = null;
	}

	protected abstract void setupConfiguration(Configuration cfg);
}
