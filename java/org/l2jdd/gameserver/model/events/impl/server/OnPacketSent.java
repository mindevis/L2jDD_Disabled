/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jdd.gameserver.model.events.impl.server;

import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.IBaseEvent;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author UnAfraid
 */
public class OnPacketSent implements IBaseEvent
{
	private final GameClient _client;
	private final byte[] _data;
	
	public OnPacketSent(GameClient client, byte[] data)
	{
		_client = client;
		_data = data;
	}
	
	public PlayerInstance getActiveChar()
	{
		return _client.getPlayer();
	}
	
	public GameClient getClient()
	{
		return _client;
	}
	
	public byte[] getData()
	{
		return _data;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_PACKET_SENT;
	}
}