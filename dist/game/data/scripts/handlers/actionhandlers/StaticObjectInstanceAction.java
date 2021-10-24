
package handlers.actionhandlers;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.cache.HtmCache;
import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.handler.IActionHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.actor.instance.StaticObjectInstance;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;

public class StaticObjectInstanceAction implements IActionHandler
{
	@Override
	public boolean action(PlayerInstance player, WorldObject target, boolean interact)
	{
		final StaticObjectInstance staticObject = (StaticObjectInstance) target;
		if (staticObject.getType() < 0)
		{
			LOGGER.info("StaticObjectInstance: StaticObject with invalid type! StaticObjectId: " + staticObject.getId());
		}
		
		// Check if the PlayerInstance already target the NpcInstance
		if (player.getTarget() != staticObject)
		{
			// Set the target of the PlayerInstance player
			player.setTarget(staticObject);
		}
		else if (interact)
		{
			// Calculate the distance between the PlayerInstance and the NpcInstance
			if (!player.isInsideRadius2D(staticObject, Npc.INTERACTION_DISTANCE))
			{
				// Notify the PlayerInstance AI with AI_INTENTION_INTERACT
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, staticObject);
			}
			else if (staticObject.getType() == 2)
			{
				final String filename = (staticObject.getId() == 24230101) ? "data/html/signboard/tomb_of_crystalgolem.htm" : "data/html/signboard/pvp_signboard.htm";
				final String content = HtmCache.getInstance().getHtm(player, filename);
				final NpcHtmlMessage html = new NpcHtmlMessage(staticObject.getObjectId());
				
				if (content == null)
				{
					html.setHtml("<html><body>Signboard is missing:<br>" + filename + "</body></html>");
				}
				else
				{
					html.setHtml(content);
				}
				
				player.sendPacket(html);
			}
			else if (staticObject.getType() == 0)
			{
				player.sendPacket(staticObject.getMap());
			}
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.StaticObjectInstance;
	}
}
