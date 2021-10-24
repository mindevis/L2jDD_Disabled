
package org.l2jdd.gameserver.model.events.impl.creature.player;

import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.IBaseEvent;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author UnAfraid
 */
public class OnPlayerRestore implements IBaseEvent
{
	private final int _objectId;
	private final String _name;
	private final GameClient _client;
	
	public OnPlayerRestore(int objectId, String name, GameClient client)
	{
		_objectId = objectId;
		_name = name;
		_client = client;
	}
	
	public int getObjectId()
	{
		return _objectId;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public GameClient getClient()
	{
		return _client;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_PLAYER_RESTORE;
	}
}
