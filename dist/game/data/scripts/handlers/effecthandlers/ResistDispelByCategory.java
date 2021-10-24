
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.DispelSlotType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class ResistDispelByCategory extends AbstractEffect
{
	private final DispelSlotType _slot;
	private final double _amount;
	
	public ResistDispelByCategory(StatSet params)
	{
		_amount = params.getDouble("amount", 0);
		_slot = params.getEnum("slot", DispelSlotType.class, DispelSlotType.BUFF);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		switch (_slot)
		{
			// Only this one is in use it seems
			case BUFF:
			{
				effected.getStat().mergeMul(Stat.RESIST_DISPEL_BUFF, 1 + (_amount / 100));
				break;
			}
		}
	}
}
