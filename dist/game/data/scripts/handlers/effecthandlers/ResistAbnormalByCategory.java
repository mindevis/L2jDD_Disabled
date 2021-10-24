
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
public class ResistAbnormalByCategory extends AbstractEffect
{
	private final DispelSlotType _slot;
	private final double _amount;
	
	public ResistAbnormalByCategory(StatSet params)
	{
		_amount = params.getDouble("amount", 0);
		_slot = params.getEnum("slot", DispelSlotType.class, DispelSlotType.DEBUFF);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		switch (_slot)
		{
			// Only this one is in use it seems
			case DEBUFF:
			{
				effected.getStat().mergeMul(Stat.RESIST_ABNORMAL_DEBUFF, 1 + (_amount / 100));
				break;
			}
		}
	}
}
