
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.MovieHolder;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author JIV
 */
public class EndScenePlayer implements IClientIncomingPacket
{
	private int _movieId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_movieId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if ((player == null) || (_movieId == 0))
		{
			return;
		}
		
		final MovieHolder holder = player.getMovieHolder();
		if ((holder == null) || (holder.getMovie().getClientId() != _movieId))
		{
			LOGGER.warning("Player " + client + " sent EndScenePlayer with wrong movie id: " + _movieId);
			return;
		}
		player.stopMovie();
	}
}