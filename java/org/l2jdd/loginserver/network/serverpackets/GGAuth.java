
package org.l2jdd.loginserver.network.serverpackets;

import org.l2jdd.commons.network.IOutgoingPacket;
import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.loginserver.network.OutgoingPackets;

/**
 * Fromat: d d: response
 */
public class GGAuth implements IOutgoingPacket
{
	private final int _response;
	
	public GGAuth(int response)
	{
		_response = response;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.GG_AUTH.writeId(packet);
		packet.writeD(_response);
		packet.writeD(0x00);
		packet.writeD(0x00);
		packet.writeD(0x00);
		packet.writeD(0x00);
		return true;
	}
}
