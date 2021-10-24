
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.enums.CastleSide;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author UnAfraid
 */
public class ExCastleState implements IClientOutgoingPacket
{
	private final int _castleId;
	private final CastleSide _castleSide;
	
	public ExCastleState(Castle castle)
	{
		_castleId = castle.getResidenceId();
		_castleSide = castle.getSide();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_CASTLE_STATE.writeId(packet);
		
		packet.writeD(_castleId);
		packet.writeD(_castleSide.ordinal());
		return true;
	}
}
