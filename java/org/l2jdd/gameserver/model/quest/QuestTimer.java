
package org.l2jdd.gameserver.model.quest;

import java.util.concurrent.ScheduledFuture;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

public class QuestTimer
{
	protected final String _name;
	protected final Quest _quest;
	protected final Npc _npc;
	protected final PlayerInstance _player;
	protected final boolean _isRepeating;
	protected ScheduledFuture<?> _scheduler;
	
	public QuestTimer(Quest quest, String name, long time, Npc npc, PlayerInstance player, boolean repeating)
	{
		_quest = quest;
		_name = name;
		_npc = npc;
		_player = player;
		_isRepeating = repeating;
		
		if (repeating)
		{
			_scheduler = ThreadPool.scheduleAtFixedRate(new ScheduleTimerTask(), time, time); // Prepare auto end task
		}
		else
		{
			_scheduler = ThreadPool.schedule(new ScheduleTimerTask(), time); // Prepare auto end task
		}
		
		if (npc != null)
		{
			npc.addQuestTimer(this);
		}
		
		if (player != null)
		{
			player.addQuestTimer(this);
		}
	}
	
	public void cancel()
	{
		cancelTask();
		
		if (_npc != null)
		{
			_npc.removeQuestTimer(this);
		}
		
		if (_player != null)
		{
			_player.removeQuestTimer(this);
		}
	}
	
	public void cancelTask()
	{
		if ((_scheduler != null) && !_scheduler.isDone() && !_scheduler.isCancelled())
		{
			_scheduler.cancel(false);
			_scheduler = null;
		}
		_quest.removeQuestTimer(this);
	}
	
	/**
	 * public method to compare if this timer matches with the key attributes passed.
	 * @param quest : Quest instance to which the timer is attached
	 * @param name : Name of the timer
	 * @param npc : Npc instance attached to the desired timer (null if no npc attached)
	 * @param player : Player instance attached to the desired timer (null if no player attached)
	 * @return boolean
	 */
	public boolean equals(Quest quest, String name, Npc npc, PlayerInstance player)
	{
		if ((quest == null) || (quest != _quest))
		{
			return false;
		}
		
		if ((name == null) || !name.equals(_name))
		{
			return false;
		}
		
		return (npc == _npc) && (player == _player);
	}
	
	public boolean isActive()
	{
		return (_scheduler != null) && !_scheduler.isCancelled() && !_scheduler.isDone();
	}
	
	public boolean isRepeating()
	{
		return _isRepeating;
	}
	
	public Quest getQuest()
	{
		return _quest;
	}
	
	public Npc getNpc()
	{
		return _npc;
	}
	
	public PlayerInstance getPlayer()
	{
		return _player;
	}
	
	@Override
	public String toString()
	{
		return _name;
	}
	
	public class ScheduleTimerTask implements Runnable
	{
		@Override
		public void run()
		{
			if (_scheduler == null)
			{
				return;
			}
			
			if (!_isRepeating)
			{
				cancel();
			}
			
			_quest.notifyEvent(_name, _npc, _player);
		}
	}
}
