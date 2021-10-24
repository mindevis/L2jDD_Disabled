
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.TrapInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Trap Detect effect implementation.
 * @author UnAfraid
 */
public class TrapDetect extends AbstractEffect
{
	private final int _power;
	
	public TrapDetect(StatSet params)
	{
		if (params.isEmpty())
		{
			throw new IllegalArgumentException(getClass().getSimpleName() + ": effect without power!");
		}
		
		_power = params.getInt("power");
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (!effected.isTrap() || effected.isAlikeDead())
		{
			return;
		}
		
		final TrapInstance trap = (TrapInstance) effected;
		if (trap.getLevel() <= _power)
		{
			trap.setDetected(effector);
		}
	}
}
