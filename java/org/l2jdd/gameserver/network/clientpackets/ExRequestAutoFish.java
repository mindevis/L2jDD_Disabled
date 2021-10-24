
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author St3eT
 */
public class ExRequestAutoFish implements IClientIncomingPacket
{
	private boolean _start;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_start = packet.readC() != 0;
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
		
		if (_start)
		{
			player.getFishing().startFishing();
		}
		else
		{
			player.getFishing().stopFishing();
		}
	}
}
