
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.SkillCaster;

/**
 * Call Learned Skill by Level effect implementation.
 * @author Kazumi
 */
public class CallLearnedSkill extends AbstractEffect
{
	private final int _skillId;
	
	public CallLearnedSkill(StatSet params)
	{
		_skillId = params.getInt("skillId");
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final Skill knownSkill = effector.getKnownSkill(_skillId);
		if (knownSkill != null)
		{
			SkillCaster.triggerCast(effector, effected, knownSkill);
		}
	}
}