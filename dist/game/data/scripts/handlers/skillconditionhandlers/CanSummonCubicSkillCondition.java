
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
public class CanSummonCubicSkillCondition implements ISkillCondition
{
	public CanSummonCubicSkillCondition(StatSet params)
	{
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		if (!caster.isPlayer() || caster.isAlikeDead() || caster.getActingPlayer().inObserverMode())
		{
			return false;
		}
		
		final PlayerInstance player = caster.getActingPlayer();
		return !player.inObserverMode() && !player.isMounted() && !player.isSpawnProtected() && !player.isTeleportProtected();
	}
}
