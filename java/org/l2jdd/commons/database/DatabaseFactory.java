
package org.l2jdd.commons.database;

import java.sql.Connection;
import java.util.logging.Logger;

import org.mariadb.jdbc.MariaDbPoolDataSource;

import org.l2jdd.Config;

/**
 * @author Mobius
 * @version November 10th 2018
 */
public class DatabaseFactory
{
	private static final Logger LOGGER = Logger.getLogger(DatabaseFactory.class.getName());
	
	private static final MariaDbPoolDataSource DATABASE_POOL = new MariaDbPoolDataSource(Config.DATABASE_URL + "&user=" + Config.DATABASE_LOGIN + "&password=" + Config.DATABASE_PASSWORD + "&maxPoolSize=" + Config.DATABASE_MAX_CONNECTIONS);
	
	public static void init()
	{
		// Test if connection is valid.
		try
		{
			DATABASE_POOL.getConnection().close();
			LOGGER.info("Database: Initialized.");
		}
		catch (Exception e)
		{
			LOGGER.info("Database: Problem on initialize. " + e);
		}
	}
	
	public static Connection getConnection()
	{
		Connection con = null;
		while (con == null)
		{
			try
			{
				con = DATABASE_POOL.getConnection();
			}
			catch (Exception e)
			{
				LOGGER.severe("DatabaseFactory: Cound not get a connection. " + e);
			}
		}
		return con;
	}
	
	public static void close()
	{
		try
		{
			DATABASE_POOL.close();
		}
		catch (Exception e)
		{
			LOGGER.severe("DatabaseFactory: There was a problem closing the data source. " + e);
		}
	}
}
