
package handlers.playeractions;

import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.instance.PetInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * Pet attack player action handler.
 * @author Nik
 */
public class PetAttack implements IPlayerActionHandler
{
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		if ((player.getPet() == null) || !player.getPet().isPet())
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_A_PET);
			return;
		}
		
		final PetInstance pet = player.getPet();
		if (pet.isUncontrollable())
		{
			player.sendPacket(SystemMessageId.WHEN_YOUR_PET_S_HUNGER_GAUGE_IS_AT_0_YOU_CANNOT_USE_YOUR_PET);
		}
		else if (pet.isBetrayed())
		{
			player.sendPacket(SystemMessageId.YOUR_PET_SERVITOR_IS_UNRESPONSIVE_AND_WILL_NOT_OBEY_ANY_ORDERS);
		}
		else if (pet.canAttack(player.getTarget(), ctrlPressed))
		{
			// Prevent spamming next target and attack to increase attack speed.
			if (pet.isAttackingNow())
			{
				pet.abortAttack();
				pet.abortCast();
			}
			
			pet.doAttack(player.getTarget());
		}
	}
}
