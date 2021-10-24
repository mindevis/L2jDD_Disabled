
package org.l2jdd.gameserver.network.serverpackets.ensoul;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author UnAfraid
 */
public class ExShowEnsoulWindow implements IClientOutgoingPacket
{
	public static final ExShowEnsoulWindow STATIC_PACKET = new ExShowEnsoulWindow();
	
	private ExShowEnsoulWindow()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SHOW_ENSOUL_WINDOW.writeId(packet);
		return true;
	}
}
