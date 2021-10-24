
package org.l2jdd.gameserver.network.serverpackets.friend;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

public class FriendAddRequest implements IClientOutgoingPacket
{
	private final String _requestorName;
	
	public FriendAddRequest(String requestorName)
	{
		_requestorName = requestorName;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.FRIEND_ADD_REQUEST.writeId(packet);
		
		packet.writeC(0x00);
		packet.writeS(_requestorName);
		return true;
	}
}
