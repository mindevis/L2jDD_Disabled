
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author -Wooden-
 */
public class PledgeSkillListAdd implements IClientOutgoingPacket
{
	private final int _id;
	private final int _level;
	
	public PledgeSkillListAdd(int id, int level)
	{
		_id = id;
		_level = level;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.PLEDGE_SKILL_LIST_ADD.writeId(packet);
		
		packet.writeD(_id);
		packet.writeD(_level);
		return true;
	}
}