
package org.l2jdd.gameserver.model.eventengine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.commons.database.DatabaseFactory;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.model.StatSet;

import it.sauronsoftware.cron4j.PastPredictor;
import it.sauronsoftware.cron4j.Predictor;

/**
 * @author UnAfraid
 */
public class EventScheduler
{
	private static final Logger LOGGER = Logger.getLogger(EventScheduler.class.getName());
	private final AbstractEventManager<?> _eventManager;
	private final String _name;
	private final String _pattern;
	private final boolean _repeat;
	private List<EventMethodNotification> _notifications;
	private ScheduledFuture<?> _task;
	
	public EventScheduler(AbstractEventManager<?> manager, StatSet set)
	{
		_eventManager = manager;
		_name = set.getString("name", "");
		_pattern = set.getString("minute", "*") + " " + set.getString("hour", "*") + " " + set.getString("dayOfMonth", "*") + " " + set.getString("month", "*") + " " + set.getString("dayOfWeek", "*");
		_repeat = set.getBoolean("repeat", false);
	}
	
	public String getName()
	{
		return _name;
	}
	
	public long getNextSchedule()
	{
		final Predictor predictor = new Predictor(_pattern);
		return predictor.nextMatchingTime();
	}
	
	public long getNextSchedule(long fromTime)
	{
		final Predictor predictor = new Predictor(_pattern, fromTime);
		return predictor.nextMatchingTime();
	}
	
	public long getPrevSchedule()
	{
		final PastPredictor predictor = new PastPredictor(_pattern);
		return predictor.prevMatchingTime();
	}
	
	public long getPrevSchedule(long fromTime)
	{
		final PastPredictor predictor = new PastPredictor(_pattern, fromTime);
		return predictor.prevMatchingTime();
	}
	
	public boolean isRepeating()
	{
		return _repeat;
	}
	
	public void addEventNotification(EventMethodNotification notification)
	{
		if (_notifications == null)
		{
			_notifications = new ArrayList<>();
		}
		_notifications.add(notification);
	}
	
	public List<EventMethodNotification> getEventNotifications()
	{
		return _notifications;
	}
	
	public void startScheduler()
	{
		if (_notifications == null)
		{
			LOGGER.info("Scheduler without notificator manager: " + _eventManager.getClass().getSimpleName() + " pattern: " + _pattern);
			return;
		}
		
		final Predictor predictor = new Predictor(_pattern);
		final long nextSchedule = predictor.nextMatchingTime();
		final long timeSchedule = nextSchedule - Chronos.currentTimeMillis();
		if (timeSchedule <= (30 * 1000))
		{
			LOGGER.warning("Wrong reschedule for " + _eventManager.getClass().getSimpleName() + " end up run in " + (timeSchedule / 1000) + " seconds!");
			ThreadPool.schedule(this::startScheduler, timeSchedule + 1000);
			return;
		}
		
		if (_task != null)
		{
			_task.cancel(false);
		}
		
		_task = ThreadPool.schedule(() ->
		{
			run();
			updateLastRun();
			
			if (_repeat)
			{
				ThreadPool.schedule(this::startScheduler, 1000);
			}
		}, timeSchedule);
	}
	
	public boolean updateLastRun()
	{
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO event_schedulers (eventName, schedulerName, lastRun) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE eventName = ?, schedulerName = ?, lastRun = ?"))
		{
			ps.setString(1, _eventManager.getName());
			ps.setString(2, _name);
			ps.setTimestamp(3, new Timestamp(Chronos.currentTimeMillis()));
			ps.setString(4, _eventManager.getName());
			ps.setString(5, _name);
			ps.setTimestamp(6, new Timestamp(Chronos.currentTimeMillis()));
			ps.execute();
			return true;
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Failed to insert/update information for scheduled task manager: " + _eventManager.getClass().getSimpleName() + " scheduler: " + _name, e);
		}
		return false;
	}
	
	public void stopScheduler()
	{
		if (_task != null)
		{
			_task.cancel(false);
			_task = null;
		}
	}
	
	public long getRemainingTime(TimeUnit unit)
	{
		return (_task != null) && !_task.isDone() ? _task.getDelay(unit) : 0;
	}
	
	public void run()
	{
		for (EventMethodNotification notification : _notifications)
		{
			try
			{
				notification.execute();
			}
			catch (Exception e)
			{
				LOGGER.warning("Failed to notify to event manager: " + notification.getManager().getClass().getSimpleName() + " method: " + notification.getMethod().getName());
			}
		}
	}
}
