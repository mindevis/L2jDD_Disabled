
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public class OpPledgeSkillCondition implements ISkillCondition
{
	private final int _level;
	
	public OpPledgeSkillCondition(StatSet params)
	{
		_level = params.getInt("level");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		final Clan clan = caster.getClan();
		return (clan != null) && (clan.getLevel() >= _level);
	}
}
