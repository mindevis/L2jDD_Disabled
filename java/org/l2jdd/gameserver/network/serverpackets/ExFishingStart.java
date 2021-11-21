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
 * Format (ch)ddddd
 * @author -Wooden-
 */
public class ExFishingStart implements IClientOutgoingPacket
{
	private final Creature _creature;
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _fishType;
	@SuppressWarnings("unused")
	private final boolean _isNightLure;
	
	public ExFishingStart(Creature creature, int fishType, int x, int y, int z, boolean isNightLure)
	{
		_creature = creature;
		_fishType = fishType;
		_x = x;
		_y = y;
		_z = z;
		_isNightLure = isNightLure;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_FISHING_START.writeId(packet);
		packet.writeD(_creature.getObjectId());
		packet.writeD(_fishType); // fish type
		packet.writeD(_x); // x poisson
		packet.writeD(_y); // y poisson
		packet.writeD(_z); // z poisson
		packet.writeC(0x00); // night lure
		packet.writeC(0x00); // ??
		packet.writeC((_fishType >= 7) && (_fishType <= 9) ? 0x01 : 0x00); // 0 = day lure 1 = night lure
		packet.writeC(0x00);
		return true;
	}
}
