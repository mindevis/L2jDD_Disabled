
package org.l2jdd.gameserver.network.serverpackets.teleports;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class ExShowTeleportUi implements IClientOutgoingPacket
{
	public static final ExShowTeleportUi STATIC_PACKET = new ExShowTeleportUi();
	
	public ExShowTeleportUi()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SHOW_TELEPORT_UI.writeId(packet);
		return true;
	}
}