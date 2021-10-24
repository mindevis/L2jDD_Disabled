
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
public class OpSocialClassSkillCondition implements ISkillCondition
{
	private final int _socialClass;
	
	public OpSocialClassSkillCondition(StatSet params)
	{
		_socialClass = params.getInt("socialClass");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		final PlayerInstance player = caster.getActingPlayer();
		if ((player == null) || (player.getClan() == null))
		{
			return false;
		}
		
		final boolean isClanLeader = player.isClanLeader();
		if ((_socialClass == -1) && !isClanLeader)
		{
			return false;
		}
		
		return isClanLeader || (player.getPledgeType() >= _socialClass);
	}
}
