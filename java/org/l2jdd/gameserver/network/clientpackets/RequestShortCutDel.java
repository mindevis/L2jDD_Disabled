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
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.Shortcut;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.taskmanager.AutoUseTaskManager;

/**
 * @version $Revision: 1.3.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestShortCutDel implements IClientIncomingPacket
{
	private int _id;
	private int _slot;
	private int _page;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_id = packet.readD();
		_slot = _id % 12;
		_page = _id / 12;
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if ((_page > 23) || (_page < 0))
		{
			return;
		}
		
		// Store shortcut reference id.
		final Shortcut shortcut = player.getShortCut(_slot, _page);
		final int id = shortcut == null ? -1 : shortcut.getId();
		
		// Delete the shortcut.
		player.deleteShortCut(_slot, _page);
		
		// Remove auto used ids.
		if (_slot > 263)
		{
			AutoUseTaskManager.getInstance().removeAutoSupplyItem(player, id);
		}
		else
		{
			AutoUseTaskManager.getInstance().removeAutoSkill(player, id);
		}
	}
}
