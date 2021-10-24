
package handlers.effecthandlers;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.SkillCaster;

/**
 * @author Mobius
 */
public class CallRandomSkill extends AbstractEffect
{
	private final List<SkillHolder> _skills = new ArrayList<>();
	
	public CallRandomSkill(StatSet params)
	{
		final String skills = params.getString("skills", null);
		if (skills != null)
		{
			for (String skill : skills.split(";"))
			{
				_skills.add(new SkillHolder(Integer.parseInt(skill.split(",")[0]), Integer.parseInt(skill.split(",")[1])));
			}
		}
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		SkillCaster.triggerCast(effector, effected, _skills.get(Rnd.get(_skills.size())).getSkill());
	}
}
