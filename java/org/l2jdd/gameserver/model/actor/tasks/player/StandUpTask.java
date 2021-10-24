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
