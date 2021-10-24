
package org.l2jdd.gameserver.network.serverpackets.ceremonyofchaos;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Sdw
 */
public class ExCuriousHouseRemainTime implements IClientOutgoingPacket
{
	private final int _time;
	
	public ExCuriousHouseRemainTime(int time)
	{
		_time = time;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_CURIOUS_HOUSE_REMAIN_TIME.writeId(packet);
		packet.writeD(_time);
		
		return true;
	}
}
