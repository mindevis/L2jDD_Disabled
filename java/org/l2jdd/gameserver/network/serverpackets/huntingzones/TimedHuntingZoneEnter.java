
package org.l2jdd.gameserver.network.serverpackets.huntingzones;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class TimedHuntingZoneEnter implements IClientOutgoingPacket
{
	private final int _remainingTime;
	
	public TimedHuntingZoneEnter(int remainingTime)
	{
		_remainingTime = remainingTime;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_TIME_RESTRICT_FIELD_USER_ENTER.writeId(packet);
		packet.writeC(_remainingTime);
		return true;
	}
}