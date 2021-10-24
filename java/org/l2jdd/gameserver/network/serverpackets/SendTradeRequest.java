
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class SendTradeRequest implements IClientOutgoingPacket
{
	private final int _senderId;
	
	public SendTradeRequest(int senderId)
	{
		_senderId = senderId;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.TRADE_REQUEST.writeId(packet);
		
		packet.writeD(_senderId);
		return true;
	}
}
