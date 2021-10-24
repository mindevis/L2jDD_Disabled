
package handlers.skillconditionhandlers;

import java.util.List;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Sdw
 */
public class OpTargetNpcSkillCondition implements ISkillCondition
{
	private final List<Integer> _npcId;
	
	public OpTargetNpcSkillCondition(StatSet params)
	{
		_npcId = params.getList("npcIds", Integer.class);
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		final WorldObject actualTarget = (caster == null) || !caster.isPlayer() ? target : caster.getTarget();
		return (actualTarget != null) && (actualTarget.isNpc() || actualTarget.isDoor()) && _npcId.contains(actualTarget.getId());
	}
}
