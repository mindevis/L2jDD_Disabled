
package org.l2jdd.gameserver.model.events.impl.creature.npc;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.IBaseEvent;

/**
 * @author Zealar
 */
public class OnNpcTeleport implements IBaseEvent
{
	private final Npc _npc;
	
	public OnNpcTeleport(Npc npc)
	{
		_npc = npc;
	}
	
	public Npc getNpc()
	{
		return _npc;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_NPC_TELEPORT;
	}
}
