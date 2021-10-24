
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.MovieHolder;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author St3eT
 */
public class RequestExEscapeScene implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final MovieHolder holder = player.getMovieHolder();
		if (holder == null)
		{
			return;
		}
		holder.playerEscapeVote(player);
	}
}