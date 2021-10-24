
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Sdw
 */
public class ExDissmissMPCCRoom implements IClientOutgoingPacket
{
	public static final ExDissmissMPCCRoom STATIC_PACKET = new ExDissmissMPCCRoom();
	
	private ExDissmissMPCCRoom()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_DISSMISS_MPCC_ROOM.writeId(packet);
		
		return true;
	}
}
