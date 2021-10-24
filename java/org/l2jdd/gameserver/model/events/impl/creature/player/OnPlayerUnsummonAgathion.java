
package org.l2jdd.gameserver.model.events.impl.creature.player;

import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.IBaseEvent;

/**
 * @author Mobius
 */
public class OnPlayerUnsummonAgathion implements IBaseEvent
{
	private final PlayerInstance _player;
	private final int _agathionId;
	
	public OnPlayerUnsummonAgathion(PlayerInstance player, int agathionId)
	{
		_player = player;
		_agathionId = agathionId;
	}
	
	public PlayerInstance getPlayer()
	{
		return _player;
	}
	
	public int getAgathionId()
	{
		return _agathionId;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_PLAYER_UNSUMMON_AGATHION;
	}
}
