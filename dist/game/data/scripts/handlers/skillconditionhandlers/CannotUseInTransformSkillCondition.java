
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Sdw
 */
public class CannotUseInTransformSkillCondition implements ISkillCondition
{
	private final int _transformId;
	
	public CannotUseInTransformSkillCondition(StatSet params)
	{
		_transformId = params.getInt("transformId", -1);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		return (_transformId > 0) ? caster.getTransformationId() != _transformId : !caster.isTransformed();
	}
}
