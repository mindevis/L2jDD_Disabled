
package handlers.playeractions;

import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;

/**
 * Tactical Signs setting player action handler.
 * @author Nik
 */
public class TacticalSignUse implements IPlayerActionHandler
{
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		if ((!player.isInParty() || (player.getTarget() == null) || !player.getTarget().isCreature()))
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		player.getParty().addTacticalSign(player, data.getOptionId(), (Creature) player.getTarget());
	}
}
