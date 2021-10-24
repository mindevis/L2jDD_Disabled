
package ai.others.NpcBuffers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author UnAfraid
 */
class NpcBufferData
{
	private final int _id;
	private final List<NpcBufferSkillData> _skills = new ArrayList<>();
	
	NpcBufferData(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
	
	void addSkill(NpcBufferSkillData skill)
	{
		_skills.add(skill);
	}
	
	public List<NpcBufferSkillData> getSkills()
	{
		return _skills;
	}
}
