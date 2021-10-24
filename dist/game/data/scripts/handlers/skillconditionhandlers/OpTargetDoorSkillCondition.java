
package handlers.skillconditionhandlers;

import java.util.List;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Mobius
 */
public class OpTargetDoorSkillCondition implements ISkillCondition
{
	private final List<Integer> _doorIds;
	
	public OpTargetDoorSkillCondition(StatSet params)
	{
		_doorIds = params.getList("doorIds", Integer.class);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		return (target != null) && target.isDoor() && _doorIds.contains(target.getId());
	}
}
