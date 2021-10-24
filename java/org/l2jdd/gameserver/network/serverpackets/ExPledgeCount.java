
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author UnAfraid
 */
public class ExPledgeCount implements IClientOutgoingPacket
{
	private final int _count;
	
	public ExPledgeCount(Clan clan)
	{
		_count = clan.getOnlineMembersCount();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PLEDGE_COUNT.writeId(packet);
		
		packet.writeD(_count);
		return true;
	}
}
