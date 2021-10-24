
package org.l2jdd.gameserver.network.serverpackets.compound;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author UnAfraid
 */
public class ExEnchantOneFail implements IClientOutgoingPacket
{
	public static final ExEnchantOneFail STATIC_PACKET = new ExEnchantOneFail();
	
	private ExEnchantOneFail()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_ENCHANT_ONE_FAIL.writeId(packet);
		return true;
	}
}
