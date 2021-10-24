
package handlers.effecthandlers;

import java.util.List;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.conditions.Condition;
import org.l2jdd.gameserver.model.conditions.ConditionUsingItemType;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.type.ArmorType;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.BaseStat;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class StatBonusSkillCritical extends AbstractEffect
{
	private final BaseStat _stat;
	private final Condition _armorTypeCondition;
	
	public StatBonusSkillCritical(StatSet params)
	{
		_stat = params.getEnum("stat", BaseStat.class, BaseStat.DEX);
		
		int armorTypesMask = 0;
		final List<String> armorTypes = params.getList("armorType", String.class);
		if (armorTypes != null)
		{
			for (String armorType : armorTypes)
			{
				try
				{
					armorTypesMask |= ArmorType.valueOf(armorType).mask();
				}
				catch (IllegalArgumentException e)
				{
					final IllegalArgumentException exception = new IllegalArgumentException("armorTypes should contain ArmorType enum value but found " + armorType);
					exception.addSuppressed(e);
					throw exception;
				}
			}
		}
		_armorTypeCondition = armorTypesMask != 0 ? new ConditionUsingItemType(armorTypesMask) : null;
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		if ((_armorTypeCondition == null) || _armorTypeCondition.test(effected, effected, skill))
		{
			effected.getStat().mergeAdd(Stat.STAT_BONUS_SKILL_CRITICAL, _stat.ordinal());
		}
	}
}
