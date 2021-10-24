
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.ReduceDropType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class ReduceDropPenalty extends AbstractEffect
{
	private final double _exp;
	private final double _deathPenalty;
	private final ReduceDropType _type;
	
	public ReduceDropPenalty(StatSet params)
	{
		_exp = params.getDouble("exp", 0);
		_deathPenalty = params.getDouble("deathPenalty", 0);
		_type = params.getEnum("type", ReduceDropType.class, ReduceDropType.MOB);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		switch (_type)
		{
			case MOB:
			{
				effected.getStat().mergeMul(Stat.REDUCE_EXP_LOST_BY_MOB, (_exp / 100) + 1);
				effected.getStat().mergeMul(Stat.REDUCE_DEATH_PENALTY_BY_MOB, (_deathPenalty / 100) + 1);
				break;
			}
			case PK:
			{
				effected.getStat().mergeMul(Stat.REDUCE_EXP_LOST_BY_PVP, (_exp / 100) + 1);
				effected.getStat().mergeMul(Stat.REDUCE_DEATH_PENALTY_BY_PVP, (_deathPenalty / 100) + 1);
				break;
			}
			case RAID:
			{
				effected.getStat().mergeMul(Stat.REDUCE_EXP_LOST_BY_RAID, (_exp / 100) + 1);
				effected.getStat().mergeMul(Stat.REDUCE_DEATH_PENALTY_BY_RAID, (_deathPenalty / 100) + 1);
				break;
			}
			case ANY:
			{
				effected.getStat().mergeMul(Stat.REDUCE_EXP_LOST_BY_MOB, (_exp / 100) + 1);
				effected.getStat().mergeMul(Stat.REDUCE_DEATH_PENALTY_BY_MOB, (_deathPenalty / 100) + 1);
				effected.getStat().mergeMul(Stat.REDUCE_EXP_LOST_BY_PVP, (_exp / 100) + 1);
				effected.getStat().mergeMul(Stat.REDUCE_DEATH_PENALTY_BY_PVP, (_deathPenalty / 100) + 1);
				effected.getStat().mergeMul(Stat.REDUCE_EXP_LOST_BY_RAID, (_exp / 100) + 1);
				effected.getStat().mergeMul(Stat.REDUCE_DEATH_PENALTY_BY_RAID, (_deathPenalty / 100) + 1);
				break;
			}
		}
	}
}
