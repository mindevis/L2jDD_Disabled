
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * (just a trigger)
 * @author -Wooden-
 */
public class ExMailArrived implements IClientOutgoingPacket
{
	public static final ExMailArrived STATIC_PACKET = new ExMailArrived();
	
	private ExMailArrived()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_MAIL_ARRIVED.writeId(packet);
		
		return true;
	}
}
