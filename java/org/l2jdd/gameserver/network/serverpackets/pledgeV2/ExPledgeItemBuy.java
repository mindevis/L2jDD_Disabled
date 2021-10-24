
package org.l2jdd.gameserver.network.serverpackets.pledgeV2;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class ExPledgeItemBuy implements IClientOutgoingPacket
{
	final int _result;
	
	public ExPledgeItemBuy(int result)
	{
		_result = result;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PLEDGE_ITEM_BUY.writeId(packet);
		packet.writeD(_result); // 0 success, 2 not authorized, 3 trade requirements not met
		return true;
	}
}