
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Kerberos
 */
public class AcquireSkillDone implements IClientOutgoingPacket
{
	public AcquireSkillDone()
	{
		//
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.ACQUIRE_SKILL_DONE.writeId(packet);
		return true;
	}
}