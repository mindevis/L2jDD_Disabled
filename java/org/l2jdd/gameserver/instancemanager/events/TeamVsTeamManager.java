
package org.l2jdd.gameserver.instancemanager.events;

import org.l2jdd.gameserver.instancemanager.QuestManager;
import org.l2jdd.gameserver.model.eventengine.AbstractEvent;
import org.l2jdd.gameserver.model.eventengine.AbstractEventManager;
import org.l2jdd.gameserver.model.eventengine.ScheduleTarget;
import org.l2jdd.gameserver.model.quest.Event;

/**
 * @author Mobius
 */
public class TeamVsTeamManager extends AbstractEventManager<AbstractEvent<?>>
{
	protected TeamVsTeamManager()
	{
	}
	
	@Override
	public void onInitialized()
	{
	}
	
	@ScheduleTarget
	protected void startEvent()
	{
		final Event event = (Event) QuestManager.getInstance().getQuest("TvT");
		if (event != null)
		{
			event.eventStart(null);
		}
	}
	
	public static TeamVsTeamManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final TeamVsTeamManager INSTANCE = new TeamVsTeamManager();
	}
}
