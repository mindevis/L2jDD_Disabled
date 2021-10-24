
package handlers.effecthandlers;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.gameserver.enums.StatModifierType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.conditions.Condition;
import org.l2jdd.gameserver.model.conditions.ConditionPlayerIsInCombat;
import org.l2jdd.gameserver.model.conditions.ConditionUsingItemType;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.type.ArmorType;
import org.l2jdd.gameserver.model.items.type.WeaponType;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public abstract class AbstractStatEffect extends AbstractEffect
{
	protected final Stat _addStat;
	protected final Stat _mulStat;
	protected final double _amount;
	protected final StatModifierType _mode;
	protected final List<Condition> _conditions = new ArrayList<>();
	
	public AbstractStatEffect(StatSet params, Stat stat)
	{
		this(params, stat, stat);
	}
	
	public AbstractStatEffect(StatSet params, Stat mulStat, Stat addStat)
	{
		_addStat = addStat;
		_mulStat = mulStat;
		_amount = params.getDouble("amount", 0);
		_mode = params.getEnum("mode", StatModifierType.class, StatModifierType.DIFF);
		
		int weaponTypesMask = 0;
		final List<String> weaponTypes = params.getList("weaponType", String.class);
		if (weaponTypes != null)
		{
			for (String weaponType : weaponTypes)
			{
				try
				{
					weaponTypesMask |= WeaponType.valueOf(weaponType).mask();
				}
				catch (IllegalArgumentException e)
				{
					final IllegalArgumentException exception = new IllegalArgumentException("weaponType should contain WeaponType enum value but found " + weaponType);
					exception.addSuppressed(e);
					throw exception;
				}
			}
		}
		
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
		
		if (weaponTypesMask != 0)
		{
			_conditions.add(new ConditionUsingItemType(weaponTypesMask));
		}
		
		if (armorTypesMask != 0)
		{
			_conditions.add(new ConditionUsingItemType(armorTypesMask));
		}
		
		if (params.contains("inCombat"))
		{
			_conditions.add(new ConditionPlayerIsInCombat(params.getBoolean("inCombat")));
		}
	}
	
	@Override
	public void pump(Creature effected, Skill skill)
	{
		if (_conditions.isEmpty() || _conditions.stream().allMatch(cond -> cond.test(effected, effected, skill)))
		{
			switch (_mode)
			{
				case DIFF:
				{
					effected.getStat().mergeAdd(_addStat, _amount);
					break;
				}
				case PER:
				{
					effected.getStat().mergeMul(_mulStat, (_amount / 100) + 1);
					break;
				}
			}
		}
	}
}
