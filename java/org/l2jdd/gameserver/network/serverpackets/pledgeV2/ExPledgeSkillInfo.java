
package org.l2jdd.gameserver.network.serverpackets.pledgeV2;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class ExPledgeSkillInfo implements IClientOutgoingPacket
{
	private final int _skillId;
	private final int _skillLevel;
	private final int _timeLeft;
	private final int _availability;
	
	public ExPledgeSkillInfo(int skillId, int skillLevel, int timeLeft, int availability)
	{
		_skillId = skillId;
		_skillLevel = skillLevel;
		_timeLeft = timeLeft;
		_availability = availability;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PLEDGE_SKILL_INFO.writeId(packet);
		packet.writeD(_skillId);
		packet.writeD(_skillLevel);
		packet.writeD(_timeLeft);
		packet.writeC(_availability);
		return true;
	}
}
