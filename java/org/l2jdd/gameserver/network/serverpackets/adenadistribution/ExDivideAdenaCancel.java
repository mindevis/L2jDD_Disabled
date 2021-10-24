
package org.l2jdd.gameserver.network.serverpackets.adenadistribution;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Sdw
 */
public class ExDivideAdenaCancel implements IClientOutgoingPacket
{
	public static final ExDivideAdenaCancel STATIC_PACKET = new ExDivideAdenaCancel();
	
	private ExDivideAdenaCancel()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_DIVIDE_ADENA_CANCEL.writeId(packet);
		
		packet.writeC(0x00); // TODO: Find me
		return true;
	}
}
