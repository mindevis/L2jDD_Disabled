
package handlers.actionhandlers;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.handler.IActionHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

public class ArtefactInstanceAction implements IActionHandler
{
	/**
	 * Manage actions when a player click on the ArtefactInstance.<br>
	 * <br>
	 * <b><u>Actions</u>:</b><br>
	 * <li>Set the NpcInstance as target of the PlayerInstance player (if necessary)</li>
	 * <li>Send a Server->Client packet MyTargetSelected to the PlayerInstance player (display the select window)</li>
	 * <li>Send a Server->Client packet ValidateLocation to correct the NpcInstance position and heading on the client</li><br>
	 * <br>
	 * <b><u>Example of use</u>:</b><br>
	 * <li>Client packet : Action, AttackRequest</li>
	 */
	@Override
	public boolean action(PlayerInstance player, WorldObject target, boolean interact)
	{
		if (!((Npc) target).canTarget(player))
		{
			return false;
		}
		if (player.getTarget() != target)
		{
			player.setTarget(target);
		}
		// Calculate the distance between the PlayerInstance and the NpcInstance
		else if (interact && !((Npc) target).canInteract(player))
		{
			// Notify the PlayerInstance AI with AI_INTENTION_INTERACT
			player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, target);
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.ArtefactInstance;
	}
}