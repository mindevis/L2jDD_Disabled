
package org.l2jdd.gameserver.network.serverpackets.pledgeV2;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class ExPledgeSkillActivate implements IClientOutgoingPacket
{
	private final int _specialtyId;
	
	public ExPledgeSkillActivate(int specialtyId)
	{
		_specialtyId = specialtyId;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PLEDGE_SKILL_ACTIVATE.writeId(packet);
		packet.writeC(_specialtyId);
		return true;
	}
}
