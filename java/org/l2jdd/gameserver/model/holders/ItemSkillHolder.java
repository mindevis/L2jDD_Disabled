
package org.l2jdd.gameserver.model.holders;

import org.l2jdd.gameserver.enums.ItemSkillType;

/**
 * @author UnAfraid
 */
public class ItemSkillHolder extends SkillHolder
{
	private final ItemSkillType _type;
	private final int _chance;
	private final int _value;
	
	public ItemSkillHolder(int skillId, int skillLevel, ItemSkillType type, int chance, int value)
	{
		super(skillId, skillLevel);
		_type = type;
		_chance = chance;
		_value = value;
	}
	
	public ItemSkillType getType()
	{
		return _type;
	}
	
	public int getChance()
	{
		return _chance;
	}
	
	public int getValue()
	{
		return _value;
	}
}
