
package org.l2jdd.gameserver.model.actor.tasks.player;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * Task dedicated to put player to stand up.
 * @author UnAfraid
 */
public class StandUpTask implements Runnable
{
	private final PlayerInstance _player;
	
	public StandUpTask(PlayerInstance player)
	{
		_player = player;
	}
	
	@Override
	public void run()
	{
		if (_player != null)
		{
			_player.setSitting(false);
			_player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		}
	}
}
