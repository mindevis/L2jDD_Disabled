
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author -Wooden-
 */
public class ExSearchOrc implements IClientOutgoingPacket
{
	public static final ExSearchOrc STATIC_PACKET = new ExSearchOrc();
	
	private ExSearchOrc()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SEARCH_ORC.writeId(packet);
		
		return true;
	}
}
