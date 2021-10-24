
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class TradeDone implements IClientOutgoingPacket
{
	private final int _num;
	
	public TradeDone(int num)
	{
		_num = num;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.TRADE_DONE.writeId(packet);
		
		packet.writeD(_num);
		return true;
	}
}
