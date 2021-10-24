
package handlers.actionhandlers;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.handler.IActionHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.model.actor.instance.PetInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.EventDispatcher;
import org.l2jdd.gameserver.model.events.impl.creature.player.OnPlayerSummonTalk;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.PetStatusShow;

public class PetInstanceAction implements IActionHandler
{
	@Override
	public boolean action(PlayerInstance player, WorldObject target, boolean interact)
	{
		// Aggression target lock effect
		if (player.isLockedTarget() && (player.getLockedTarget() != target))
		{
			player.sendPacket(SystemMessageId.FAILED_TO_CHANGE_ENMITY);
			return false;
		}
		
		final boolean isOwner = player.getObjectId() == ((PetInstance) target).getOwner().getObjectId();
		if (isOwner && (player != ((PetInstance) target).getOwner()))
		{
			((PetInstance) target).updateRefOwner(player);
		}
		if (player.getTarget() != target)
		{
			// Set the target of the PlayerInstance player
			player.setTarget(target);
		}
		else if (interact)
		{
			// Check if the pet is attackable (without a forced attack) and isn't dead
			if (target.isAutoAttackable(player) && !isOwner)
			{
				if (GeoEngine.getInstance().canSeeTarget(player, target))
				{
					// Set the PlayerInstance Intention to AI_INTENTION_ATTACK
					player.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
					player.onActionRequest();
				}
			}
			else if (!((Creature) target).isInsideRadius2D(player, 150))
			{
				if (GeoEngine.getInstance().canSeeTarget(player, target))
				{
					player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, target);
					player.onActionRequest();
				}
			}
			else
			{
				if (isOwner)
				{
					player.sendPacket(new PetStatusShow((PetInstance) target));
					
					// Notify to scripts
					EventDispatcher.getInstance().notifyEventAsync(new OnPlayerSummonTalk((Summon) target), (Summon) target);
				}
				player.updateNotMoveUntil();
			}
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.PetInstance;
	}
}
