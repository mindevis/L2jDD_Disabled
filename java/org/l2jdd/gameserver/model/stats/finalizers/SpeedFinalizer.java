
package org.l2jdd.gameserver.model.stats.finalizers;

import java.util.OptionalDouble;

import org.l2jdd.Config;
import org.l2jdd.gameserver.data.xml.PetDataTable;
import org.l2jdd.gameserver.instancemanager.ZoneManager;
import org.l2jdd.gameserver.model.PetLevelData;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.stats.BaseStat;
import org.l2jdd.gameserver.model.stats.IStatFunction;
import org.l2jdd.gameserver.model.stats.Stat;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.model.zone.type.SwampZone;

/**
 * @author UnAfraid
 */
public class SpeedFinalizer implements IStatFunction
{
	@Override
	public double calc(Creature creature, OptionalDouble base, Stat stat)
	{
		throwIfPresent(base);
		
		double baseValue = getBaseSpeed(creature, stat);
		if (creature.isPlayer())
		{
			// Enchanted feet bonus
			baseValue += calcEnchantBodyPart(creature, Item.SLOT_FEET);
		}
		
		final byte speedStat = (byte) creature.getStat().getAdd(Stat.STAT_BONUS_SPEED, -1);
		if ((speedStat >= 0) && (speedStat < BaseStat.values().length))
		{
			final BaseStat baseStat = BaseStat.values()[speedStat];
			final double bonusDex = Math.max(0, baseStat.calcValue(creature) - 55);
			baseValue += bonusDex;
		}
		
		return validateValue(creature, Stat.defaultValue(creature, stat, baseValue), 1, creature.isPlayer() ? Config.MAX_RUN_SPEED : Double.MAX_VALUE);
	}
	
	@Override
	public double calcEnchantBodyPartBonus(int enchantLevel, boolean isBlessed)
	{
		if (isBlessed)
		{
			return (1 * Math.max(enchantLevel - 3, 0)) + (1 * Math.max(enchantLevel - 6, 0));
		}
		return (0.6 * Math.max(enchantLevel - 3, 0)) + (0.6 * Math.max(enchantLevel - 6, 0));
	}
	
	private double getBaseSpeed(Creature creature, Stat stat)
	{
		double baseValue = calcWeaponPlusBaseValue(creature, stat);
		if (creature.isPlayer())
		{
			final PlayerInstance player = creature.getActingPlayer();
			if (player.isMounted())
			{
				final PetLevelData data = PetDataTable.getInstance().getPetLevelData(player.getMountNpcId(), player.getMountLevel());
				if (data != null)
				{
					baseValue = data.getSpeedOnRide(stat);
					// if level diff with mount >= 10, it decreases move speed by 50%
					if ((player.getMountLevel() - creature.getLevel()) >= 10)
					{
						baseValue /= 2;
					}
					
					// if mount is hungry, it decreases move speed by 50%
					if (player.isHungry())
					{
						baseValue /= 2;
					}
				}
			}
			baseValue += Config.RUN_SPD_BOOST;
		}
		if (creature.isPlayable() && creature.isInsideZone(ZoneId.SWAMP))
		{
			final SwampZone zone = ZoneManager.getInstance().getZone(creature, SwampZone.class);
			if (zone != null)
			{
				baseValue *= zone.getMoveBonus();
			}
		}
		return baseValue;
	}
}
