
package org.l2jdd.gameserver.model.skills;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jdd.gameserver.model.holders.ItemSkillHolder;

/**
 * @author Mobius
 */
public class AmmunitionSkillList
{
	private static final Set<Integer> SKILLS = ConcurrentHashMap.newKeySet();
	
	public static void add(List<ItemSkillHolder> skills)
	{
		for (ItemSkillHolder skill : skills)
		{
			SKILLS.add(skill.getSkillId());
		}
	}
	
	public static Set<Integer> values()
	{
		return SKILLS;
	}
}
