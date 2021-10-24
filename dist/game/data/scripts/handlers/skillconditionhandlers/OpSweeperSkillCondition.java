
package handlers.skillconditionhandlers;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * @author Zoey76
 */
public class OpSweeperSkillCondition implements ISkillCondition
{
	public OpSweeperSkillCondition(StatSet params)
	{
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		boolean canSweep = false;
		if (caster.getActingPlayer() != null)
		{
			final PlayerInstance sweeper = caster.getActingPlayer();
			if (skill != null)
			{
				for (WorldObject wo : skill.getTargetsAffected(sweeper, target))
				{
					if ((wo != null) && wo.isAttackable())
					{
						final Attackable attackable = (Attackable) wo;
						if (attackable.isDead())
						{
							if (attackable.isSpoiled())
							{
								canSweep = attackable.checkSpoilOwner(sweeper, true);
								if (canSweep)
								{
									canSweep = !attackable.isOldCorpse(sweeper, Config.CORPSE_CONSUME_SKILL_ALLOWED_TIME_BEFORE_DECAY, true);
								}
								if (canSweep)
								{
									canSweep = sweeper.getInventory().checkInventorySlotsAndWeight(attackable.getSpoilLootItems(), true, true);
								}
							}
							else
							{
								sweeper.sendPacket(SystemMessageId.SWEEPER_FAILED_TARGET_NOT_SPOILED);
							}
						}
					}
				}
			}
		}
		return canSweep;
	}
}
