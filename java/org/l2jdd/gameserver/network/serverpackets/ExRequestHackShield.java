
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * Trigger packet
 * @author KenM
 */
public class ExRequestHackShield implements IClientOutgoingPacket
{
	public static final ExRequestHackShield STATIC_PACKET = new ExRequestHackShield();
	
	private ExRequestHackShield()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_REQUEST_HACK_SHIELD.writeId(packet);
		
		return true;
	}
}
