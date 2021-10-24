
package org.l2jdd.gameserver.network.serverpackets.ceremonyofchaos;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author UnAfraid
 */
public class ExCuriousHouseObserveMode implements IClientOutgoingPacket
{
	public static final ExCuriousHouseObserveMode STATIC_ENABLED = new ExCuriousHouseObserveMode(0);
	public static final ExCuriousHouseObserveMode STATIC_DISABLED = new ExCuriousHouseObserveMode(1);
	
	private final int _spectating;
	
	private ExCuriousHouseObserveMode(int spectating)
	{
		_spectating = spectating;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_CURIOUS_HOUSE_OBSERVE_MODE.writeId(packet);
		packet.writeC(_spectating);
		return true;
	}
}
