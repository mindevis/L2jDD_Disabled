
package handlers.effecthandlers;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.l2jdd.gameserver.ai.CtrlEvent;
import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Block Actions effect implementation.
 * @author mkizub
 */
public class BlockActions extends AbstractEffect
{
	private final Set<Integer> _allowedSkills;
	
	public BlockActions(StatSet params)
	{
		final String[] allowedSkills = params.getString("allowedSkills", "").split(";");
		_allowedSkills = Arrays.stream(allowedSkills).filter(s -> !s.isEmpty()).map(Integer::parseInt).collect(Collectors.toSet());
	}
	
	@Override
	public long getEffectFlags()
	{
		return _allowedSkills.isEmpty() ? EffectFlag.BLOCK_ACTIONS.getMask() : EffectFlag.CONDITIONAL_BLOCK_ACTIONS.getMask();
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.BLOCK_ACTIONS;
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if ((effected == null) || effected.isRaid())
		{
			return;
		}
		
		_allowedSkills.stream().forEach(effected::addBlockActionsAllowedSkill);
		effected.startParalyze();
		// Cancel running skill casters.
		effected.abortAllSkillCasters();
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		_allowedSkills.stream().forEach(effected::removeBlockActionsAllowedSkill);
		if (effected.isPlayable())
		{
			if (effected.isSummon())
			{
				if ((effector != null) && !effector.isDead())
				{
					if (effector.isPlayable() && (effected.getActingPlayer().getPvpFlag() == 0))
					{
						effected.disableCoreAI(false);
					}
					else
					{
						((Summon) effected).doAutoAttack(effector);
					}
				}
				else
				{
					effected.disableCoreAI(false);
				}
			}
			else
			{
				effected.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
			}
		}
		else
		{
			effected.getAI().notifyEvent(CtrlEvent.EVT_THINK);
		}
	}
}
