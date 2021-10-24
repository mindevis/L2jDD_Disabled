
package org.l2jdd.gameserver.model.events.impl.sieges;

import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.IBaseEvent;
import org.l2jdd.gameserver.model.siege.FortSiege;

/**
 * @author UnAfraid
 */
public class OnFortSiegeStart implements IBaseEvent
{
	private final FortSiege _siege;
	
	public OnFortSiegeStart(FortSiege siege)
	{
		_siege = siege;
	}
	
	public FortSiege getSiege()
	{
		return _siege;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_FORT_SIEGE_START;
	}
}
