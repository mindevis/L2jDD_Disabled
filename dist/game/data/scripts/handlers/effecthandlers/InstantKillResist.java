
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class InstantKillResist extends AbstractEffect
{
	private final int _amount;
	
	public InstantKillResist(StatSet params)
	{
		_amount = params.getInt("amount", 0);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		effected.getStat().mergeAdd(Stat.INSTANT_KILL_RESIST, _amount);
	}
}
