
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Add Hate effect implementation.
 * @author Adry_85
 */
public class AddHate extends AbstractEffect
{
	private final double _power;
	private final boolean _affectSummoner;
	
	public AddHate(StatSet params)
	{
		_power = params.getDouble("power", 0);
		_affectSummoner = params.getBoolean("affectSummoner", false);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature creature, Creature effected, Skill skill, ItemInstance item)
	{
		Creature effector = creature;
		if (_affectSummoner && (effector.getSummoner() != null))
		{
			effector = effector.getSummoner();
		}
		
		if (!effected.isAttackable())
		{
			return;
		}
		
		final double val = _power;
		if (val > 0)
		{
			((Attackable) effected).addDamageHate(effector, 0, (int) val);
			effected.setRunning();
		}
		else if (val < 0)
		{
			((Attackable) effected).reduceHate(effector, (int) -val);
		}
	}
}
