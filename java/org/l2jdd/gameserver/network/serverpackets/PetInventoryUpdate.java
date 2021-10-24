
package org.l2jdd.gameserver.network.serverpackets;

import java.util.List;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.ItemInfo;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Yme, Advi, UnAfraid
 */
public class PetInventoryUpdate extends AbstractInventoryUpdate
{
	public PetInventoryUpdate()
	{
	}
	
	public PetInventoryUpdate(ItemInstance item)
	{
		super(item);
	}
	
	public PetInventoryUpdate(List<ItemInfo> items)
	{
		super(items);
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.PET_INVENTORY_UPDATE.writeId(packet);
		
		writeItems(packet);
		return true;
	}
}
