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
import org.l2jdd.gameserver.model.actor.instance.DoorInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * 61 d6 6d c0 4b door id 8f 14 00 00 x b7 f1 00 00 y 60 f2 ff ff z 00 00 00 00 ?? format dddd rev 377 ID:%d X:%d Y:%d Z:%d ddddd rev 419
 * @version $Revision: 1.3.2.2.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class DoorStatusUpdate implements IClientOutgoingPacket
{
	private final DoorInstance _door;
	private final PlayerInstance _player;
	
	public DoorStatusUpdate(DoorInstance door, PlayerInstance player)
	{
		_door = door;
		_player = player;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.DOOR_STATUS_UPDATE.writeId(packet);
		packet.writeD(_door.getObjectId());
		packet.writeD(_door.isOpen() ? 0 : 1);
		packet.writeD(_door.getDamage());
		packet.writeD(_door.isEnemyOf(_player) ? 1 : 0);
		packet.writeD(_door.getDoorId());
		packet.writeD(_door.getMaxHp());
		packet.writeD((int) _door.getCurrentHp());
		return true;
	}
}
