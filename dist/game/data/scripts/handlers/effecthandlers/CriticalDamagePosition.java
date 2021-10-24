
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.Position;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;
import org.l2jdd.gameserver.util.MathUtil;

/**
 * @author Sdw
 */
public class CriticalDamagePosition extends AbstractEffect
{
	private final double _amount;
	private final Position _position;
	
	public CriticalDamagePosition(StatSet params)
	{
		_amount = params.getDouble("amount", 0);
		_position = params.getEnum("position", Position.class, Position.FRONT);
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		effected.getStat().mergePositionTypeValue(Stat.CRITICAL_DAMAGE, _position, (_amount / 100) + 1, MathUtil::mul);
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		effected.getStat().mergePositionTypeValue(Stat.CRITICAL_DAMAGE, _position, (_amount / 100) + 1, MathUtil::div);
	}
}
