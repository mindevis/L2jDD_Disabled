
package org.l2jdd.gameserver.network.serverpackets.teleports;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.variables.PlayerVariables;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class ExTeleportFavoritesList implements IClientOutgoingPacket
{
	private final int[] _teleports;
	private final boolean _enable;
	
	public ExTeleportFavoritesList(PlayerInstance player, boolean enable)
	{
		if (player.getVariables().contains(PlayerVariables.FAVORITE_TELEPORTS))
		{
			_teleports = player.getVariables().getIntArray(PlayerVariables.FAVORITE_TELEPORTS, ",");
		}
		else
		{
			_teleports = new int[0];
		}
		_enable = enable;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_TELEPORT_FAVORITES_LIST.writeId(packet);
		
		packet.writeC(_enable ? 0x01 : 0x00);
		packet.writeD(_teleports.length);
		for (int id : _teleports)
		{
			packet.writeD(id);
		}
		
		return true;
	}
}
