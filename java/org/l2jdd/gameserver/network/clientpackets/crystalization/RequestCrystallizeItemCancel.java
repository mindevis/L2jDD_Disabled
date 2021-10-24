
package org.l2jdd.gameserver.network.clientpackets.crystalization;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;

/**
 * @author UnAfraid
 */
public class RequestCrystallizeItemCancel implements IClientIncomingPacket
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
		
		// if (!client.getFloodProtectors().getTransaction().tryPerformAction("crystallize"))
		// {
		// player.sendMessage("You are crystallizing too fast.");
		// return;
		// }
		if (player.isInCrystallize())
		{
			player.setInCrystallize(false);
		}
	}
}
