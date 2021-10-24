
package org.l2jdd.gameserver.taskmanager;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.ai.CreatureAI;
import org.l2jdd.gameserver.model.actor.Attackable;

/**
 * @author Mobius
 */
public class AttackableThinkTaskManager
{
	private static final Set<Attackable> ATTACKABLES = ConcurrentHashMap.newKeySet();
	private static boolean _working = false;
	
	public AttackableThinkTaskManager()
	{
		ThreadPool.scheduleAtFixedRate(() ->
		{
			if (_working)
			{
				return;
			}
			_working = true;
			
			CreatureAI ai;
			for (Attackable attackable : ATTACKABLES)
			{
				if (attackable.hasAI())
				{
					ai = attackable.getAI();
					if (ai != null)
					{
						ai.onEvtThink();
					}
					else
					{
						remove(attackable);
					}
				}
				else
				{
					remove(attackable);
				}
			}
			
			_working = false;
		}, 1000, 1000);
	}
	
	public void add(Attackable attackable)
	{
		if (!ATTACKABLES.contains(attackable))
		{
			ATTACKABLES.add(attackable);
		}
	}
	
	public void remove(Attackable attackable)
	{
		ATTACKABLES.remove(attackable);
	}
	
	public static AttackableThinkTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final AttackableThinkTaskManager INSTANCE = new AttackableThinkTaskManager();
	}
}
