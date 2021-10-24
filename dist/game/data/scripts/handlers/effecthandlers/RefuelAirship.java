
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.AirShipInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Refuel Airship effect implementation.
 * @author Adry_85
 */
public class RefuelAirship extends AbstractEffect
{
	private final int _value;
	
	public RefuelAirship(StatSet params)
	{
		_value = params.getInt("value", 0);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.REFUEL_AIRSHIP;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final AirShipInstance ship = effector.getActingPlayer().getAirShip();
		ship.setFuel(ship.getFuel() + _value);
		ship.updateAbnormalVisualEffects();
	}
}
