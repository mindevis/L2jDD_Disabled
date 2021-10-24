
package org.l2jdd.gameserver.model.interfaces;

import java.util.Map;

import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
public interface ISkillsHolder
{
	Map<Integer, Skill> getSkills();
	
	Skill addSkill(Skill skill);
	
	Skill getKnownSkill(int skillId);
	
	int getSkillLevel(int skillId);
}
