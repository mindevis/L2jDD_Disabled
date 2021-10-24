
package handlers.bypasshandlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.l2jdd.Config;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.handler.IBypassHandler;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.network.serverpackets.CreatureSay;

/**
 * @author Mobius (based on Tenkai pvpzone)
 */
public class FindPvP implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"FindPvP"
	};
	
	@Override
	public boolean useBypass(String command, PlayerInstance player, Creature target)
	{
		if (!Config.ENABLE_FIND_PVP || !target.isNpc())
		{
			return false;
		}
		
		PlayerInstance mostPvP = null;
		int max = -1;
		for (PlayerInstance plr : World.getInstance().getPlayers())
		{
			if ((plr == null) //
				|| (plr.getPvpFlag() == 0) //
				|| (plr.getInstanceId() != 0) //
				|| plr.isGM() //
				|| plr.isInsideZone(ZoneId.PEACE) //
				|| plr.isInsideZone(ZoneId.SIEGE) //
				|| plr.isInsideZone(ZoneId.NO_SUMMON_FRIEND))
			{
				continue;
			}
			
			int count = 0;
			for (PlayerInstance pl : World.getInstance().getVisibleObjects(plr, PlayerInstance.class))
			{
				if ((pl.getPvpFlag() > 0) && !pl.isInsideZone(ZoneId.PEACE))
				{
					count++;
				}
			}
			
			if (count > max)
			{
				max = count;
				mostPvP = plr;
			}
		}
		
		if (mostPvP != null)
		{
			// Check if the player's clan is already outnumbering the PvP
			if (player.getClan() != null)
			{
				final Map<Integer, Integer> clanNumbers = new HashMap<>();
				int allyId = player.getAllyId();
				if (allyId == 0)
				{
					allyId = player.getClanId();
				}
				clanNumbers.put(allyId, 1);
				for (PlayerInstance known : World.getInstance().getVisibleObjects(mostPvP, PlayerInstance.class))
				{
					int knownAllyId = known.getAllyId();
					if (knownAllyId == 0)
					{
						knownAllyId = known.getClanId();
					}
					if (knownAllyId != 0)
					{
						if (clanNumbers.containsKey(knownAllyId))
						{
							clanNumbers.put(knownAllyId, clanNumbers.get(knownAllyId) + 1);
						}
						else
						{
							clanNumbers.put(knownAllyId, 1);
						}
					}
				}
				
				int biggestAllyId = 0;
				int biggestAmount = 2;
				for (Entry<Integer, Integer> clanNumber : clanNumbers.entrySet())
				{
					if (clanNumber.getValue() > biggestAmount)
					{
						biggestAllyId = clanNumber.getKey();
						biggestAmount = clanNumber.getValue();
					}
				}
				
				if (biggestAllyId == allyId)
				{
					player.sendPacket(new CreatureSay(null, ChatType.WHISPER, target.getName(), "Sorry, your clan/ally is outnumbering the place already so you can't move there."));
					return true;
				}
			}
			
			player.teleToLocation((mostPvP.getX() + Rnd.get(300)) - 150, (mostPvP.getY() + Rnd.get(300)) - 150, mostPvP.getZ());
			player.setSpawnProtection(true);
			if (!player.isGM())
			{
				player.setPvpFlagLasts(Chronos.currentTimeMillis() + Config.PVP_PVP_TIME);
				player.startPvPFlag();
			}
		}
		else
		{
			player.sendPacket(new CreatureSay(null, ChatType.WHISPER, target.getName(), "Sorry, I can't find anyone in flag status right now."));
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
