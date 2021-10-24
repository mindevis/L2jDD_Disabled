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
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author devScarlet
 */
public class NicknameChanged implements IClientOutgoingPacket
{
	private final String _title;
	private final int _objectId;
	
	public NicknameChanged(Creature creature)
	{
		_objectId = creature.getObjectId();
		_title = creature.getTitle();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.NICK_NAME_CHANGED.writeId(packet);
		
		packet.writeD(_objectId);
		packet.writeS(_title);
		return true;
	}
}
