
package org.l2jdd.gameserver.model.actor.tasks.attackable;

import org.l2jdd.Config;
import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.model.actor.Attackable;

/**
 * @author xban1x
 */
public class CommandChannelTimer implements Runnable
{
	private final Attackable _attackable;
	
	public CommandChannelTimer(Attackable attackable)
	{
		_attackable = attackable;
	}
	
	@Override
	public void run()
	{
		if (_attackable == null)
		{
			return;
		}
		
		if ((Chronos.currentTimeMillis() - _attackable.getCommandChannelLastAttack()) > Config.LOOT_RAIDS_PRIVILEGE_INTERVAL)
		{
			_attackable.setCommandChannelTimer(null);
			_attackable.setFirstCommandChannelAttacked(null);
			_attackable.setCommandChannelLastAttack(0);
		}
		else
		{
			ThreadPool.schedule(this, 10000); // 10sec
		}
	}
}
