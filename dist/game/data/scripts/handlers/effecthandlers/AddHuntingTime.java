
package handlers.effecthandlers;

import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.data.xml.TimedHuntingZoneData;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.holders.TimedHuntingZoneHolder;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.variables.PlayerVariables;
import org.l2jdd.gameserver.network.serverpackets.huntingzones.TimedHuntingZoneList;

/**
 * @author Mobius
 */
public class AddHuntingTime extends AbstractEffect
{
	private final int _zoneId;
	private final long _time;
	
	public AddHuntingTime(StatSet params)
	{
		_zoneId = params.getInt("zoneId", 0);
		_time = params.getLong("time", 3600000);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final PlayerInstance player = effected.getActingPlayer();
		if (player == null)
		{
			return;
		}
		
		final TimedHuntingZoneHolder holder = TimedHuntingZoneData.getInstance().getHuntingZone(_zoneId);
		if (holder == null)
		{
			return;
		}
		
		final long currentTime = Chronos.currentTimeMillis();
		long endTime = currentTime + player.getTimedHuntingZoneRemainingTime(_zoneId);
		if ((endTime > currentTime) && (((endTime - currentTime) + _time) >= holder.getMaximumAddedTime()))
		{
			player.getInventory().addItem("AddHuntingTime effect refund", item.getId(), 1, player, player);
			player.sendMessage("You cannot exceed the time zone limit.");
			return;
		}
		
		if (player.isInTimedHuntingZone(_zoneId))
		{
			player.getVariables().set(PlayerVariables.HUNTING_ZONE_TIME + _zoneId, _time + player.getTimedHuntingZoneRemainingTime(_zoneId));
			player.startTimedHuntingZone(_zoneId, endTime);
		}
		else
		{
			if ((endTime + holder.getResetDelay()) < currentTime)
			{
				endTime = currentTime + holder.getInitialTime();
			}
			else if (endTime < currentTime)
			{
				endTime = currentTime;
			}
			player.getVariables().set(PlayerVariables.HUNTING_ZONE_TIME + _zoneId, (endTime - currentTime) + _time);
		}
		
		player.sendPacket(new TimedHuntingZoneList(player));
	}
}
