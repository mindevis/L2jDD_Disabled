
package org.l2jdd.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.model.TimeStamp;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * Skill Cool Time server packet implementation.
 * @author KenM, Zoey76, Mobius
 */
public class SkillCoolTime implements IClientOutgoingPacket
{
	private final long _currentTime;
	private final List<TimeStamp> _skillReuseTimeStamps = new ArrayList<>();
	
	public SkillCoolTime(PlayerInstance player)
	{
		_currentTime = Chronos.currentTimeMillis();
		for (TimeStamp ts : player.getSkillReuseTimeStamps().values())
		{
			if ((_currentTime < ts.getStamp()) && !SkillData.getInstance().getSkill(ts.getSkillId(), ts.getSkillLevel(), ts.getSkillSubLevel()).isNotBroadcastable())
			{
				_skillReuseTimeStamps.add(ts);
			}
		}
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.SKILL_COOL_TIME.writeId(packet);
		
		packet.writeD(_skillReuseTimeStamps.size());
		for (TimeStamp ts : _skillReuseTimeStamps)
		{
			packet.writeD(ts.getSkillId());
			packet.writeD(0x00); // ?
			packet.writeD((int) ts.getReuse() / 1000);
			packet.writeD((int) Math.max(ts.getStamp() - _currentTime, 0) / 1000);
		}
		return true;
	}
}
