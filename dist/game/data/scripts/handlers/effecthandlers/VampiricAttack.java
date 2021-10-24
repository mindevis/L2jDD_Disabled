
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class VampiricAttack extends AbstractEffect
{
	private final double _amount;
	private final double _sum;
	
	public VampiricAttack(StatSet params)
	{
		_amount = params.getDouble("amount");
		_sum = _amount * params.getDouble("chance");
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		effected.getStat().mergeAdd(Stat.ABSORB_DAMAGE_PERCENT, _amount / 100);
		effected.getStat().addToVampiricSum(_sum);
	}
}
