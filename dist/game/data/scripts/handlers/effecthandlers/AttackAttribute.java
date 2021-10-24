
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.AttributeType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class AttackAttribute extends AbstractEffect
{
	private final AttributeType _attribute;
	private final double _amount;
	
	public AttackAttribute(StatSet params)
	{
		_amount = params.getDouble("amount", 0);
		_attribute = params.getEnum("attribute", AttributeType.class, AttributeType.FIRE);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		Stat stat = Stat.FIRE_POWER;
		
		switch (_attribute)
		{
			case WATER:
			{
				stat = Stat.WATER_POWER;
				break;
			}
			case WIND:
			{
				stat = Stat.WIND_POWER;
				break;
			}
			case EARTH:
			{
				stat = Stat.EARTH_POWER;
				break;
			}
			case HOLY:
			{
				stat = Stat.HOLY_POWER;
				break;
			}
			case DARK:
			{
				stat = Stat.DARK_POWER;
				break;
			}
		}
		effected.getStat().mergeAdd(stat, _amount);
	}
}
