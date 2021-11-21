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
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.AskJoinPledge;

public class RequestJoinPledge implements IClientIncomingPacket
{
	private int _target;
	private int _pledgeType;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_target = packet.readD();
		_pledgeType = packet.readD();
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
		
		if (!(World.getInstance().findObject(_target) instanceof PlayerInstance))
		{
			player.sendPacket(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET);
			return;
		}
		
		final PlayerInstance target = (PlayerInstance) World.getInstance().findObject(_target);
		final Clan clan = player.getClan();
		if (!clan.checkClanJoinCondition(player, target, _pledgeType))
		{
			return;
		}
		
		if (!player.getRequest().setRequest(target, this))
		{
			return;
		}
		
		target.sendPacket(new AskJoinPledge(player.getObjectId(), player.getClan().getName()));
	}
	
	public int getPledgeType()
	{
		return _pledgeType;
	}
}