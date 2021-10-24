
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.AbnormalType;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;
import org.l2jdd.gameserver.network.serverpackets.ExRegenMax;

/**
 * Heal Over Time effect implementation.
 */
public class HealOverTime extends AbstractEffect
{
	private final double _power;
	
	public HealOverTime(StatSet params)
	{
		_power = params.getDouble("power", 0);
		setTicks(params.getInt("ticks"));
	}
	
	@Override
	public boolean onActionTime(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effected.isDead() || effected.isDoor())
		{
			return false;
		}
		
		double hp = effected.getCurrentHp();
		final double maxhp = effected.getMaxRecoverableHp();
		
		// Not needed to set the HP and send update packet if player is already at max HP
		if (_power > 0)
		{
			if (hp >= maxhp)
			{
				return false;
			}
		}
		else
		{
			if ((hp - _power) <= 0)
			{
				return false;
			}
		}
		
		double power = _power;
		if ((item != null) && (item.isPotion() || item.isElixir()))
		{
			power += effected.getStat().getValue(Stat.ADDITIONAL_POTION_HP, 0) / getTicks();
		}
		
		hp += power * getTicksMultiplier();
		if (_power > 0)
		{
			hp = Math.min(hp, maxhp);
		}
		else
		{
			hp = Math.max(hp, 1);
		}
		effected.setCurrentHp(hp, false);
		effected.broadcastStatusUpdate(effector);
		return skill.isToggle();
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effected.isPlayer() && (getTicks() > 0) && (skill.getAbnormalType() == AbnormalType.HP_RECOVER))
		{
			double power = _power;
			if ((item != null) && (item.isPotion() || item.isElixir()))
			{
				final double bonus = effected.getStat().getValue(Stat.ADDITIONAL_POTION_HP, 0);
				if (bonus > 0)
				{
					power += bonus / getTicks();
				}
			}
			
			effected.sendPacket(new ExRegenMax(skill.getAbnormalTime(), getTicks(), power));
		}
	}
}
