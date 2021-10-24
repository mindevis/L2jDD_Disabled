
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * @author UnAfraid
 */
public class ConsumeBodySkillCondition implements ISkillCondition
{
	public ConsumeBodySkillCondition(StatSet params)
	{
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		if ((target != null) && (target.isMonster() || target.isSummon()))
		{
			final Creature creature = (Creature) target;
			if (creature.isDead() && creature.isSpawned())
			{
				return true;
			}
		}
		
		if (caster.isPlayer())
		{
			caster.sendPacket(SystemMessageId.INVALID_TARGET);
		}
		return false;
	}
}
