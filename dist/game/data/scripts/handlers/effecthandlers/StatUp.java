
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.BaseStat;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class StatUp extends AbstractEffect
{
	private final BaseStat _stat;
	private final double _amount;
	
	public StatUp(StatSet params)
	{
		_amount = params.getDouble("amount", 0);
		_stat = params.getEnum("stat", BaseStat.class, BaseStat.STR);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		Stat stat = Stat.STAT_STR;
		
		switch (_stat)
		{
			case INT:
			{
				stat = Stat.STAT_INT;
				break;
			}
			case DEX:
			{
				stat = Stat.STAT_DEX;
				break;
			}
			case WIT:
			{
				stat = Stat.STAT_WIT;
				break;
			}
			case CON:
			{
				stat = Stat.STAT_CON;
				break;
			}
			case MEN:
			{
				stat = Stat.STAT_MEN;
				break;
			}
			case CHA:
			{
				stat = Stat.STAT_CHA;
				break;
			}
			case LUC:
			{
				stat = Stat.STAT_LUC;
				break;
			}
		}
		effected.getStat().mergeAdd(stat, _amount);
	}
}
