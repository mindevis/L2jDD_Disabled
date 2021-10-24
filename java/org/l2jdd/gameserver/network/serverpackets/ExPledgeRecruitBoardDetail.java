
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.clan.entry.PledgeRecruitInfo;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Sdw
 */
public class ExPledgeRecruitBoardDetail implements IClientOutgoingPacket
{
	final PledgeRecruitInfo _pledgeRecruitInfo;
	
	public ExPledgeRecruitBoardDetail(PledgeRecruitInfo pledgeRecruitInfo)
	{
		_pledgeRecruitInfo = pledgeRecruitInfo;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PLEDGE_RECRUIT_BOARD_DETAIL.writeId(packet);
		
		packet.writeD(_pledgeRecruitInfo.getClanId());
		packet.writeD(_pledgeRecruitInfo.getKarma());
		packet.writeS(_pledgeRecruitInfo.getInformation());
		packet.writeS(_pledgeRecruitInfo.getDetailedInformation());
		packet.writeD(_pledgeRecruitInfo.getApplicationType());
		packet.writeD(_pledgeRecruitInfo.getRecruitType());
		return true;
	}
}
