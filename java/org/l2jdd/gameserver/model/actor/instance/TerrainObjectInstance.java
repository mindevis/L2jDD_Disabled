
package org.l2jdd.gameserver.model.actor.instance;

import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.templates.NpcTemplate;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;

public class TerrainObjectInstance extends Npc
{
	public TerrainObjectInstance(NpcTemplate template)
	{
		super(template);
		setInstanceType(InstanceType.TerrainObjectInstance);
	}
	
	@Override
	public void onAction(PlayerInstance player, boolean interact)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	@Override
	public void onActionShift(PlayerInstance player)
	{
		if (player.isGM())
		{
			super.onActionShift(player);
		}
		else
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
	}
}