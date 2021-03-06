
package org.l2jdd.gameserver.model.holders;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.World;

/**
 * @author Mobius
 */
public class TimedHuntingZoneHolder
{
	private final int _id;
	private final String _name;
	private final int _initialTime;
	private final int _maximumAddedTime;
	private final int _resetDelay;
	private final int _entryItemId;
	private final int _entryFee;
	private final int _minLevel;
	private final int _maxLevel;
	private final int _remainRefillTime;
	private final int _refillTimeMax;
	private final boolean _weekly;
	private final Location _enterLocation;
	private final int _mapX;
	private final int _mapY;
	
	public TimedHuntingZoneHolder(int id, String name, int initialTime, int maximumAddedTime, int resetDelay, int entryItemId, int entryFee, int minLevel, int maxLevel, int remainRefillTime, int refillTimeMax, boolean weekly, Location enterLocation)
	{
		_id = id;
		_name = name;
		_initialTime = initialTime;
		_maximumAddedTime = maximumAddedTime;
		_resetDelay = resetDelay;
		_entryItemId = entryItemId;
		_entryFee = entryFee;
		_minLevel = minLevel;
		_maxLevel = maxLevel;
		_remainRefillTime = remainRefillTime;
		_refillTimeMax = refillTimeMax;
		_weekly = weekly;
		_enterLocation = enterLocation;
		_mapX = ((_enterLocation.getX() - World.WORLD_X_MIN) >> 15) + World.TILE_X_MIN;
		_mapY = ((_enterLocation.getY() - World.WORLD_Y_MIN) >> 15) + World.TILE_Y_MIN;
	}
	
	public int getZoneId()
	{
		return _id;
	}
	
	public String getZoneName()
	{
		return _name;
	}
	
	public int getInitialTime()
	{
		return _initialTime;
	}
	
	public int getMaximumAddedTime()
	{
		return _maximumAddedTime;
	}
	
	public int getResetDelay()
	{
		return _resetDelay;
	}
	
	public int getEntryItemId()
	{
		return _entryItemId;
	}
	
	public int getEntryFee()
	{
		return _entryFee;
	}
	
	public int getMinLevel()
	{
		return _minLevel;
	}
	
	public int getMaxLevel()
	{
		return _maxLevel;
	}
	
	public int getRemainRefillTime()
	{
		return _remainRefillTime;
	}
	
	public int getRefillTimeMax()
	{
		return _refillTimeMax;
	}
	
	public boolean isWeekly()
	{
		return _weekly;
	}
	
	public Location getEnterLocation()
	{
		return _enterLocation;
	}
	
	public int getMapX()
	{
		return _mapX;
	}
	
	public int getMapY()
	{
		return _mapY;
	}
}
