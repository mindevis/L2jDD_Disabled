
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.olympiad.Hero;
import org.l2jdd.gameserver.network.GameClient;

/**
 * Format chS c (id) 0xD0 h (subid) 0x0C S the hero's words :)
 * @author -Wooden-
 */
public class RequestWriteHeroWords implements IClientIncomingPacket
{
	private String _heroWords;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_heroWords = packet.readS();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if ((player == null) || !player.isHero())
		{
			return;
		}
		
		if ((_heroWords == null) || (_heroWords.length() > 300))
		{
			return;
		}
		
		Hero.getInstance().setHeroMessage(player, _heroWords);
	}
}