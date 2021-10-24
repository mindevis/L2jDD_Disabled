
package org.l2jdd.gameserver.model;

import java.util.concurrent.ScheduledFuture;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.instancemanager.WalkingManager;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.events.EventDispatcher;
import org.l2jdd.gameserver.model.events.impl.creature.npc.OnNpcMoveRouteFinished;

/**
 * Holds info about current walk progress.
 * @author GKR, UnAfraid
 */
public class WalkInfo
{
	private final String _routeName;
	private ScheduledFuture<?> _walkCheckTask;
	private boolean _blocked = false;
	private boolean _suspended = false;
	private boolean _stoppedByAttack = false;
	private int _currentNode = 0;
	private boolean _forward = true; // Determines first --> last or first <-- last direction
	private long _lastActionTime; // Debug field
	
	public WalkInfo(String routeName)
	{
		_routeName = routeName;
	}
	
	/**
	 * @return name of route of this WalkInfo.
	 */
	public WalkRoute getRoute()
	{
		return WalkingManager.getInstance().getRoute(_routeName);
	}
	
	/**
	 * @return current node of this WalkInfo.
	 */
	public NpcWalkerNode getCurrentNode()
	{
		return getRoute().getNodeList().get(Math.min(Math.max(0, _currentNode), getRoute().getNodeList().size() - 1));
	}
	
	/**
	 * Calculate next node for this WalkInfo and send debug message from given npc
	 * @param npc NPC to debug message to be sent from
	 */
	public synchronized void calculateNextNode(Npc npc)
	{
		// Check this first, within the bounds of random moving, we have no conception of "first" or "last" node
		if (getRoute().getRepeatType() == WalkingManager.REPEAT_RANDOM)
		{
			int newNode = _currentNode;
			
			while (newNode == _currentNode)
			{
				newNode = Rnd.get(getRoute().getNodesCount());
			}
			
			_currentNode = newNode;
		}
		else
		{
			if (_forward)
			{
				_currentNode++;
			}
			else
			{
				_currentNode--;
			}
			
			if (_currentNode == getRoute().getNodesCount()) // Last node arrived
			{
				// Notify quest
				EventDispatcher.getInstance().notifyEventAsync(new OnNpcMoveRouteFinished(npc), npc);
				
				if (!getRoute().repeatWalk())
				{
					WalkingManager.getInstance().cancelMoving(npc);
					return;
				}
				
				switch (getRoute().getRepeatType())
				{
					case WalkingManager.REPEAT_GO_BACK:
					{
						_forward = false;
						_currentNode -= 2;
						break;
					}
					case WalkingManager.REPEAT_GO_FIRST:
					{
						_currentNode = 0;
						break;
					}
					case WalkingManager.REPEAT_TELE_FIRST:
					{
						npc.teleToLocation(npc.getSpawn().getLocation());
						_currentNode = 0;
						break;
					}
				}
			}
			else if (_currentNode == WalkingManager.NO_REPEAT) // First node arrived, when direction is first <-- last
			{
				_currentNode = 1;
				_forward = true;
			}
		}
	}
	
	/**
	 * @return {@code true} if walking task is blocked, {@code false} otherwise,
	 */
	public boolean isBlocked()
	{
		return _blocked;
	}
	
	/**
	 * @param value
	 */
	public void setBlocked(boolean value)
	{
		_blocked = value;
	}
	
	/**
	 * @return {@code true} if walking task is suspended, {@code false} otherwise,
	 */
	public boolean isSuspended()
	{
		return _suspended;
	}
	
	/**
	 * @param value
	 */
	public void setSuspended(boolean value)
	{
		_suspended = value;
	}
	
	/**
	 * @return {@code true} if walking task shall be stopped by attack, {@code false} otherwise,
	 */
	public boolean isStoppedByAttack()
	{
		return _stoppedByAttack;
	}
	
	/**
	 * @param value
	 */
	public void setStoppedByAttack(boolean value)
	{
		_stoppedByAttack = value;
	}
	
	/**
	 * @return the id of the current node in this walking task.
	 */
	public int getCurrentNodeId()
	{
		return _currentNode;
	}
	
	/**
	 * @return {@code long} last action time used only for debugging.
	 */
	public long getLastAction()
	{
		return _lastActionTime;
	}
	
	/**
	 * @param value
	 */
	public void setLastAction(long value)
	{
		_lastActionTime = value;
	}
	
	/**
	 * @return walking check task.
	 */
	public ScheduledFuture<?> getWalkCheckTask()
	{
		return _walkCheckTask;
	}
	
	/**
	 * @param task walking check task.
	 */
	public void setWalkCheckTask(ScheduledFuture<?> task)
	{
		_walkCheckTask = task;
	}
	
	@Override
	public String toString()
	{
		return "WalkInfo [_routeName=" + _routeName + ", _walkCheckTask=" + _walkCheckTask + ", _blocked=" + _blocked + ", _suspended=" + _suspended + ", _stoppedByAttack=" + _stoppedByAttack + ", _currentNode=" + _currentNode + ", _forward=" + _forward + ", _lastActionTime=" + _lastActionTime + "]";
	}
}
