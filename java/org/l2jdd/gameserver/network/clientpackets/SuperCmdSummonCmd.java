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
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.network.GameClient;

/**
 * Format chS c: (id) 0x39 h: (subid) 0x01 S: the summon name (or maybe cmd string ?)
 * @author -Wooden-
 */
public class SuperCmdSummonCmd implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private String _summonName;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_summonName = packet.readS();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
	}
}
