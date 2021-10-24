
package org.l2jdd.gameserver.model.holders;

import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Simple class for storing skill id/level.
 * @author BiggBoss
 */
public class SkillHolder
{
	private final int _skillId;
	private final int _skillLevel;
	private final int _skillSubLevel;
	private Skill _skill;
	
	public SkillHolder(int skillId, int skillLevel)
	{
		_skillId = skillId;
		_skillLevel = skillLevel;
		_skillSubLevel = 0;
		_skill = null;
	}
	
	public SkillHolder(int skillId, int skillLevel, int skillSubLevel)
	{
		_skillId = skillId;
		_skillLevel = skillLevel;
		_skillSubLevel = skillSubLevel;
		_skill = null;
	}
	
	public SkillHolder(Skill skill)
	{
		_skillId = skill.getId();
		_skillLevel = skill.getLevel();
		_skillSubLevel = skill.getSubLevel();
		_skill = skill;
	}
	
	public int getSkillId()
	{
		return _skillId;
	}
	
	public int getSkillLevel()
	{
		return _skillLevel;
	}
	
	public int getSkillSubLevel()
	{
		return _skillSubLevel;
	}
	
	public Skill getSkill()
	{
		if (_skill == null)
		{
			_skill = SkillData.getInstance().getSkill(_skillId, Math.max(_skillLevel, 1), _skillSubLevel);
		}
		return _skill;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		
		if (!(obj instanceof SkillHolder))
		{
			return false;
		}
		
		final SkillHolder holder = (SkillHolder) obj;
		return (holder.getSkillId() == _skillId) && (holder.getSkillLevel() == _skillLevel) && (holder.getSkillSubLevel() == _skillSubLevel);
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + _skillId;
		result = (prime * result) + _skillLevel;
		result = (prime * result) + _skillSubLevel;
		return result;
	}
	
	@Override
	public String toString()
	{
		return "[SkillId: " + _skillId + " Level: " + _skillLevel + "]";
	}
}