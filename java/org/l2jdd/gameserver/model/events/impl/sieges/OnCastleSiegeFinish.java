
package org.l2jdd.gameserver.model.events.impl.sieges;

import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.IBaseEvent;
import org.l2jdd.gameserver.model.siege.Siege;

/**
 * @author UnAfraid
 */
public class OnCastleSiegeFinish implements IBaseEvent
{
	private final Siege _siege;
	
	public OnCastleSiegeFinish(Siege siege)
	{
		_siege = siege;
	}
	
	public Siege getSiege()
	{
		return _siege;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_CASTLE_SIEGE_FINISH;
	}
}
