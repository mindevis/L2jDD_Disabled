
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class CharCreateOk implements IClientOutgoingPacket
{
	public static final CharCreateOk STATIC_PACKET = new CharCreateOk();
	
	private CharCreateOk()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.CHARACTER_CREATE_SUCCESS.writeId(packet);
		
		packet.writeD(0x01);
		return true;
	}
}
