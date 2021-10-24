
package org.l2jdd.gameserver.network.serverpackets.autoplay;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class ExAutoPlayDoMacro implements IClientOutgoingPacket
{
	public static final ExAutoPlayDoMacro STATIC_PACKET = new ExAutoPlayDoMacro();
	
	public ExAutoPlayDoMacro()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_AUTOPLAY_DO_MACRO.writeId(packet);
		packet.writeD(0x114);
		return true;
	}
}
