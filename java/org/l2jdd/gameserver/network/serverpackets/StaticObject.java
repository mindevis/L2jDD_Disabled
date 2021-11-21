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
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.StaticObjectInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class StaticObject implements IClientOutgoingPacket
{
	private final StaticObjectInstance _staticObject;
	
	/**
	 * [S]0x99 StaticObjectPacket dd
	 * @param staticObject
	 */
	public StaticObject(StaticObjectInstance staticObject)
	{
		_staticObject = staticObject; // staticObjectId
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.STATIC_OBJECT.writeId(packet);
		packet.writeD(_staticObject.getStaticObjectId()); // staticObjectId
		packet.writeD(_staticObject.getObjectId()); // objectId
		return true;
	}
}
