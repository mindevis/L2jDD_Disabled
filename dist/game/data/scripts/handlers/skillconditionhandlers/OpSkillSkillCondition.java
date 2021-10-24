
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class OpSkillSkillCondition implements ISkillCondition
{
	private final int _skillId;
	private final int _skillLevel;
	private final boolean _hasLearned;
	
	public OpSkillSkillCondition(StatSet params)
	{
		_skillId = params.getInt("skillId");
		_skillLevel = params.getInt("skillLevel");
		_hasLearned = params.getBoolean("hasLearned");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		final Skill requestedSkill = caster.getKnownSkill(_skillId);
		if (_hasLearned)
		{
			return (requestedSkill != null) && (requestedSkill.getLevel() == _skillLevel);
		}
		return (requestedSkill == null) || (requestedSkill.getLevel() != _skillLevel);
	}
}
