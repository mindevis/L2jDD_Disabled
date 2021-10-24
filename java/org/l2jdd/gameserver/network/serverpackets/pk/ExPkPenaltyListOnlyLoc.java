
package org.l2jdd.gameserver.network.serverpackets.pk;

import java.util.Set;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class ExPkPenaltyListOnlyLoc implements IClientOutgoingPacket
{
	public ExPkPenaltyListOnlyLoc()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_PK_PENALTY_LIST_ONLY_LOC.writeId(packet);
		
		final Set<PlayerInstance> players = World.getInstance().getPkPlayers();
		packet.writeD(World.getInstance().getLastPkTime());
		packet.writeD(players.size());
		for (PlayerInstance player : players)
		{
			packet.writeD(player.getObjectId());
			packet.writeD(player.getX());
			packet.writeD(player.getY());
			packet.writeD(player.getZ());
		}
		
		return true;
	}
}
