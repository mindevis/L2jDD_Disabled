
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class CriticalRate extends AbstractConditionalHpEffect
{
	public CriticalRate(StatSet params)
	{
		super(params, Stat.CRITICAL_RATE);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		if (_conditions.isEmpty() || _conditions.stream().allMatch(cond -> cond.test(effected, effected, skill)))
		{
			switch (_mode)
			{
				case DIFF:
				{
					effected.getStat().mergeAdd(_addStat, _amount);
					break;
				}
				case PER:
				{
					effected.getStat().mergeMul(_mulStat, (_amount / 100));
					break;
				}
			}
		}
	}
}
