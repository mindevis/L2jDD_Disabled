
package ai.others.NpcBuffers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
class NpcBufferSkillData
{
	private final SkillHolder _skill;
	private final int _scaleToLevel;
	private final int _initialDelay;
	
	NpcBufferSkillData(StatSet set)
	{
		_skill = new SkillHolder(set.getInt("id"), set.getInt("level"));
		_scaleToLevel = set.getInt("scaleToLevel", -1);
		_initialDelay = set.getInt("initialDelay", 0) * 1000;
	}
	
	public Skill getSkill()
	{
		return _skill.getSkill();
	}
	
	public int getScaleToLevel()
	{
		return _scaleToLevel;
	}
	
	public int getInitialDelay()
	{
		return _initialDelay;
	}
}
