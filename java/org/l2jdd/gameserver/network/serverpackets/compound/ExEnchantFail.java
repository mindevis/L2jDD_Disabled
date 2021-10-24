
package org.l2jdd.gameserver.network.serverpackets.compound;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author UnAfraid
 */
public class ExEnchantFail implements IClientOutgoingPacket
{
	public static final ExEnchantFail STATIC_PACKET = new ExEnchantFail(0, 0);
	private final int _itemOne;
	private final int _itemTwo;
	
	public ExEnchantFail(int itemOne, int itemTwo)
	{
		_itemOne = itemOne;
		_itemTwo = itemTwo;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_ENCHANT_FAIL.writeId(packet);
		
		packet.writeD(_itemOne);
		packet.writeD(_itemTwo);
		return true;
	}
}
