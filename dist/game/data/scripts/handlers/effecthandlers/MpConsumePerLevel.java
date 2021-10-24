
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * Mp Consume Per Level effect implementation.
 */
public class MpConsumePerLevel extends AbstractEffect
{
	private final double _power;
	
	public MpConsumePerLevel(StatSet params)
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
		
		final double base = _power * getTicksMultiplier();
		final double consume = (skill.getAbnormalTime() > 0) ? ((effected.getLevel() - 1) / 7.5) * base * skill.getAbnormalTime() : base;
		if (consume > effected.getCurrentMp())
		{
			effected.sendPacket(SystemMessageId.YOUR_SKILL_WAS_DEACTIVATED_DUE_TO_LACK_OF_MP);
			return false;
		}
		
		effected.reduceCurrentMp(consume);
		return skill.isToggle();
	}
}
