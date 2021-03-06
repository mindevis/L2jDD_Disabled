
package handlers.effecthandlers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jdd.gameserver.enums.StatModifierType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.creature.OnCreatureDamageReceived;
import org.l2jdd.gameserver.model.events.listeners.FunctionEventListener;
import org.l2jdd.gameserver.model.events.returns.DamageReturn;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Sdw, Mobius
 */
public class AbsorbDamage extends AbstractEffect
{
	private static final Map<Integer, Double> DIFF_DAMAGE_HOLDER = new ConcurrentHashMap<>();
	private static final Map<Integer, Double> PER_DAMAGE_HOLDER = new ConcurrentHashMap<>();
	
	private final double _damage;
	private final StatModifierType _mode;
	
	public AbsorbDamage(StatSet params)
	{
		_damage = params.getDouble("damage", 0);
		_mode = params.getEnum("mode", StatModifierType.class, StatModifierType.DIFF);
	}
	
	private DamageReturn onDamageReceivedDiffEvent(OnCreatureDamageReceived event, Creature effected, Skill skill)
	{
		// DOT effects are not taken into account.
		if (event.isDamageOverTime())
		{
			return null;
		}
		
		final int objectId = event.getTarget().getObjectId();
		
		final double damageLeft = DIFF_DAMAGE_HOLDER.getOrDefault(objectId, 0d);
		final double newDamageLeft = Math.max(damageLeft - event.getDamage(), 0);
		final double newDamage = Math.max(event.getDamage() - damageLeft, 0);
		
		if (newDamageLeft > 0)
		{
			effected.sendMessage("You absorbed " + (int) damageLeft + " damage from " + (int) event.getDamage() + ".");
			DIFF_DAMAGE_HOLDER.put(objectId, newDamageLeft);
		}
		else
		{
			effected.sendMessage("You absorbed " + (int) damageLeft + " damage from " + (int) event.getDamage() + " and barrier has been removed.");
			effected.stopSkillEffects(skill);
		}
		
		return new DamageReturn(false, true, false, newDamage);
	}
	
	private DamageReturn onDamageReceivedPerEvent(OnCreatureDamageReceived event)
	{
		// DOT effects are not taken into account.
		if (event.isDamageOverTime())
		{
			return null;
		}
		
		final int objectId = event.getTarget().getObjectId();
		
		final double damagePercent = PER_DAMAGE_HOLDER.getOrDefault(objectId, 0d);
		final double currentDamage = event.getDamage();
		final double newDamage = currentDamage - ((currentDamage / 100) * damagePercent);
		
		return new DamageReturn(false, true, false, newDamage);
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		effected.removeListenerIf(EventType.ON_CREATURE_DAMAGE_RECEIVED, listener -> listener.getOwner() == this);
		if (_mode == StatModifierType.DIFF)
		{
			DIFF_DAMAGE_HOLDER.remove(effected.getObjectId());
		}
		else
		{
			PER_DAMAGE_HOLDER.remove(effected.getObjectId());
		}
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (_mode == StatModifierType.DIFF)
		{
			DIFF_DAMAGE_HOLDER.put(effected.getObjectId(), _damage);
			effected.addListener(new FunctionEventListener(effected, EventType.ON_CREATURE_DAMAGE_RECEIVED, (OnCreatureDamageReceived event) -> onDamageReceivedDiffEvent(event, effected, skill), this));
		}
		else
		{
			PER_DAMAGE_HOLDER.put(effected.getObjectId(), _damage);
			effected.addListener(new FunctionEventListener(effected, EventType.ON_CREATURE_DAMAGE_RECEIVED, (OnCreatureDamageReceived event) -> onDamageReceivedPerEvent(event), this));
		}
	}
}
