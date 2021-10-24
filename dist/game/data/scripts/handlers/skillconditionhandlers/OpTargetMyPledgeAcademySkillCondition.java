
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class OpTargetMyPledgeAcademySkillCondition implements ISkillCondition
{
	public OpTargetMyPledgeAcademySkillCondition(StatSet params)
	{
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		if ((caster.getClan() == null) || (target == null) || !target.isPlayer())
		{
			return false;
		}
		final PlayerInstance targetPlayer = target.getActingPlayer();
		return targetPlayer.isAcademyMember() && (targetPlayer.getClan() == caster.getClan());
	}
}
