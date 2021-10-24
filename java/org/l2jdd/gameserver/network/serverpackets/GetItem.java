
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class GetItem implements IClientOutgoingPacket
{
	private final ItemInstance _item;
	private final int _playerId;
	
	public GetItem(ItemInstance item, int playerId)
	{
		_item = item;
		_playerId = playerId;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.GET_ITEM.writeId(packet);
		
		packet.writeD(_playerId);
		packet.writeD(_item.getObjectId());
		
		packet.writeD(_item.getX());
		packet.writeD(_item.getY());
		packet.writeD(_item.getZ());
		return true;
	}
}
