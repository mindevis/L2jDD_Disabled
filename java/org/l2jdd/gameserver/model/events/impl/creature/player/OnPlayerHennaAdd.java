
package org.l2jdd.gameserver.model.events.impl.creature.player;

import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.IBaseEvent;
import org.l2jdd.gameserver.model.items.Henna;

/**
 * @author UnAfraid
 */
public class OnPlayerHennaAdd implements IBaseEvent
{
	private final PlayerInstance _player;
	private final Henna _henna;
	
	public OnPlayerHennaAdd(PlayerInstance player, Henna henna)
	{
		_player = player;
		_henna = henna;
	}
	
	public PlayerInstance getPlayer()
	{
		return _player;
	}
	
	public Henna getHenna()
	{
		return _henna;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_PLAYER_HENNA_ADD;
	}
}
