
package org.l2jdd.gameserver.model.events.impl.creature;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.IBaseEvent;
import org.l2jdd.gameserver.model.instancezone.Instance;

/**
 * @author Nik
 */
public class OnCreatureTeleport implements IBaseEvent
{
	private final Creature _creature;
	private final int _destX;
	private final int _destY;
	private final int _destZ;
	private final int _destHeading;
	private final Instance _destInstance;
	
	public OnCreatureTeleport(Creature creature, int destX, int destY, int destZ, int destHeading, Instance destInstance)
	{
		_creature = creature;
		_destX = destX;
		_destY = destY;
		_destZ = destZ;
		_destHeading = destHeading;
		_destInstance = destInstance;
	}
	
	public Creature getCreature()
	{
		return _creature;
	}
	
	public int getDestX()
	{
		return _destX;
	}
	
	public int getDestY()
	{
		return _destY;
	}
	
	public int getDestZ()
	{
		return _destZ;
	}
	
	public int getDestHeading()
	{
		return _destHeading;
	}
	
	public Instance getDestInstance()
	{
		return _destInstance;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_CREATURE_TELEPORT;
	}
}
