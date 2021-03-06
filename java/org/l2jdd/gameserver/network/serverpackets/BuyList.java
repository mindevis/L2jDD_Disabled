
package org.l2jdd.gameserver.network.serverpackets;

import java.util.Collection;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.buylist.Product;
import org.l2jdd.gameserver.model.buylist.ProductList;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class BuyList extends AbstractItemPacket
{
	private final int _listId;
	private final Collection<Product> _list;
	private final long _money;
	private final int _inventorySlots;
	private final double _castleTaxRate;
	
	public BuyList(ProductList list, PlayerInstance player, double castleTaxRate)
	{
		_listId = list.getListId();
		_list = list.getProducts();
		_money = player.getAdena();
		_inventorySlots = player.getInventory().getItems(item -> !item.isQuestItem()).size();
		_castleTaxRate = castleTaxRate;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_BUY_SELL_LIST.writeId(packet);
		
		packet.writeD(0x00); // Type BUY
		packet.writeQ(_money); // current money
		packet.writeD(_listId);
		packet.writeD(_inventorySlots);
		packet.writeH(_list.size());
		for (Product product : _list)
		{
			if ((product.getCount() > 0) || !product.hasLimitedStock())
			{
				writeItem(packet, product);
				packet.writeQ((long) (product.getPrice() * (1.0 + _castleTaxRate + product.getBaseTaxRate())));
			}
		}
		return true;
	}
}
