
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.TradeItem;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author daemon
 */
public class TradeUpdate extends AbstractItemPacket
{
	private final int _sendType;
	private final TradeItem _item;
	private final long _newCount;
	private final long _count;
	
	public TradeUpdate(int sendType, PlayerInstance player, TradeItem item, long count)
	{
		_sendType = sendType;
		_count = count;
		_item = item;
		_newCount = player == null ? 0 : player.getInventory().getItemByObjectId(item.getObjectId()).getCount() - item.getCount();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.TRADE_UPDATE.writeId(packet);
		packet.writeC(_sendType);
		packet.writeD(0x01);
		if (_sendType == 2)
		{
			packet.writeD(0x01);
			packet.writeH((_newCount > 0) && _item.getItem().isStackable() ? 3 : 2);
			writeItem(packet, _item, _count);
		}
		return true;
	}
}
