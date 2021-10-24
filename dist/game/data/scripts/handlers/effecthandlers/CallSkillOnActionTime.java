
package handlers.effecthandlers;

import java.util.Collections;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.SkillCaster;

/**
 * Dam Over Time effect implementation.
 */
public class CallSkillOnActionTime extends AbstractEffect
{
	private final SkillHolder _skill;
	
	public CallSkillOnActionTime(StatSet params)
	{
		_skill = new SkillHolder(params.getInt("skillId"), params.getInt("skillLevel", 1), params.getInt("skillSubLevel", 0));
		setTicks(params.getInt("ticks"));
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (!_skill.getSkill().isSynergySkill())
		{
			effected.getEffectList().stopEffects(Collections.singleton(_skill.getSkill().getAbnormalType()));
			effected.getEffectList().addBlockedAbnormalTypes(Collections.singleton(_skill.getSkill().getAbnormalType()));
		}
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		if (!_skill.getSkill().isSynergySkill())
		{
			effected.getEffectList().removeBlockedAbnormalTypes(Collections.singleton(_skill.getSkill().getAbnormalType()));
		}
	}
	
	@Override
	public boolean onActionTime(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effector.isDead())
		{
			return false;
		}
		
		final Skill triggerSkill = _skill.getSkill();
		if (triggerSkill != null)
		{
			if (triggerSkill.isSynergySkill())
			{
				triggerSkill.applyEffects(effector, effector);
			}
			
			World.getInstance().forEachVisibleObjectInRange(effector, Creature.class, _skill.getSkill().getAffectRange(), c ->
			{
				final WorldObject target = triggerSkill.getTarget(effector, c, false, false, false);
				
				if ((target != null) && target.isCreature())
				{
					SkillCaster.triggerCast(effector, (Creature) target, triggerSkill);
				}
			});
		}
		else
		{
			LOGGER.warning("Skill not found effect called from " + skill);
		}
		return skill.isToggle();
	}
}