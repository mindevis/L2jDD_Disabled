
package org.l2jdd.gameserver.model.actor.tasks.npc.trap;

import org.l2jdd.gameserver.model.actor.instance.TrapInstance;

/**
 * Trap unsummon task.
 * @author Zoey76
 */
public class TrapUnsummonTask implements Runnable
{
	private final TrapInstance _trap;
	
	public TrapUnsummonTask(TrapInstance trap)
	{
		_trap = trap;
	}
	
	@Override
	public void run()
	{
		_trap.unSummon();
	}
}