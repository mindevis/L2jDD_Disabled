
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Sdw
 */
public class ExRemoveEnchantSupportItemResult implements IClientOutgoingPacket
{
	public static final ExRemoveEnchantSupportItemResult STATIC_PACKET = new ExRemoveEnchantSupportItemResult();
	
	private ExRemoveEnchantSupportItemResult()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_REMOVE_ENCHANT_SUPPORT_ITEM_RESULT.writeId(packet);
		
		return true;
	}
}
