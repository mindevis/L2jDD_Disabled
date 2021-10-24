
package org.l2jdd.gameserver.network.serverpackets.sayune;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author UnAfraid
 */
public class ExNotifyFlyMoveStart implements IClientOutgoingPacket
{
	public static final ExNotifyFlyMoveStart STATIC_PACKET = new ExNotifyFlyMoveStart();
	
	private ExNotifyFlyMoveStart()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_NOTIFY_FLY_MOVE_START.writeId(packet);
		
		return true;
	}
}