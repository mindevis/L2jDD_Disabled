
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;

/**
 * format ch c: (id) 0xD0 h: (subid) 0x12
 * @author -Wooden-
 */
public class RequestOlympiadObserverEnd implements IClientIncomingPacket
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
		
		if (player.inObserverMode())
		{
			player.leaveOlympiadObserverMode();
		}
	}
}