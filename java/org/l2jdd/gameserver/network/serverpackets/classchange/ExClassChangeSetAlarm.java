
package org.l2jdd.gameserver.network.serverpackets.classchange;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class ExClassChangeSetAlarm implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC_PACKET = new ExClassChangeSetAlarm();
	
	public ExClassChangeSetAlarm()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_CLASS_CHANGE_SET_ALARM.writeId(packet);
		return true;
	}
}
