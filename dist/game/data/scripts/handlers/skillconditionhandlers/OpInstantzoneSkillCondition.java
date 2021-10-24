
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.instancezone.Instance;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class OpInstantzoneSkillCondition implements ISkillCondition
{
	private final int _instanceId;
	
	public OpInstantzoneSkillCondition(StatSet params)
	{
		_instanceId = params.getInt("instanceId");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		final Instance instance = caster.getInstanceWorld();
		return (instance != null) && (instance.getTemplateId() == _instanceId);
	}
}
