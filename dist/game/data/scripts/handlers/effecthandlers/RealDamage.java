
package handlers.effecthandlers;

import org.l2jdd.Config;
import org.l2jdd.gameserver.enums.StatModifierType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw, Mobius
 */
public class RealDamage extends AbstractEffect
{
	private final double _power;
	private final StatModifierType _mode;
	
	public RealDamage(StatSet params)
	{
		_power = params.getDouble("power", 0);
		_mode = params.getEnum("mode", StatModifierType.class, StatModifierType.DIFF);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effected.isDead() || effected.isDoor() || effected.isRaid())
		{
			return;
		}
		
		// Check if effected NPC is not attackable.
		if (effected.isNpc() && !effected.isAttackable())
		{
			return;
		}
		
		// Check if fake players should aggro each other.
		if (effector.isFakePlayer() && !Config.FAKE_PLAYER_AGGRO_FPC && effected.isFakePlayer())
		{
			return;
		}
		
		// Calculate resistance.
		final double damage;
		if (_mode == StatModifierType.DIFF)
		{
			damage = _power - (_power * (Math.min(effected.getStat().getMul(Stat.REAL_DAMAGE_RESIST, 1), 1.8) - 1));
		}
		else // PER
		{
			// Percent does not ignore HP block.
			if (effected.isHpBlocked())
			{
				return;
			}
			
			damage = (effected.getCurrentHp() * _power) / 100;
		}
		
		// Do damage.
		if (damage > 0)
		{
			effected.setCurrentHp(Math.max(effected.getCurrentHp() - damage, effected.isUndying() ? 1 : 0));
			
			// Die.
			if (effected.getCurrentHp() < 0.5)
			{
				effected.doDie(effector);
			}
		}
		
		// Send message.
		if (effector.isPlayer())
		{
			effector.sendDamageMessage(effected, skill, (int) damage, false, false);
		}
	}
}
