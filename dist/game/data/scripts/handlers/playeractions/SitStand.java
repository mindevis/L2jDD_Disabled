
package handlers.playeractions;

import org.l2jdd.gameserver.ai.CtrlEvent;
import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.ai.NextAction;
import org.l2jdd.gameserver.enums.MountType;
import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.actor.instance.StaticObjectInstance;
import org.l2jdd.gameserver.model.effects.EffectFlag;
import org.l2jdd.gameserver.network.serverpackets.ChairSit;

/**
 * Sit/Stand player action handler.
 * @author UnAfraid
 */
public class SitStand implements IPlayerActionHandler
{
	@Override
	public void useAction(PlayerInstance player, ActionDataHolder data, boolean ctrlPressed, boolean shiftPressed)
	{
		if (player.isSitting() || !player.isMoving() || player.isFakeDeath())
		{
			useSit(player, player.getTarget());
		}
		else
		{
			// Sit when arrive using next action.
			// Creating next action class.
			final NextAction nextAction = new NextAction(CtrlEvent.EVT_ARRIVED, CtrlIntention.AI_INTENTION_MOVE_TO, () -> useSit(player, player.getTarget()));
			
			// Binding next action to AI.
			player.getAI().setNextAction(nextAction);
		}
	}
	
	/**
	 * Use the sit action.
	 * @param player the player trying to sit
	 * @param target the target to sit, throne, bench or chair
	 * @return {@code true} if the player can sit, {@code false} otherwise
	 */
	private boolean useSit(PlayerInstance player, WorldObject target)
	{
		if (player.getMountType() != MountType.NONE)
		{
			return false;
		}
		
		if (!player.isSitting() && (target instanceof StaticObjectInstance) && (((StaticObjectInstance) target).getType() == 1) && player.isInsideRadius2D(target, StaticObjectInstance.INTERACTION_DISTANCE))
		{
			final ChairSit cs = new ChairSit(player, target.getId());
			player.sendPacket(cs);
			player.sitDown();
			player.broadcastPacket(cs);
			return true;
		}
		
		if (player.isFakeDeath())
		{
			player.stopEffects(EffectFlag.FAKE_DEATH);
		}
		else if (player.isSitting())
		{
			player.standUp();
		}
		else
		{
			player.sitDown();
		}
		return true;
	}
}
