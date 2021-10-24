
package handlers.effecthandlers;

import org.l2jdd.commons.util.CommonUtil;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.creature.OnCreatureSkillUse;
import org.l2jdd.gameserver.model.events.listeners.FunctionEventListener;
import org.l2jdd.gameserver.model.events.returns.TerminateReturn;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Block Skills by isMagic type.
 * @author Nik
 */
public class BlockSkill extends AbstractEffect
{
	private final int[] _magicTypes;
	
	public BlockSkill(StatSet params)
	{
		_magicTypes = params.getIntArray("magicTypes", ";");
	}
	
	private TerminateReturn onSkillUseEvent(OnCreatureSkillUse event)
	{
		if (CommonUtil.contains(_magicTypes, event.getSkill().getMagicType()))
		{
			return new TerminateReturn(true, true, true);
		}
		
		return null;
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if ((_magicTypes == null) || (_magicTypes.length == 0))
		{
			return;
		}
		
		effected.addListener(new FunctionEventListener(effected, EventType.ON_CREATURE_SKILL_USE, (OnCreatureSkillUse event) -> onSkillUseEvent(event), this));
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		effected.removeListenerIf(EventType.ON_CREATURE_SKILL_USE, listener -> listener.getOwner() == this);
	}
}
