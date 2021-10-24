
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.enums.PrivateStoreType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Sdw
 */
public class OpCannotUseTargetWithPrivateStoreSkillCondition implements ISkillCondition
{
	public OpCannotUseTargetWithPrivateStoreSkillCondition(StatSet params)
	{
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		return (target == null) || !target.isPlayer() || (target.getActingPlayer().getPrivateStoreType() == PrivateStoreType.NONE);
	}
}
