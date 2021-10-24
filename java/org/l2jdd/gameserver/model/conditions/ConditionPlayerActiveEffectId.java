
package org.l2jdd.gameserver.model.conditions;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.skills.BuffInfo;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * The Class ConditionPlayerActiveEffectId.
 */
public class ConditionPlayerActiveEffectId extends Condition
{
	private final int _effectId;
	private final int _effectLvl;
	
	/**
	 * Instantiates a new condition player active effect id.
	 * @param effectId the effect id
	 */
	public ConditionPlayerActiveEffectId(int effectId)
	{
		_effectId = effectId;
		_effectLvl = -1;
	}
	
	/**
	 * Instantiates a new condition player active effect id.
	 * @param effectId the effect id
	 * @param effectLevel the effect level
	 */
	public ConditionPlayerActiveEffectId(int effectId, int effectLevel)
	{
		_effectId = effectId;
		_effectLvl = effectLevel;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		final BuffInfo info = effector.getEffectList().getBuffInfoBySkillId(_effectId);
		return ((info != null) && ((_effectLvl == -1) || (_effectLvl <= info.getSkill().getLevel())));
	}
}
