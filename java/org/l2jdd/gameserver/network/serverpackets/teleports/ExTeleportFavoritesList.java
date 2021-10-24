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
package org.l2jdd.gameserver.network.serverpackets.teleports;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.variables.PlayerVariables;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class ExTeleportFavoritesList implements IClientOutgoingPacket
{
	private final int[] _teleports;
	private final boolean _enable;
	
	public ExTeleportFavoritesList(PlayerInstance player, boolean enable)
	{
		if (player.getVariables().contains(PlayerVariables.FAVORITE_TELEPORTS))
		{
			_teleports = player.getVariables().getIntArray(PlayerVariables.FAVORITE_TELEPORTS, ",");
		}
		else
		{
			_teleports = new int[0];
		}
		_enable = enable;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_TELEPORT_FAVORITES_LIST.writeId(packet);
		
		packet.writeC(_enable ? 0x01 : 0x00);
		packet.writeD(_teleports.length);
		for (int id : _teleports)
		{
			packet.writeD(id);
		}
		
		return true;
	}
}
