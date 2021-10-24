
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.enums.Movie;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author JIV
 */
public class ExStartScenePlayer implements IClientOutgoingPacket
{
	private final Movie _movie;
	
	public ExStartScenePlayer(Movie movie)
	{
		_movie = movie;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_START_SCENE_PLAYER.writeId(packet);
		
		packet.writeD(_movie.getClientId());
		return true;
	}
}
