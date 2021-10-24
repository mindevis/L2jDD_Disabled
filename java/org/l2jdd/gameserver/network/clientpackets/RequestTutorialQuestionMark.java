
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.EventDispatcher;
import org.l2jdd.gameserver.model.events.impl.creature.player.OnPlayerPressTutorialMark;
import org.l2jdd.gameserver.network.GameClient;

public class RequestTutorialQuestionMark implements IClientIncomingPacket
{
	private int _number = 0;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		packet.readC(); // index ?
		_number = packet.readD();
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
		
		// Notify scripts
		EventDispatcher.getInstance().notifyEventAsync(new OnPlayerPressTutorialMark(player, _number), player);
	}
}
