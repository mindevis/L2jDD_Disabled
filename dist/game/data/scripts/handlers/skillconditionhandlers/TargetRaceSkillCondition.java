
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.enums.Race;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class TargetRaceSkillCondition implements ISkillCondition
{
	private final Race _race;
	
	public TargetRaceSkillCondition(StatSet params)
	{
		_race = params.getEnum("race", Race.class);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		if ((target == null) || !target.isCreature())
		{
			return false;
		}
		final Creature targetCreature = (Creature) target;
		return targetCreature.getRace() == _race;
	}
}
