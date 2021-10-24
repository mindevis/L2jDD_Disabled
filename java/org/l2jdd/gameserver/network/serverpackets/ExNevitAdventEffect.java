
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author mochitto
 */
public class ExNevitAdventEffect implements IClientOutgoingPacket
{
	private final int _timeLeft;
	
	public ExNevitAdventEffect(int timeLeft)
	{
		_timeLeft = timeLeft;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SHOW_CHANNELING_EFFECT.writeId(packet);
		
		packet.writeD(_timeLeft);
		return true;
	}
}
