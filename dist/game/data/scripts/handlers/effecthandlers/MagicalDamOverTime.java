
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Formulas;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * MagicalAttack-damage over time effect implementation.
 * @author Nik
 */
public class MagicalDamOverTime extends AbstractEffect
{
	private final double _power;
	private final boolean _canKill;
	
	public MagicalDamOverTime(StatSet params)
	{
		_power = params.getDouble("power", 0);
		_canKill = params.getBoolean("canKill", false);
		setTicks(params.getInt("ticks"));
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.MAGICAL_DMG_OVER_TIME;
	}
	
	@Override
	public boolean onActionTime(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final Creature creature = effector;
		final Creature target = effected;
		
		if (target.isDead())
		{
			return false;
		}
		
		double damage = Formulas.calcMagicDam(creature, target, skill, creature.getMAtk(), _power, target.getMDef(), false, false, false); // In retail spiritshots change nothing.
		damage *= getTicksMultiplier();
		
		if (damage >= (target.getCurrentHp() - 1))
		{
			if (skill.isToggle())
			{
				target.sendPacket(SystemMessageId.YOUR_SKILL_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_HP);
				return false;
			}
			
			// For DOT skills that will not kill effected player.
			if (!_canKill)
			{
				// Fix for players dying by DOTs if HP < 1 since reduceCurrentHP method will kill them
				if (target.getCurrentHp() <= 1)
				{
					return skill.isToggle();
				}
				damage = target.getCurrentHp() - 1;
			}
		}
		
		effector.doAttack(damage, effected, skill, true, false, false, false);
		return skill.isToggle();
	}
}
