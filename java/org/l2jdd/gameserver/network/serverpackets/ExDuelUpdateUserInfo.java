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
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * Format: ch Sddddddddd.
 * @author KenM
 */
public class ExDuelUpdateUserInfo implements IClientOutgoingPacket
{
	/** The _active char. */
	private final PlayerInstance _player;
	
	/**
	 * Instantiates a new ex duel update user info.
	 * @param player the cha
	 */
	public ExDuelUpdateUserInfo(PlayerInstance player)
	{
		_player = player;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_DUEL_UPDATE_USER_INFO.writeId(packet);
		packet.writeS(_player.getName());
		packet.writeD(_player.getObjectId());
		packet.writeD(_player.getClassId().getId());
		packet.writeD(_player.getLevel());
		packet.writeD((int) _player.getCurrentHp());
		packet.writeD(_player.getMaxHp());
		packet.writeD((int) _player.getCurrentMp());
		packet.writeD(_player.getMaxMp());
		packet.writeD((int) _player.getCurrentCp());
		packet.writeD(_player.getMaxCp());
		return true;
	}
}
