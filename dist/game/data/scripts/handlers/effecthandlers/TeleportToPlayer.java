
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.instancezone.Instance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.olympiad.OlympiadManager;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 */
public class TeleportToPlayer extends AbstractEffect
{
	public TeleportToPlayer(StatSet params)
	{
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.TELEPORT_TO_TARGET;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if ((effector.getTarget() != null) && (effector.getTarget() != effector) && effector.getTarget().isPlayer())
		{
			final PlayerInstance target = (PlayerInstance) effector.getTarget();
			if (target.isAlikeDead())
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.C1_IS_DEAD_AT_THE_MOMENT_AND_CANNOT_BE_SUMMONED_OR_TELEPORTED);
				sm.addPcName(target);
				effector.sendPacket(sm);
				return;
			}
			
			if (target.isInStoreMode())
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.C1_IS_CURRENTLY_TRADING_OR_OPERATING_A_PRIVATE_STORE_AND_CANNOT_BE_SUMMONED_OR_TELEPORTED);
				sm.addPcName(target);
				effector.sendPacket(sm);
				return;
			}
			
			if (target.isRooted() || target.isInCombat())
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.C1_IS_ENGAGED_IN_COMBAT_AND_CANNOT_BE_SUMMONED_OR_TELEPORTED);
				sm.addPcName(target);
				effector.sendPacket(sm);
				return;
			}
			
			if (target.isInOlympiadMode())
			{
				effector.sendPacket(SystemMessageId.A_USER_PARTICIPATING_IN_THE_OLYMPIAD_CANNOT_USE_SUMMONING_OR_TELEPORTING);
				return;
			}
			
			if (target.isFlyingMounted() || target.isCombatFlagEquipped() || target.isInTraingCamp())
			{
				effector.sendPacket(SystemMessageId.YOU_CANNOT_USE_SUMMONING_OR_TELEPORTING_IN_THIS_AREA);
				return;
			}
			
			if (target.inObserverMode() || OlympiadManager.getInstance().isRegisteredInComp(target))
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.C1_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING_OR_TELEPORTING_2);
				sm.addString(target.getName());
				effector.sendPacket(sm);
				return;
			}
			
			if (target.isInsideZone(ZoneId.NO_SUMMON_FRIEND) || target.isInsideZone(ZoneId.JAIL))
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.C1_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING_OR_TELEPORTING);
				sm.addString(target.getName());
				effector.sendPacket(sm);
				return;
			}
			
			final Instance instance = target.getInstanceWorld();
			if (((instance != null) && !instance.isPlayerSummonAllowed()) || target.isInsideZone(ZoneId.TIMED_HUNTING))
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.C1_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING_OR_TELEPORTING);
				sm.addString(target.getName());
				effector.sendPacket(sm);
				return;
			}
			
			effector.teleToLocation(target, true, null);
		}
	}
}
