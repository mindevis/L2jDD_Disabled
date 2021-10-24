
package org.l2jdd.gameserver.model.stats.finalizers;

import java.util.OptionalDouble;

import org.l2jdd.Config;
import org.l2jdd.gameserver.data.xml.ClanHallData;
import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.instancemanager.FortManager;
import org.l2jdd.gameserver.instancemanager.SiegeManager;
import org.l2jdd.gameserver.instancemanager.ZoneManager;
import org.l2jdd.gameserver.model.SiegeClan;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PetInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.residences.AbstractResidence;
import org.l2jdd.gameserver.model.residences.ResidenceFunction;
import org.l2jdd.gameserver.model.residences.ResidenceFunctionType;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.model.siege.Castle.CastleFunction;
import org.l2jdd.gameserver.model.siege.Fort;
import org.l2jdd.gameserver.model.siege.Fort.FortFunction;
import org.l2jdd.gameserver.model.siege.Siege;
import org.l2jdd.gameserver.model.stats.BaseStat;
import org.l2jdd.gameserver.model.stats.IStatFunction;
import org.l2jdd.gameserver.model.stats.Stat;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.model.zone.type.CastleZone;
import org.l2jdd.gameserver.model.zone.type.ClanHallZone;
import org.l2jdd.gameserver.model.zone.type.FortZone;
import org.l2jdd.gameserver.model.zone.type.MotherTreeZone;
import org.l2jdd.gameserver.util.Util;

/**
 * @author UnAfraid
 */
public class RegenHPFinalizer implements IStatFunction
{
	@Override
	public double calc(Creature creature, OptionalDouble base, Stat stat)
	{
		throwIfPresent(base);
		
		double baseValue = creature.isPlayer() ? creature.getActingPlayer().getTemplate().getBaseHpRegen(creature.getLevel()) : creature.getTemplate().getBaseHpReg();
		baseValue *= creature.isRaid() ? Config.RAID_HP_REGEN_MULTIPLIER : Config.HP_REGEN_MULTIPLIER;
		if (Config.CHAMPION_ENABLE && creature.isChampion())
		{
			baseValue *= Config.CHAMPION_HP_REGEN;
		}
		
		if (creature.isPlayer())
		{
			final PlayerInstance player = creature.getActingPlayer();
			final double siegeModifier = calcSiegeRegenModifier(player);
			if (siegeModifier > 0)
			{
				baseValue *= siegeModifier;
			}
			
			if (player.isInsideZone(ZoneId.CLAN_HALL) && (player.getClan() != null) && (player.getClan().getHideoutId() > 0))
			{
				final ClanHallZone zone = ZoneManager.getInstance().getZone(player, ClanHallZone.class);
				final int posChIndex = zone == null ? -1 : zone.getResidenceId();
				final int clanHallIndex = player.getClan().getHideoutId();
				if ((clanHallIndex > 0) && (clanHallIndex == posChIndex))
				{
					final AbstractResidence residense = ClanHallData.getInstance().getClanHallById(player.getClan().getHideoutId());
					if (residense != null)
					{
						final ResidenceFunction func = residense.getFunction(ResidenceFunctionType.HP_REGEN);
						if (func != null)
						{
							baseValue *= func.getValue();
						}
					}
				}
			}
			
			if (player.isInsideZone(ZoneId.CASTLE) && (player.getClan() != null) && (player.getClan().getCastleId() > 0))
			{
				final CastleZone zone = ZoneManager.getInstance().getZone(player, CastleZone.class);
				final int posCastleIndex = zone == null ? -1 : zone.getResidenceId();
				final int castleIndex = player.getClan().getCastleId();
				if ((castleIndex > 0) && (castleIndex == posCastleIndex))
				{
					final Castle castle = CastleManager.getInstance().getCastleById(player.getClan().getCastleId());
					if (castle != null)
					{
						final CastleFunction func = castle.getCastleFunction(Castle.FUNC_RESTORE_HP);
						if (func != null)
						{
							baseValue *= (func.getLvl() / 100);
						}
					}
				}
			}
			
			if (player.isInsideZone(ZoneId.FORT) && (player.getClan() != null) && (player.getClan().getFortId() > 0))
			{
				final FortZone zone = ZoneManager.getInstance().getZone(player, FortZone.class);
				final int posFortIndex = zone == null ? -1 : zone.getResidenceId();
				final int fortIndex = player.getClan().getFortId();
				if ((fortIndex > 0) && (fortIndex == posFortIndex))
				{
					final Fort fort = FortManager.getInstance().getFortById(player.getClan().getCastleId());
					if (fort != null)
					{
						final FortFunction func = fort.getFortFunction(Fort.FUNC_RESTORE_HP);
						if (func != null)
						{
							baseValue *= (func.getLvl() / 100);
						}
					}
				}
			}
			
			// Mother Tree effect is calculated at last
			if (player.isInsideZone(ZoneId.MOTHER_TREE))
			{
				final MotherTreeZone zone = ZoneManager.getInstance().getZone(player, MotherTreeZone.class);
				final int hpBonus = zone == null ? 0 : zone.getHpRegenBonus();
				baseValue += hpBonus;
			}
			
			// Calculate Movement bonus
			if (player.isSitting())
			{
				baseValue *= 1.5; // Sitting
			}
			else if (!player.isMoving())
			{
				baseValue *= 1.1; // Staying
			}
			else if (player.isRunning())
			{
				baseValue *= 0.7; // Running
			}
			
			// Add CON bonus
			baseValue *= creature.getLevelMod() * BaseStat.CON.calcBonus(creature);
		}
		else if (creature.isPet())
		{
			baseValue = ((PetInstance) creature).getPetLevelData().getPetRegenHP() * Config.PET_HP_REGEN_MULTIPLIER;
		}
		
		return Stat.defaultValue(creature, stat, baseValue);
	}
	
	private static double calcSiegeRegenModifier(PlayerInstance player)
	{
		if ((player == null) || (player.getClan() == null))
		{
			return 0;
		}
		
		final Siege siege = SiegeManager.getInstance().getSiege(player.getX(), player.getY(), player.getZ());
		if ((siege == null) || !siege.isInProgress())
		{
			return 0;
		}
		
		final SiegeClan siegeClan = siege.getAttackerClan(player.getClan().getId());
		if ((siegeClan == null) || siegeClan.getFlag().isEmpty() || !Util.checkIfInRange(200, player, siegeClan.getFlag().stream().findAny().get(), true))
		{
			return 0;
		}
		
		return 1.5; // If all is true, then modifier will be 50% more
	}
}
