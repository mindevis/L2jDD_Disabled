
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.serverpackets.UserInfo;

public class RequestRecordInfo implements IClientIncomingPacket
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
		
		client.sendPacket(new UserInfo(player));
		World.getInstance().forEachVisibleObject(player, WorldObject.class, object ->
		{
			if (object.isVisibleFor(player))
			{
				object.sendInfo(player);
				
				if (object.isCreature())
				{
					// Update the state of the Creature object client side by sending Server->Client packet
					// MoveToPawn/CharMoveToLocation and AutoAttackStart to the PlayerInstance
					final Creature creature = (Creature) object;
					if (creature.hasAI())
					{
						creature.getAI().describeStateToPlayer(player);
					}
				}
			}
		});
	}
}
