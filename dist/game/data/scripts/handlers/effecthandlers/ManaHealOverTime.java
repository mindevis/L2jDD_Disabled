
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * Mana Heal Over Time effect implementation.
 */
public class ManaHealOverTime extends AbstractEffect
{
	private final double _power;
	
	public ManaHealOverTime(StatSet params)
	{
		_power = params.getDouble("power", 0);
		setTicks(params.getInt("ticks"));
	}
	
	@Override
	public boolean onActionTime(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effected.isDead())
		{
			return false;
		}
		
		double mp = effected.getCurrentMp();
		final double maxmp = effected.getMaxRecoverableMp();
		
		// Not needed to set the MP and send update packet if player is already at max MP
		if (_power > 0)
		{
			if (mp >= maxmp)
			{
				return true;
			}
		}
		else
		{
			if ((mp - _power) <= 0)
			{
				return true;
			}
		}
		
		double power = _power;
		if ((item != null) && (item.isPotion() || item.isElixir()))
		{
			power += effected.getStat().getValue(Stat.ADDITIONAL_POTION_MP, 0) / getTicks();
		}
		
		mp += power * getTicksMultiplier();
		if (_power > 0)
		{
			mp = Math.min(mp, maxmp);
		}
		else
		{
			mp = Math.max(mp, 1);
		}
		effected.setCurrentMp(mp, false);
		effected.broadcastStatusUpdate(effector);
		return skill.isToggle();
	}
}
