
package org.l2jdd.gameserver.model.actor.tasks.npc.trap;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.model.actor.instance.TrapInstance;

/**
 * Trap trigger task.
 * @author Zoey76
 */
public class TrapTriggerTask implements Runnable
{
	private final TrapInstance _trap;
	
	public TrapTriggerTask(TrapInstance trap)
	{
		_trap = trap;
	}
	
	@Override
	public void run()
	{
		try
		{
			_trap.doCast(_trap.getSkill());
			ThreadPool.schedule(new TrapUnsummonTask(_trap), _trap.getSkill().getHitTime() + 300);
		}
		catch (Exception e)
		{
			_trap.unSummon();
		}
	}
}
