
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class StatAddForMp extends AbstractEffect
{
	private final int _mp;
	private final Stat _stat;
	private final double _amount;
	
	public StatAddForMp(StatSet params)
	{
		_mp = params.getInt("mp", 0);
		_stat = params.getEnum("stat", Stat.class);
		_amount = params.getDouble("amount", 0);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		if (effected.getMaxMp() >= _mp)
		{
			effected.getStat().mergeAdd(_stat, _amount);
		}
	}
}
