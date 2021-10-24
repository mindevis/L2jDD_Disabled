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
package org.l2jdd.gameserver.network.clientpackets.teleports;

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.xml.TeleportListData;
import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.EffectFlag;
import org.l2jdd.gameserver.model.holders.TeleportListHolder;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;

/**
 * @author NviX, Mobius
 */
public class ExRequestTeleport implements IClientIncomingPacket
{
	private int _teleportId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_teleportId = packet.readD();
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
		
		final TeleportListHolder teleport = TeleportListData.getInstance().getTeleport(_teleportId);
		if (teleport == null)
		{
			LOGGER.warning("No registered teleport location for id: " + _teleportId);
			return;
		}
		
		// Dead characters cannot use teleports.
		if (player.isDead())
		{
			player.sendPacket(SystemMessageId.CANNOT_TELEPORT_WHILE_DEAD);
			return;
		}
		
		// Players should not be able to teleport if in combat, or in a special location.
		if (player.isCastingNow() || player.isInCombat() || player.isImmobilized() || player.isInInstance() || player.isOnEvent() || player.isInOlympiadMode() || player.inObserverMode() || player.isInTraingCamp() || player.isInsideZone(ZoneId.TIMED_HUNTING))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_TELEPORT_RIGHT_NOW);
			return;
		}
		
		// Karma related configurations.
		if ((!Config.ALT_GAME_KARMA_PLAYER_CAN_TELEPORT || !Config.ALT_GAME_KARMA_PLAYER_CAN_USE_GK) && (player.getReputation() < 0))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_TELEPORT_RIGHT_NOW);
			return;
		}
		
		// Cannot escape effect.
		if (player.isAffected(EffectFlag.CANNOT_ESCAPE))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_TELEPORT_RIGHT_NOW);
			return;
		}
		
		if (!Config.TELEPORT_WHILE_SIEGE_IN_PROGRESS)
		{
			final Castle castle = CastleManager.getInstance().getCastle(teleport.getX(), teleport.getY(), teleport.getZ());
			if ((castle != null) && castle.getSiege().isInProgress())
			{
				player.sendPacket(SystemMessageId.YOU_CANNOT_TELEPORT_TO_A_VILLAGE_THAT_IS_IN_A_SIEGE);
				return;
			}
		}
		
		if (player.getLevel() > Config.MAX_FREE_TELEPORT_LEVEL)
		{
			final int price = teleport.getPrice();
			if (price > 0)
			{
				if (player.getAdena() < price)
				{
					player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
					return;
				}
				player.reduceAdena("Teleport", price, player, true);
			}
		}
		
		player.abortCast();
		player.stopMove(null);
		player.teleToLocation(teleport.getX(), teleport.getY(), teleport.getZ());
	}
}