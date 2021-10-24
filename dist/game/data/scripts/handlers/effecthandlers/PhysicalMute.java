
package handlers.effecthandlers;

import org.l2jdd.gameserver.ai.CtrlEvent;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Physical Mute effect implementation.
 * @author -Nemesiss-
 */
public class PhysicalMute extends AbstractEffect
{
	public PhysicalMute(StatSet params)
	{
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.PSYCHICAL_MUTED.getMask();
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		effected.getAI().notifyEvent(CtrlEvent.EVT_MUTED);
	}
}
