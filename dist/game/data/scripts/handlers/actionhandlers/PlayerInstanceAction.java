
package handlers.actionhandlers;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.enums.PrivateStoreType;
import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.handler.IActionHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;

public class PlayerInstanceAction implements IActionHandler
{
	private static final int CURSED_WEAPON_VICTIM_MIN_LEVEL = 21;
	
	/**
	 * Manage actions when a player click on this PlayerInstance.<br>
	 * <br>
	 * <b><u>Actions on first click on the PlayerInstance (Select it)</u>:</b><br>
	 * <li>Set the target of the player</li>
	 * <li>Send a Server->Client packet MyTargetSelected to the player (display the select window)</li><br>
	 * <br>
	 * <b><u>Actions on second click on the PlayerInstance (Follow it/Attack it/Intercat with it)</u>:</b><br>
	 * <li>Send a Server->Client packet MyTargetSelected to the player (display the select window)</li>
	 * <li>If target PlayerInstance has a Private Store, notify the player AI with AI_INTENTION_INTERACT</li>
	 * <li>If target PlayerInstance is autoAttackable, notify the player AI with AI_INTENTION_ATTACK</li>
	 * <li>If target PlayerInstance is NOT autoAttackable, notify the player AI with AI_INTENTION_FOLLOW</li><br>
	 * <br>
	 * <b><u>Example of use</u>:</b><br>
	 * <li>Client packet : Action, AttackRequest</li><br>
	 * @param player The player that start an action on target PlayerInstance
	 */
	@Override
	public boolean action(PlayerInstance player, WorldObject target, boolean interact)
	{
		// Check if the PlayerInstance is confused
		if (player.isControlBlocked())
		{
			return false;
		}
		
		// Aggression target lock effect
		if (player.isLockedTarget() && (player.getLockedTarget() != target))
		{
			player.sendPacket(SystemMessageId.FAILED_TO_CHANGE_ENMITY);
			return false;
		}
		
		// Check if the player already target this PlayerInstance
		if (player.getTarget() != target)
		{
			// Set the target of the player
			player.setTarget(target);
		}
		else if (interact)
		{
			// Check if this PlayerInstance has a Private Store
			final PlayerInstance targetPlayer = target.getActingPlayer();
			if (targetPlayer.getPrivateStoreType() != PrivateStoreType.NONE)
			{
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, target);
			}
			else
			{
				// Check if this PlayerInstance is autoAttackable
				if (target.isAutoAttackable(player))
				{
					// Player with level < 21 can't attack a cursed weapon holder
					// And a cursed weapon holder can't attack players with level < 21
					if ((targetPlayer.isCursedWeaponEquipped() && (player.getLevel() < CURSED_WEAPON_VICTIM_MIN_LEVEL)) //
						|| (player.isCursedWeaponEquipped() && (targetPlayer.getLevel() < CURSED_WEAPON_VICTIM_MIN_LEVEL)))
					{
						player.sendPacket(ActionFailed.STATIC_PACKET);
					}
					else
					{
						if (GeoEngine.getInstance().canSeeTarget(player, target))
						{
							player.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
							player.onActionRequest();
						}
					}
				}
				else
				{
					// This Action Failed packet avoids player getting stuck when clicking three or more times
					player.sendPacket(ActionFailed.STATIC_PACKET);
					if (GeoEngine.getInstance().canSeeTarget(player, target))
					{
						player.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, target);
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.PlayerInstance;
	}
}
