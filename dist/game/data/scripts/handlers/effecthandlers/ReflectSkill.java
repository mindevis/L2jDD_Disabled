
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.BasicProperty;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class ReflectSkill extends AbstractEffect
{
	private final Stat _stat;
	private final double _amount;
	
	public ReflectSkill(StatSet params)
	{
		_stat = params.getEnum("type", BasicProperty.class, BasicProperty.PHYSICAL) == BasicProperty.PHYSICAL ? Stat.REFLECT_SKILL_PHYSIC : Stat.REFLECT_SKILL_MAGIC;
		_amount = params.getDouble("amount", 0);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		effected.getStat().mergeAdd(_stat, _amount);
	}
}
