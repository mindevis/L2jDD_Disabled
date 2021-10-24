
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author KenM
 */
public class ExRpItemLink extends AbstractItemPacket
{
	private final ItemInstance _item;
	
	public ExRpItemLink(ItemInstance item)
	{
		_item = item;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_RP_ITEM_LINK.writeId(packet);
		
		writeItem(packet, _item);
		return true;
	}
}
