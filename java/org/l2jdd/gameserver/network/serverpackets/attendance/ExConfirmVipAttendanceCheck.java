
package org.l2jdd.gameserver.network.serverpackets.attendance;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class ExConfirmVipAttendanceCheck implements IClientOutgoingPacket
{
	boolean _available;
	int _index;
	
	public ExConfirmVipAttendanceCheck(boolean rewardAvailable, int rewardIndex)
	{
		_available = rewardAvailable;
		_index = rewardIndex;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_CONFIRM_VIP_ATTENDANCE_CHECK.writeId(packet);
		packet.writeC(_available ? 0x01 : 0x00); // can receive reward today? 1 else 0
		packet.writeC(_index); // active reward index
		packet.writeD(0);
		packet.writeD(0);
		return true;
	}
}
