
package org.l2jdd.gameserver.network.serverpackets.primeshop;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Gnacik, UnAfraid
 */
public class ExBRGamePoint implements IClientOutgoingPacket
{
	private final int _charId;
	private final int _charPoints;
	
	public ExBRGamePoint(PlayerInstance player)
	{
		_charId = player.getObjectId();
		_charPoints = player.getPrimePoints();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_BR_GAME_POINT.writeId(packet);
		
		packet.writeD(_charId);
		packet.writeQ(_charPoints);
		packet.writeD(0x00);
		return true;
	}
}
