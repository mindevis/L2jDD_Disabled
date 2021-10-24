
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * Trigger packet
 * @author KenM
 */
public class ExShowVariationMakeWindow implements IClientOutgoingPacket
{
	public static final ExShowVariationMakeWindow STATIC_PACKET = new ExShowVariationMakeWindow();
	
	private ExShowVariationMakeWindow()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SHOW_VARIATION_MAKE_WINDOW.writeId(packet);
		
		return true;
	}
}
