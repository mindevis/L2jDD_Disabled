
package handlers.playeractions;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * Servitor move to target player action handler.
 * @author Nik
 */
public class ServitorMove implements IPlayerActionHandler
{
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		if (!player.hasServitors())
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_A_SERVITOR);
			return;
		}
		
		if (player.getTarget() != null)
		{
			for (Summon summon : player.getServitors().values())
			{
				if ((summon != player.getTarget()) && !summon.isMovementDisabled())
				{
					if (summon.isBetrayed())
					{
						player.sendPacket(SystemMessageId.YOUR_PET_SERVITOR_IS_UNRESPONSIVE_AND_WILL_NOT_OBEY_ANY_ORDERS);
						return;
					}
					
					summon.setFollowStatus(false);
					summon.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, player.getTarget().getLocation());
				}
			}
		}
	}
}
