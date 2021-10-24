
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Sdw
 */
public class ExNewSkillToLearnByLevelUp implements IClientOutgoingPacket
{
	public static final ExNewSkillToLearnByLevelUp STATIC_PACKET = new ExNewSkillToLearnByLevelUp();
	
	private ExNewSkillToLearnByLevelUp()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_NEW_SKILL_TO_LEARN_BY_LEVEL_UP.writeId(packet);
		
		return true;
	}
}
