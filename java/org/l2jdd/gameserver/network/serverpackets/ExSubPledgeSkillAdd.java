
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author VISTALL
 */
public class ExSubPledgeSkillAdd implements IClientOutgoingPacket
{
	private final int _type;
	private final int _skillId;
	private final int _skillLevel;
	
	public ExSubPledgeSkillAdd(int type, int skillId, int skillLevel)
	{
		_type = type;
		_skillId = skillId;
		_skillLevel = skillLevel;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SUB_PLEDGET_SKILL_ADD.writeId(packet);
		
		packet.writeD(_type);
		packet.writeD(_skillId);
		packet.writeD(_skillLevel);
		return true;
	}
}
