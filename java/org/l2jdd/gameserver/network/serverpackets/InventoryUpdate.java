
package org.l2jdd.gameserver.network.serverpackets;

import java.util.List;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.ItemInfo;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Advi, UnAfraid
 */
public class InventoryUpdate extends AbstractInventoryUpdate
{
	public InventoryUpdate()
	{
	}
	
	public InventoryUpdate(ItemInstance item)
	{
		super(item);
	}
	
	public InventoryUpdate(List<ItemInfo> items)
	{
		super(items);
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.INVENTORY_UPDATE.writeId(packet);
		
		writeItems(packet);
		return true;
	}
}
