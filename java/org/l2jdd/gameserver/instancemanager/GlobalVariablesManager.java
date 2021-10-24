
package org.l2jdd.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.l2jdd.commons.database.DatabaseFactory;
import org.l2jdd.gameserver.model.variables.AbstractVariables;

/**
 * Global Variables Manager.
 * @author xban1x
 */
public class GlobalVariablesManager extends AbstractVariables
{
	private static final Logger LOGGER = Logger.getLogger(GlobalVariablesManager.class.getName());
	
	// SQL Queries.
	private static final String SELECT_QUERY = "SELECT * FROM global_variables";
	private static final String DELETE_QUERY = "DELETE FROM global_variables";
	private static final String INSERT_QUERY = "INSERT INTO global_variables (var, value) VALUES (?, ?)";
	
	// Public variable names
	public static final String COC_TOP_MARKS = "COC_TOP_MARKS";
	public static final String COC_TOP_MEMBER = "COC_TOP_MEMBER";
	public static final String COC_TRUE_HERO = "COC_TRUE_HERO";
	public static final String COC_TRUE_HERO_REWARDED = "COC_TRUE_HERO_REWARDED";
	
	protected GlobalVariablesManager()
	{
		restoreMe();
	}
	
	@Override
	public boolean restoreMe()
	{
		// Restore previous variables.
		try (Connection con = DatabaseFactory.getConnection();
			Statement st = con.createStatement();
			ResultSet rset = st.executeQuery(SELECT_QUERY))
		{
			while (rset.next())
			{
				set(rset.getString("var"), rset.getString("value"));
			}
		}
		catch (SQLException e)
		{
			LOGGER.warning(getClass().getSimpleName() + ": Couldn't restore global variables.");
			return false;
		}
		
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + getSet().size() + " variables.");
		return true;
	}
	
	@Override
	public boolean storeMe()
	{
		try (Connection con = DatabaseFactory.getConnection();
			Statement del = con.createStatement();
			PreparedStatement st = con.prepareStatement(INSERT_QUERY))
		{
			// Clear previous entries.
			del.execute(DELETE_QUERY);
			
			// Insert all variables.
			for (Entry<String, Object> entry : getSet().entrySet())
			{
				st.setString(1, entry.getKey());
				st.setString(2, String.valueOf(entry.getValue()));
				st.addBatch();
			}
			st.executeBatch();
		}
		catch (SQLException e)
		{
			LOGGER.log(Level.WARNING, getClass().getSimpleName() + ": Couldn't save global variables to database.", e);
			return false;
		}
		
		LOGGER.info(getClass().getSimpleName() + ": Stored " + getSet().size() + " variables.");
		return true;
	}
	
	@Override
	public boolean deleteMe()
	{
		try (Connection con = DatabaseFactory.getConnection();
			Statement del = con.createStatement())
		{
			del.execute(DELETE_QUERY);
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, getClass().getSimpleName() + ": Couldn't delete global variables to database.", e);
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the single instance of {@code GlobalVariablesManager}.
	 * @return single instance of {@code GlobalVariablesManager}
	 */
	public static GlobalVariablesManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final GlobalVariablesManager INSTANCE = new GlobalVariablesManager();
	}
}