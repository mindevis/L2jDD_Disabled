
package org.l2jdd.gameserver.network.serverpackets.ceremonyofchaos;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author UnAfraid
 */
public class ExCuriousHouseEnter implements IClientOutgoingPacket
{
	public static final ExCuriousHouseEnter STATIC_PACKET = new ExCuriousHouseEnter();
	
	private ExCuriousHouseEnter()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_CURIOUS_HOUSE_ENTER.writeId(packet);
		return true;
	}
}
