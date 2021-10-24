
package org.l2jdd.gameserver.network.serverpackets.compound;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author UnAfraid
 */
public class ExEnchantSucess implements IClientOutgoingPacket
{
	private final int _itemId;
	
	public ExEnchantSucess(int itemId)
	{
		_itemId = itemId;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_ENCHANT_SUCESS.writeId(packet);
		
		packet.writeD(_itemId);
		return true;
	}
}
