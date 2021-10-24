
package org.l2jdd.gameserver.model.stats.finalizers;

import java.util.OptionalDouble;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.stats.IStatFunction;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Sdw
 */
public class ShieldDefenceFinalizer implements IStatFunction
{
	@Override
	public double calc(Creature creature, OptionalDouble base, Stat stat)
	{
		throwIfPresent(base);
		
		return Stat.defaultValue(creature, stat, calcWeaponPlusBaseValue(creature, stat));
	}
}
