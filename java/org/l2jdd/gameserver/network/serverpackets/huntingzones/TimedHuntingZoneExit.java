
package org.l2jdd.gameserver.network.serverpackets.huntingzones;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class TimedHuntingZoneExit implements IClientOutgoingPacket
{
	public static final TimedHuntingZoneExit STATIC_PACKET = new TimedHuntingZoneExit();
	
	public TimedHuntingZoneExit()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_TIME_RESTRICT_FIELD_USER_EXIT.writeId(packet);
		return true;
	}
}