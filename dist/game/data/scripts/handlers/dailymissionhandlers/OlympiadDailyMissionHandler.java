
package handlers.dailymissionhandlers;

import org.l2jdd.gameserver.enums.DailyMissionStatus;
import org.l2jdd.gameserver.handler.AbstractDailyMissionHandler;
import org.l2jdd.gameserver.model.DailyMissionDataHolder;
import org.l2jdd.gameserver.model.DailyMissionPlayerEntry;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.Containers;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.olympiad.OnOlympiadMatchResult;
import org.l2jdd.gameserver.model.events.listeners.ConsumerEventListener;

/**
 * @author UnAfraid
 */
public class OlympiadDailyMissionHandler extends AbstractDailyMissionHandler
{
	private final int _amount;
	private final boolean _winOnly;
	
	public OlympiadDailyMissionHandler(DailyMissionDataHolder holder)
	{
		super(holder);
		_amount = holder.getRequiredCompletions();
		_winOnly = holder.getParams().getBoolean("winOnly", false);
	}
	
	@Override
	public void init()
	{
		Containers.Global().addListener(new ConsumerEventListener(this, EventType.ON_OLYMPIAD_MATCH_RESULT, (OnOlympiadMatchResult event) -> onOlympiadMatchResult(event), this));
	}
	
	@Override
	public boolean isAvailable(PlayerInstance player)
	{
		final DailyMissionPlayerEntry entry = getPlayerEntry(player.getObjectId(), false);
		if (entry != null)
		{
			switch (entry.getStatus())
			{
				case NOT_AVAILABLE: // Initial state
				{
					if (entry.getProgress() >= _amount)
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
	
	private void onOlympiadMatchResult(OnOlympiadMatchResult event)
	{
		if (event.getWinner() != null)
		{
			final DailyMissionPlayerEntry winnerEntry = getPlayerEntry(event.getWinner().getObjectId(), true);
			if (winnerEntry.getStatus() == DailyMissionStatus.NOT_AVAILABLE)
			{
				if (winnerEntry.increaseProgress() >= _amount)
				{
					winnerEntry.setStatus(DailyMissionStatus.AVAILABLE);
				}
				storePlayerEntry(winnerEntry);
			}
		}
		
		if (!_winOnly && (event.getLoser() != null))
		{
			final DailyMissionPlayerEntry loseEntry = getPlayerEntry(event.getLoser().getObjectId(), true);
			if (loseEntry.getStatus() == DailyMissionStatus.NOT_AVAILABLE)
			{
				if (loseEntry.increaseProgress() >= _amount)
				{
					loseEntry.setStatus(DailyMissionStatus.AVAILABLE);
				}
				storePlayerEntry(loseEntry);
			}
		}
	}
}
