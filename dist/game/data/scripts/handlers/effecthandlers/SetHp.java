
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.StatModifierType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * An effect that sets the current hp to the given amount.
 * @author Nik
 */
public class SetHp extends AbstractEffect
{
	private final double _amount;
	private final StatModifierType _mode;
	
	public SetHp(StatSet params)
	{
		_amount = params.getDouble("amount", 0);
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
		if (effected.isDead() || effected.isDoor())
		{
			return;
		}
		
		final boolean full = (_mode == StatModifierType.PER) && (_amount == 100.0);
		final double amount = full ? effected.getMaxHp() : (_mode == StatModifierType.PER) ? ((effected.getMaxHp() * _amount) / 100.0) : _amount;
		effected.setCurrentHp(amount);
	}
}
