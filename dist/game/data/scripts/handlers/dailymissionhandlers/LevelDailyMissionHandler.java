
package handlers.dailymissionhandlers;

import org.l2jdd.gameserver.enums.DailyMissionStatus;
import org.l2jdd.gameserver.handler.AbstractDailyMissionHandler;
import org.l2jdd.gameserver.model.DailyMissionDataHolder;
import org.l2jdd.gameserver.model.DailyMissionPlayerEntry;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.Containers;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.creature.player.OnPlayerLevelChanged;
import org.l2jdd.gameserver.model.events.listeners.ConsumerEventListener;

/**
 * @author Sdw
 */
public class LevelDailyMissionHandler extends AbstractDailyMissionHandler
{
	private final int _level;
	private final boolean _dualclass;
	
	public LevelDailyMissionHandler(DailyMissionDataHolder holder)
	{
		super(holder);
		_level = holder.getParams().getInt("level");
		_dualclass = holder.isDualClassOnly();
	}
	
	@Override
	public void init()
	{
		Containers.Players().addListener(new ConsumerEventListener(this, EventType.ON_PLAYER_LEVEL_CHANGED, (OnPlayerLevelChanged event) -> onPlayerLevelChanged(event), this));
	}
	
	@Override
	public boolean isAvailable(PlayerInstance player)
	{
		final DailyMissionPlayerEntry entry = getPlayerEntry(player.getObjectId(), false);
		if (entry != null)
		{
			switch (entry.getStatus())
			{
				case NOT_AVAILABLE:
				{
					if ((player.getLevel() >= _level) && (player.isDualClassActive() == _dualclass))
					{
						entry.setStatus(DailyMissionStatus.AVAILABLE);
						storePlayerEntry(entry);
					}
					break;
				}
				case AVAILABLE:
				{
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean isLevelUpMission()
	{
		return true;
	}
	
	@Override
	public void reset()
	{
		// Level rewards doesn't reset daily
	}
	
	@Override
	public int getProgress(PlayerInstance player)
	{
		return _level;
	}
	
	private void onPlayerLevelChanged(OnPlayerLevelChanged event)
	{
		final PlayerInstance player = event.getPlayer();
		if ((player.getLevel() >= _level) && (player.isDualClassActive() == _dualclass))
		{
			final DailyMissionPlayerEntry entry = getPlayerEntry(player.getObjectId(), true);
			if (entry.getStatus() == DailyMissionStatus.NOT_AVAILABLE)
			{
				entry.setStatus(DailyMissionStatus.AVAILABLE);
				storePlayerEntry(entry);
			}
		}
	}
}
