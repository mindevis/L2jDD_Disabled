
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.skills.AbnormalType;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * The Class ConditionTargetAbnormal.
 * @author janiii
 */
public class ConditionTargetAbnormalType extends Condition
{
	private final AbnormalType _abnormalType;
	
	/**
	 * Instantiates a new condition target abnormal type.
	 * @param abnormalType the abnormal type
	 */
	public ConditionTargetAbnormalType(AbnormalType abnormalType)
	{
		_abnormalType = abnormalType;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		return effected.hasAbnormalType(_abnormalType);
	}
}
