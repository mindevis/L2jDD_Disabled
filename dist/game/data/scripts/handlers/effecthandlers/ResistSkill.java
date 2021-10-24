
package handlers.effecthandlers;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Resist Skill effect implementaion.
 * @author UnAfraid
 */
public class ResistSkill extends AbstractEffect
{
	private final List<SkillHolder> _skills = new ArrayList<>();
	
	public ResistSkill(StatSet params)
	{
		for (int i = 1;; i++)
		{
			final int skillId = params.getInt("skillId" + i, 0);
			final int skillLevel = params.getInt("skillLevel" + i, 0);
			if (skillId == 0)
			{
				break;
			}
			_skills.add(new SkillHolder(skillId, skillLevel));
		}
		
		if (_skills.isEmpty())
		{
			throw new IllegalArgumentException(getClass().getSimpleName() + ": Without parameters!");
		}
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		for (SkillHolder holder : _skills)
		{
			effected.addIgnoreSkillEffects(holder);
		}
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		for (SkillHolder holder : _skills)
		{
			effected.removeIgnoreSkillEffects(holder);
		}
	}
}
