
package handlers.effecthandlers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.ListenersContainer;
import org.l2jdd.gameserver.model.events.impl.creature.OnCreatureHpChange;
import org.l2jdd.gameserver.model.events.listeners.ConsumerEventListener;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
abstract class AbstractConditionalHpEffect extends AbstractStatEffect
{
	private final int _hpPercent;
	private final Map<Creature, AtomicBoolean> _updates = new ConcurrentHashMap<>();
	
	protected AbstractConditionalHpEffect(StatSet params, Stat stat)
	{
		super(params, stat);
		_hpPercent = params.getInt("hpPercent", 0);
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		// Augmentation option
		if (skill == null)
		{
			return;
		}
		
		// Register listeners
		if ((_hpPercent > 0) && !_updates.containsKey(effected))
		{
			_updates.put(effected, new AtomicBoolean(canPump(effector, effected, skill)));
			final ListenersContainer container = effected;
			container.addListener(new ConsumerEventListener(container, EventType.ON_CREATURE_HP_CHANGE, (OnCreatureHpChange event) -> onHpChange(event), this));
		}
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		// Augmentation option
		if (skill == null)
		{
			return;
		}
		
		effected.removeListenerIf(listener -> listener.getOwner() == this);
		_updates.remove(effected);
	}
	
	@Override
	public boolean canPump(Creature effector, Creature effected, Skill skill)
	{
		return (_hpPercent <= 0) || (effected.getCurrentHpPercent() <= _hpPercent);
	}
	
	private void onHpChange(OnCreatureHpChange event)
	{
		final Creature creature = event.getCreature();
		final AtomicBoolean update = _updates.get(creature);
		if (update == null)
		{
			return;
		}
		if (canPump(null, creature, null))
		{
			if (update.get())
			{
				update.set(false);
				creature.getStat().recalculateStats(true);
			}
		}
		else if (!update.get())
		{
			update.set(true);
			creature.getStat().recalculateStats(true);
		}
	}
}