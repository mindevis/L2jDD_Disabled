
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.TradeItem;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Yme
 */
public class TradeOtherAdd extends AbstractItemPacket
{
	private final int _sendType;
	private final TradeItem _item;
	
	public TradeOtherAdd(int sendType, TradeItem item)
	{
		_sendType = sendType;
		_item = item;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.TRADE_OTHER_ADD.writeId(packet);
		packet.writeC(_sendType);
		if (_sendType == 2)
		{
			packet.writeD(0x01);
		}
		packet.writeD(0x01);
		writeItem(packet, _item);
		return true;
	}
}
