
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.GameTimeController;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class ClientSetTime implements IClientOutgoingPacket
{
	public static final ClientSetTime STATIC_PACKET = new ClientSetTime();
	
	private ClientSetTime()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.CLIENT_SET_TIME.writeId(packet);
		
		packet.writeD(GameTimeController.getInstance().getGameTime()); // time in client minutes
		packet.writeD(6); // constant to match the server time( this determines the speed of the client clock)
		return true;
	}
}