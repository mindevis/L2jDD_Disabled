
package org.l2jdd.gameserver.model.olympiad;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * @author DS, Zoey76
 */
public class Participant
{
	private final int objectId;
	private PlayerInstance player;
	private final String name;
	private final int side;
	private final int baseClass;
	private boolean disconnected = false;
	private boolean defaulted = false;
	private final StatSet stats;
	public String clanName;
	public int clanId;
	
	public Participant(PlayerInstance plr, int olympiadSide)
	{
		objectId = plr.getObjectId();
		player = plr;
		name = plr.getName();
		side = olympiadSide;
		baseClass = plr.getBaseClass();
		stats = Olympiad.getNobleStats(objectId);
		clanName = plr.getClan() != null ? plr.getClan().getName() : "";
		clanId = plr.getClanId();
	}
	
	public Participant(int objId, int olympiadSide)
	{
		objectId = objId;
		player = null;
		name = "-";
		side = olympiadSide;
		baseClass = 0;
		stats = null;
		clanName = "";
		clanId = 0;
	}
	
	/**
	 * Updates the reference to {@link #player}, if it's null or appears off-line.
	 * @return {@code true} if after the update the player isn't null, {@code false} otherwise.
	 */
	public boolean updatePlayer()
	{
		if ((player == null) || !player.isOnline())
		{
			player = World.getInstance().getPlayer(getObjectId());
		}
		return (player != null);
	}
	
	/**
	 * @param statName
	 * @param increment
	 */
	public void updateStat(String statName, int increment)
	{
		stats.set(statName, Math.max(stats.getInt(statName) + increment, 0));
	}
	
	/**
	 * @return the name the player's name.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return the name the player's clan name.
	 */
	public String getClanName()
	{
		return clanName;
	}
	
	/**
	 * @return the name the player's id.
	 */
	public int getClanId()
	{
		return clanId;
	}
	
	/**
	 * @return the player
	 */
	public PlayerInstance getPlayer()
	{
		return player;
	}
	
	/**
	 * @return the objectId
	 */
	public int getObjectId()
	{
		return objectId;
	}
	
	/**
	 * @return the stats
	 */
	public StatSet getStats()
	{
		return stats;
	}
	
	/**
	 * @param noble the player to set
	 */
	public void setPlayer(PlayerInstance noble)
	{
		player = noble;
	}
	
	/**
	 * @return the side
	 */
	public int getSide()
	{
		return side;
	}
	
	/**
	 * @return the baseClass
	 */
	public int getBaseClass()
	{
		return baseClass;
	}
	
	/**
	 * @return the disconnected
	 */
	public boolean isDisconnected()
	{
		return disconnected;
	}
	
	/**
	 * @param value the disconnected to set
	 */
	public void setDisconnected(boolean value)
	{
		disconnected = value;
	}
	
	/**
	 * @return the defaulted
	 */
	public boolean isDefaulted()
	{
		return defaulted;
	}
	
	/**
	 * @param value the value to set.
	 */
	public void setDefaulted(boolean value)
	{
		defaulted = value;
	}
}