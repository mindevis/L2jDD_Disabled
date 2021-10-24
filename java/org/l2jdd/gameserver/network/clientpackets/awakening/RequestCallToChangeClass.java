
package org.l2jdd.gameserver.network.clientpackets.awakening;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.enums.CategoryType;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.EventDispatcher;
import org.l2jdd.gameserver.model.events.impl.creature.player.OnPlayerCallToChangeClass;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;

/**
 * @author Sdw
 */
public class RequestCallToChangeClass implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		// Nothing to read
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
		
		if (player.isDead() || player.isInParty() || (player.getLevel() < 85) || !player.isInCategory(CategoryType.FOURTH_CLASS_GROUP))
		{
			return;
		}
		
		EventDispatcher.getInstance().notifyEventAsync(new OnPlayerCallToChangeClass(player), player);
	}
}
