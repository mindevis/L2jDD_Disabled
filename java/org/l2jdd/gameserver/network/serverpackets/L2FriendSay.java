
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * Send Private (Friend) Message
 * @author Tempy
 */
public class L2FriendSay implements IClientOutgoingPacket
{
	private final String _sender;
	private final String _receiver;
	private final String _message;
	
	public L2FriendSay(String sender, String reciever, String message)
	{
		_sender = sender;
		_receiver = reciever;
		_message = message;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.L2_FRIEND_SAY.writeId(packet);
		
		packet.writeD(0); // ??
		packet.writeS(_receiver);
		packet.writeS(_sender);
		packet.writeS(_message);
		return true;
	}
}
