/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jdd.gameserver.model.eventengine;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.eventengine.drop.IEventDrop;
import org.l2jdd.gameserver.model.events.AbstractScript;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.ListenerRegisterType;
import org.l2jdd.gameserver.model.events.annotations.RegisterEvent;
import org.l2jdd.gameserver.model.events.annotations.RegisterType;
import org.l2jdd.gameserver.model.events.impl.creature.player.OnPlayerLogout;

/**
 * @author UnAfraid
 * @param <T>
 */
public abstract class AbstractEventManager<T extends AbstractEvent<?>>extends AbstractScript
{
	private String _name;
	private StatSet _variables = StatSet.EMPTY_STATSET;
	private Set<EventScheduler> _schedulers = Collections.emptySet();
	private Set<IConditionalEventScheduler> _conditionalSchedulers = Collections.emptySet();
	private Map<String, IEventDrop> _rewards = Collections.emptyMap();
	
	private final Set<T> _events = ConcurrentHashMap.newKeySet();
	private final Queue<PlayerInstance> _registeredPlayers = new ConcurrentLinkedDeque<>();
	private final AtomicReference<IEventState> _state = new AtomicReference<>();
	
	public abstract void onInitialized();
	
	/* ********************** */
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	/* ********************** */
	
	public StatSet getVariables()
	{
		return _variables;
	}
	
	public void setVariables(StatSet variables)
	{
		_variables = new StatSet(Collections.unmodifiableMap(variables.getSet()));
	}
	
	/* ********************** */
	
	public EventScheduler getScheduler(String name)
	{
		for (EventScheduler scheduler : _schedulers)
		{
			if (scheduler.getName().equalsIgnoreCase(name))
			{
				return scheduler;
			}
		}
		return null;
	}
	
	public void setSchedulers(Set<EventScheduler> schedulers)
	{
		_schedulers = Collections.unmodifiableSet(schedulers);
	}
	
	/* ********************** */
	
	public Set<IConditionalEventScheduler> getConditionalSchedulers()
	{
		return _conditionalSchedulers;
	}
	
	public void setConditionalSchedulers(Set<IConditionalEventScheduler> schedulers)
	{
		_conditionalSchedulers = Collections.unmodifiableSet(schedulers);
	}
	
	/* ********************** */
	
	public IEventDrop getRewards(String name)
	{
		return _rewards.get(name);
	}
	
	public void setRewards(Map<String, IEventDrop> rewards)
	{
		_rewards = Collections.unmodifiableMap(rewards);
	}
	
	/* ********************** */
	
	public Set<T> getEvents()
	{
		return _events;
	}
	
	/* ********************** */
	
	public void startScheduler()
	{
		_schedulers.forEach(EventScheduler::startScheduler);
	}
	
	public void stopScheduler()
	{
		_schedulers.forEach(EventScheduler::stopScheduler);
	}
	
	public void startConditionalSchedulers()
	{
		for (IConditionalEventScheduler scheduler : _conditionalSchedulers)
		{
			if (scheduler.test())
			{
				scheduler.run();
			}
		}
	}
	
	/* ********************** */
	
	public IEventState getState()
	{
		return _state.get();
	}
	
	public void setState(IEventState newState)
	{
		final IEventState previousState = _state.get();
		_state.set(newState);
		onStateChange(previousState, newState);
	}
	
	public boolean setState(IEventState previousState, IEventState newState)
	{
		if (_state.compareAndSet(previousState, newState))
		{
			onStateChange(previousState, newState);
			return true;
		}
		return false;
	}
	
	/* ********************** */
	
	public boolean registerPlayer(PlayerInstance player)
	{
		return canRegister(player, true) && _registeredPlayers.offer(player);
	}
	
	public boolean unregisterPlayer(PlayerInstance player)
	{
		return _registeredPlayers.remove(player);
	}
	
	public boolean isRegistered(PlayerInstance player)
	{
		return _registeredPlayers.contains(player);
	}
	
	public boolean canRegister(PlayerInstance player, boolean sendMessage)
	{
		return !_registeredPlayers.contains(player);
	}
	
	public Queue<PlayerInstance> getRegisteredPlayers()
	{
		return _registeredPlayers;
	}
	
	/* ********************** */
	@RegisterEvent(EventType.ON_PLAYER_LOGOUT)
	@RegisterType(ListenerRegisterType.GLOBAL)
	public void OnPlayerLogout(OnPlayerLogout event)
	{
		final PlayerInstance player = event.getPlayer();
		if (_registeredPlayers.remove(player))
		{
			onUnregisteredPlayer(player);
		}
	}
	
	/* ********************** */
	
	/**
	 * Triggered when a player is automatically removed from the event manager because he disconnected
	 * @param player
	 */
	protected void onUnregisteredPlayer(PlayerInstance player)
	{
	}
	
	/**
	 * Triggered when state is changed
	 * @param previousState
	 * @param newState
	 */
	protected void onStateChange(IEventState previousState, IEventState newState)
	{
	}
	
	/* ********************** */
	@Override
	public String getScriptName()
	{
		return getClass().getSimpleName();
	}
	
	@Override
	public Path getScriptPath()
	{
		return null;
	}
}
