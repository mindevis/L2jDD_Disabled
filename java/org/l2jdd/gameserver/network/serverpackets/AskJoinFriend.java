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
 * sample
 * <p>
 * 7d c1 b2 e0 4a 00 00 00 00
 * <p>
 * format cdd
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class AskJoinFriend implements IClientOutgoingPacket
{
	private final String _requestorName;
	
	/**
	 * @param requestorName
	 */
	public AskJoinFriend(String requestorName)
	{
		_requestorName = requestorName;
		// _itemDistribution = itemDistribution;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.ASK_JOIN_FRIEND.writeId(packet);
		packet.writeS(_requestorName);
		packet.writeD(0);
		return false;
	}
}