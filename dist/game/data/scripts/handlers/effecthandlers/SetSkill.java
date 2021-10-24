
package handlers.effecthandlers;

import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Set Skill effect implementation.
 * @author Zoey76
 */
public class SetSkill extends AbstractEffect
{
	private final int _skillId;
	private final int _skillLevel;
	
	public SetSkill(StatSet params)
	{
		_skillId = params.getInt("skillId", 0);
		_skillLevel = params.getInt("skillLevel", 1);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (!effected.isPlayer())
		{
			return;
		}
		
		final Skill setSkill = SkillData.getInstance().getSkill(_skillId, _skillLevel);
		if (setSkill == null)
		{
			return;
		}
		
		effected.getActingPlayer().addSkill(setSkill, true);
	}
}
