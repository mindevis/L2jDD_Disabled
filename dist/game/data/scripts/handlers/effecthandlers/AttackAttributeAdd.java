
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
public class AttackAttributeAdd extends AbstractEffect
{
	private final double _amount;
	
	public AttackAttributeAdd(StatSet params)
	{
		_amount = params.getDouble("amount", 0);
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		Stat stat = Stat.FIRE_POWER;
		AttributeType maxAttribute = AttributeType.FIRE;
		int maxValue = 0;
		
		for (AttributeType attribute : AttributeType.values())
		{
			final int attributeValue = effected.getStat().getAttackElementValue(attribute);
			if ((attributeValue > 0) && (attributeValue > maxValue))
			{
				maxAttribute = attribute;
				maxValue = attributeValue;
			}
		}
		
		switch (maxAttribute)
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
