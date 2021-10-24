
package org.l2jdd.gameserver.network.serverpackets.pledgeV2;

import java.util.Collection;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.data.xml.DailyMissionData;
import org.l2jdd.gameserver.model.DailyMissionDataHolder;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class ExPledgeMissionInfo implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	private final Collection<DailyMissionDataHolder> _rewards;
	
	public ExPledgeMissionInfo(PlayerInstance player)
	{
		_player = player;
		_rewards = DailyMissionData.getInstance().getDailyMissionData(player);
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		if (!DailyMissionData.getInstance().isAvailable() || (_player.getClan() == null))
		{
			return true;
		}
		
		OutgoingPackets.EX_PLEDGE_MISSION_INFO.writeId(packet);
		
		packet.writeD(_rewards.size());
		for (DailyMissionDataHolder reward : _rewards)
		{
			int progress = reward.getProgress(_player);
			int status = reward.getStatus(_player);
			
			// TODO: Figure out this.
			if (reward.isLevelUpMission())
			{
				progress = 1;
				if (status == 2)
				{
					status = reward.getRequiredCompletions() > _player.getLevel() ? 1 : 3;
				}
				else
				{
					status = reward.getRecentlyCompleted(_player) ? 0 : 3;
				}
			}
			else if (status == 1)
			{
				status = 3;
			}
			else if (status == 3)
			{
				status = 2;
			}
			
			packet.writeD(reward.getId());
			packet.writeD(progress);
			packet.writeC(status);
		}
		
		return true;
	}
}
