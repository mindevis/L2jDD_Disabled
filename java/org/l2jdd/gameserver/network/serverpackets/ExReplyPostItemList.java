
package org.l2jdd.gameserver.network.serverpackets;

import java.util.Collection;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Migi, DS
 */
public class ExReplyPostItemList extends AbstractItemPacket
{
	private final int _sendType;
	private final PlayerInstance _player;
	private final Collection<ItemInstance> _itemList;
	
	public ExReplyPostItemList(int sendType, PlayerInstance player)
	{
		_sendType = sendType;
		_player = player;
		_itemList = _player.getInventory().getAvailableItems(true, false, false);
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_REPLY_POST_ITEM_LIST.writeId(packet);
		packet.writeC(_sendType);
		packet.writeD(_itemList.size());
		if (_sendType == 2)
		{
			packet.writeD(_itemList.size());
			for (ItemInstance item : _itemList)
			{
				writeItem(packet, item);
			}
		}
		return true;
	}
}
