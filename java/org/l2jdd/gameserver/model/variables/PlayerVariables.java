
package org.l2jdd.gameserver.model.variables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.l2jdd.commons.database.DatabaseFactory;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.util.Util;

/**
 * @author UnAfraid
 */
public class PlayerVariables extends AbstractVariables
{
	private static final Logger LOGGER = Logger.getLogger(PlayerVariables.class.getName());
	
	// SQL Queries.
	private static final String SELECT_QUERY = "SELECT * FROM character_variables WHERE charId = ?";
	private static final String DELETE_QUERY = "DELETE FROM character_variables WHERE charId = ?";
	private static final String INSERT_QUERY = "INSERT INTO character_variables (charId, var, val) VALUES (?, ?, ?)";
	
	// Public variable names
	public static final String HAIR_ACCESSORY_VARIABLE_NAME = "HAIR_ACCESSORY_ENABLED";
	public static final String WORLD_CHAT_VARIABLE_NAME = "WORLD_CHAT_USED";
	public static final String VITALITY_ITEMS_USED_VARIABLE_NAME = "VITALITY_ITEMS_USED";
	private static final String DAILY_MISSION_REWARDS = "DAILY_MISSION_REWARDS";
	public static final String CEREMONY_OF_CHAOS_PROHIBITED_PENALTIES = "CEREMONY_OF_CHAOS_PENALTIES";
	public static final String CEREMONY_OF_CHAOS_MARKS = "CEREMONY_OF_CHAOS_MARKS";
	public static final String ABILITY_POINTS_MAIN_CLASS = "ABILITY_POINTS";
	public static final String ABILITY_POINTS_DUAL_CLASS = "ABILITY_POINTS_DUAL_CLASS";
	public static final String ABILITY_POINTS_USED_MAIN_CLASS = "ABILITY_POINTS_USED";
	public static final String ABILITY_POINTS_USED_DUAL_CLASS = "ABILITY_POINTS_DUAL_CLASS_USED";
	public static final String REVELATION_SKILL_1_MAIN_CLASS = "RevelationSkill1";
	public static final String REVELATION_SKILL_2_MAIN_CLASS = "RevelationSkill2";
	public static final String REVELATION_SKILL_1_DUAL_CLASS = "DualclassRevelationSkill1";
	public static final String REVELATION_SKILL_2_DUAL_CLASS = "DualclassRevelationSkill2";
	public static final String FORTUNE_TELLING_VARIABLE = "FortuneTelling";
	public static final String FORTUNE_TELLING_BLACK_CAT_VARIABLE = "FortuneTellingBlackCat";
	public static final String DELUSION_RETURN = "DELUSION_RETURN";
	public static final String HUNTING_ZONE_ENTRY = "HUNTING_ZONE_ENTRY_";
	public static final String HUNTING_ZONE_TIME = "HUNTING_ZONE_TIME_";
	public static final String FAVORITE_TELEPORTS = "FAVORITE_TELEPORTS";
	public static final String HOMUNCULUS_STATUS = "HOMUNCULUS_STATUS";
	public static final String HOMUNCULUS_TIME = "HOMUNCULUS_TIME";
	public static final String HOMUNCULUS_HP = "HOMUNCULUS_HP";
	public static final String HOMUNCULUS_SP = "HOMUNCULUS_SP";
	public static final String HOMUNCULUS_VP = "HOMUNCULUS_VP";
	public static final String HOMUNCULUS_ID = "HOMUNCULUS";
	public static final String HOMUNCULUS_QUALITY = "HOMUNCULUS_QUALITY";
	
	private final int _objectId;
	
	public PlayerVariables(int objectId)
	{
		_objectId = objectId;
		restoreMe();
	}
	
	@Override
	public boolean restoreMe()
	{
		// Restore previous variables.
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement st = con.prepareStatement(SELECT_QUERY))
		{
			st.setInt(1, _objectId);
			try (ResultSet rset = st.executeQuery())
			{
				while (rset.next())
				{
					set(rset.getString("var"), rset.getString("val"));
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.log(Level.WARNING, getClass().getSimpleName() + ": Couldn't restore variables for: " + getPlayer(), e);
			return false;
		}
		finally
		{
			compareAndSetChanges(true, false);
		}
		return true;
	}
	
	@Override
	public boolean storeMe()
	{
		// No changes, nothing to store.
		if (!hasChanges())
		{
			return false;
		}
		
		try (Connection con = DatabaseFactory.getConnection())
		{
			// Clear previous entries.
			try (PreparedStatement st = con.prepareStatement(DELETE_QUERY))
			{
				st.setInt(1, _objectId);
				st.execute();
			}
			
			// Insert all variables.
			try (PreparedStatement st = con.prepareStatement(INSERT_QUERY))
			{
				st.setInt(1, _objectId);
				for (Entry<String, Object> entry : getSet().entrySet())
				{
					st.setString(2, entry.getKey());
					st.setString(3, String.valueOf(entry.getValue()));
					st.addBatch();
				}
				st.executeBatch();
			}
		}
		catch (SQLException e)
		{
			LOGGER.log(Level.WARNING, getClass().getSimpleName() + ": Couldn't update variables for: " + getPlayer(), e);
			return false;
		}
		finally
		{
			compareAndSetChanges(true, false);
		}
		return true;
	}
	
	@Override
	public boolean deleteMe()
	{
		try (Connection con = DatabaseFactory.getConnection())
		{
			// Clear previous entries.
			try (PreparedStatement st = con.prepareStatement(DELETE_QUERY))
			{
				st.setInt(1, _objectId);
				st.execute();
			}
			
			// Clear all entries
			getSet().clear();
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, getClass().getSimpleName() + ": Couldn't delete variables for: " + getPlayer(), e);
			return false;
		}
		return true;
	}
	
	public PlayerInstance getPlayer()
	{
		return World.getInstance().getPlayer(_objectId);
	}
	
	public void addDailyMissionReward(int rewardId)
	{
		String result = getString(DAILY_MISSION_REWARDS, "");
		if (result.isEmpty())
		{
			result = Integer.toString(rewardId);
		}
		else
		{
			result += "," + rewardId;
		}
		set(DAILY_MISSION_REWARDS, result);
	}
	
	public void removeDailyMissionReward(int rewardId)
	{
		String result = "";
		final String data = getString(DAILY_MISSION_REWARDS, "");
		for (String s : data.split(","))
		{
			if (s.equals(Integer.toString(rewardId)))
			{
				continue;
			}
			if (result.isEmpty())
			{
				result = s;
			}
			else
			{
				result += "," + s;
			}
		}
		set(DAILY_MISSION_REWARDS, result);
	}
	
	public boolean hasDailyMissionReward(int rewardId)
	{
		final String data = getString(DAILY_MISSION_REWARDS, "");
		for (String s : data.split(","))
		{
			if (s.equals(Integer.toString(rewardId)))
			{
				return true;
			}
		}
		return false;
	}
	
	public List<Integer> getDailyMissionRewards()
	{
		List<Integer> rewards = null;
		final String data = getString(DAILY_MISSION_REWARDS, "");
		if (!data.isEmpty())
		{
			for (String s : getString(DAILY_MISSION_REWARDS, "").split(","))
			{
				if (Util.isDigit(s))
				{
					final int rewardId = Integer.parseInt(s);
					if (rewards == null)
					{
						rewards = new ArrayList<>();
					}
					rewards.add(rewardId);
				}
			}
		}
		return rewards != null ? rewards : Collections.emptyList();
	}
}
