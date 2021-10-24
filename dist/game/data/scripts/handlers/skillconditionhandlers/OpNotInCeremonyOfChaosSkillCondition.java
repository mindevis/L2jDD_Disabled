
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.ceremonyofchaos.CeremonyOfChaosEvent;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Mobius
 */
public class OpNotInCeremonyOfChaosSkillCondition implements ISkillCondition
{
	public OpNotInCeremonyOfChaosSkillCondition(StatSet params)
	{
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		return (caster.isPlayer() && !caster.getActingPlayer().isOnEvent(CeremonyOfChaosEvent.class));
	}
}
