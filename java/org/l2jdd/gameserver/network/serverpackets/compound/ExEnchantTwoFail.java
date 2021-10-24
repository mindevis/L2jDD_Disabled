
package org.l2jdd.gameserver.network.serverpackets.compound;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author UnAfraid
 */
public class ExEnchantTwoFail implements IClientOutgoingPacket
{
	public static final ExEnchantTwoFail STATIC_PACKET = new ExEnchantTwoFail();
	
	private ExEnchantTwoFail()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_ENCHANT_TWO_FAIL.writeId(packet);
		return true;
	}
}
