
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.network.OutgoingPackets;

public class PledgeInfo implements IClientOutgoingPacket
{
	private final Clan _clan;
	
	public PledgeInfo(Clan clan)
	{
		_clan = clan;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.PLEDGE_INFO.writeId(packet);
		
		packet.writeD(Config.SERVER_ID);
		packet.writeD(_clan.getId());
		packet.writeS(_clan.getName());
		packet.writeS(_clan.getAllyName());
		return true;
	}
}
