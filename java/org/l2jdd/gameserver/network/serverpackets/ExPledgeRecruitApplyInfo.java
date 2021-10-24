
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.enums.ClanEntryStatus;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Sdw
 */
public class ExPledgeRecruitApplyInfo implements IClientOutgoingPacket
{
	private final ClanEntryStatus _status;
	
	public ExPledgeRecruitApplyInfo(ClanEntryStatus status)
	{
		_status = status;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PLEDGE_RECRUIT_APPLY_INFO.writeId(packet);
		
		packet.writeD(_status.ordinal());
		return true;
	}
}
