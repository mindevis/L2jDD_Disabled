
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author nBd
 */
public class ExPutEnchantSupportItemResult implements IClientOutgoingPacket
{
	private final int _result;
	
	public ExPutEnchantSupportItemResult(int result)
	{
		_result = result;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PUT_ENCHANT_SUPPORT_ITEM_RESULT.writeId(packet);
		
		packet.writeD(_result);
		return true;
	}
}
