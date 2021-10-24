
package org.l2jdd.gameserver.model.events.impl.item;

import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.IBaseEvent;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;

/**
 * @author NightBR
 */
public class OnItemAttributeAdd implements IBaseEvent
{
	private final PlayerInstance _player;
	private final ItemInstance _item;
	
	public OnItemAttributeAdd(PlayerInstance player, ItemInstance item)
	{
		_player = player;
		_item = item;
	}
	
	public PlayerInstance getPlayer()
	{
		return _player;
	}
	
	public ItemInstance getItem()
	{
		return _item;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_ITEM_ATTRIBUTE_ADD;
	}
}
