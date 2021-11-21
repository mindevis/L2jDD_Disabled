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
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * Format: (ch)ddddd
 */
public class ExConfirmVariationRefiner implements IClientOutgoingPacket
{
	private final int _refinerItemObjId;
	private final int _lifestoneItemId;
	private final int _gemstoneItemId;
	private final int _gemstoneCount;
	private final int _unk2;
	
	public ExConfirmVariationRefiner(int refinerItemObjId, int lifeStoneId, int gemstoneItemId, int gemstoneCount)
	{
		_refinerItemObjId = refinerItemObjId;
		_lifestoneItemId = lifeStoneId;
		_gemstoneItemId = gemstoneItemId;
		_gemstoneCount = gemstoneCount;
		_unk2 = 1;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_CONFIRM_VARIATION_REFINER.writeId(packet);
		packet.writeD(_refinerItemObjId);
		packet.writeD(_lifestoneItemId);
		packet.writeD(_gemstoneItemId);
		packet.writeD(_gemstoneCount);
		packet.writeD(_unk2);
		return true;
	}
}
