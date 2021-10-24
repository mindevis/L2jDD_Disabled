
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.creature.OnCreatureDamageReceived;
import org.l2jdd.gameserver.model.events.listeners.FunctionEventListener;
import org.l2jdd.gameserver.model.events.returns.DamageReturn;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class SphericBarrier extends AbstractStatAddEffect
{
	public SphericBarrier(StatSet params)
	{
		super(params, Stat.SPHERIC_BARRIER_RANGE);
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		effected.addListener(new FunctionEventListener(effected, EventType.ON_CREATURE_DAMAGE_RECEIVED, (OnCreatureDamageReceived event) -> onDamageReceivedEvent(event), this));
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		effected.removeListenerIf(EventType.ON_CREATURE_DAMAGE_RECEIVED, listener -> listener.getOwner() == this);
	}
	
	private DamageReturn onDamageReceivedEvent(OnCreatureDamageReceived event)
	{
		if (event.getAttacker().calculateDistance3D(event.getTarget()) > _amount)
		{
			return new DamageReturn(false, true, false, 0);
		}
		return new DamageReturn(false, false, false, event.getDamage());
	}
}
