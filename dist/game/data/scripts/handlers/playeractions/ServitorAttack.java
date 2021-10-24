
package handlers.playeractions;

import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * Servitor Attack player action handler.
 * @author St3eT
 */
public class ServitorAttack implements IPlayerActionHandler
{
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		if (player.hasServitors())
		{
			for (Summon summon : player.getServitors().values())
			{
				if (summon.canAttack(player.getTarget(), ctrlPressed))
				{
					// Prevent spamming next target and attack to increase attack speed.
					if (summon.isAttackingNow())
					{
						summon.abortAttack();
						summon.abortCast();
					}
					
					summon.doAttack(player.getTarget());
				}
			}
		}
		else
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_A_SERVITOR);
		}
	}
}