
package org.l2jdd.gameserver.network.serverpackets.compound;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author UnAfraid
 */
public class ExEnchantOneRemoveFail implements IClientOutgoingPacket
{
	public static final ExEnchantOneRemoveFail STATIC_PACKET = new ExEnchantOneRemoveFail();
	
	private ExEnchantOneRemoveFail()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_ENCHANT_ONE_REMOVE_FAIL.writeId(packet);
		return true;
	}
}
