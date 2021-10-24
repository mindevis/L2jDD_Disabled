
package handlers.dailymissionhandlers;

import java.util.Calendar;

import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.enums.DailyMissionStatus;
import org.l2jdd.gameserver.handler.AbstractDailyMissionHandler;
import org.l2jdd.gameserver.model.DailyMissionDataHolder;
import org.l2jdd.gameserver.model.DailyMissionPlayerEntry;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.Containers;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.creature.player.OnPlayerLogin;
import org.l2jdd.gameserver.model.events.listeners.ConsumerEventListener;

/**
 * @author Iris, Mobius
 */
public class LoginWeekendDailyMissionHandler extends AbstractDailyMissionHandler
{
	public LoginWeekendDailyMissionHandler(DailyMissionDataHolder holder)
	{
		super(holder);
	}
	
	@Override
	public boolean isAvailable(PlayerInstance player)
	{
		final DailyMissionPlayerEntry entry = getPlayerEntry(player.getObjectId(), false);
		return (entry != null) && (entry.getStatus() == DailyMissionStatus.AVAILABLE);
	}
	
	@Override
	public void init()
	{
		Containers.Global().addListener(new ConsumerEventListener(this, EventType.ON_PLAYER_LOGIN, (OnPlayerLogin event) -> onPlayerLogin(event), this));
	}
	
	@Override
	public void reset()
	{
		// Weekend rewards do not reset daily.
	}
	
	private void onPlayerLogin(OnPlayerLogin event)
	{
		final DailyMissionPlayerEntry entry = getPlayerEntry(event.getPlayer().getObjectId(), true);
		final int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		final long lastCompleted = entry.getLastCompleted();
		if (((currentDay == Calendar.SATURDAY) || (currentDay == Calendar.SUNDAY)) // Reward only on weekend.
			&& ((lastCompleted == 0) || ((Chronos.currentTimeMillis() - lastCompleted) > 172800000))) // Initial entry or 172800000 (2 day) delay.
		{
			entry.setProgress(1);
			entry.setStatus(DailyMissionStatus.AVAILABLE);
		}
		else if (entry.getStatus() != DailyMissionStatus.AVAILABLE) // Not waiting to be rewarded.
		{
			entry.setProgress(0);
			entry.setStatus(DailyMissionStatus.NOT_AVAILABLE);
		}
		storePlayerEntry(entry);
	}
}