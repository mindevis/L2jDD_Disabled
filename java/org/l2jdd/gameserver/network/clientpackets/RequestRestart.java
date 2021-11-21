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

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.SkillTable;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.itemcontainer.Inventory;
import org.l2jdd.gameserver.model.olympiad.Olympiad;
import org.l2jdd.gameserver.model.sevensigns.SevenSignsFestival;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.network.ConnectionState;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;
import org.l2jdd.gameserver.network.serverpackets.CharSelectInfo;
import org.l2jdd.gameserver.network.serverpackets.RestartResponse;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;
import org.l2jdd.gameserver.taskmanager.AttackStanceTaskManager;

public class RequestRestart implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
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
		
		// Check if player is enchanting
		if (player.getActiveEnchantItem() != null)
		{
			player.sendPacket(RestartResponse.valueOf(false));
			return;
		}
		
		if (player.isInsideZone(ZoneId.NO_RESTART))
		{
			player.sendPacket(SystemMessageId.YOU_MAY_NOT_RESTART_IN_THIS_LOCATION);
			player.sendPacket(RestartResponse.valueOf(false));
			return;
		}
		
		// Check if player are changing class
		if (player.isLocked())
		{
			LOGGER.warning("Player " + player.getName() + " tried to restart during class change.");
			player.sendPacket(RestartResponse.valueOf(false));
			return;
		}
		
		// Check if player is in private store
		if (player.getPrivateStoreType() != 0)
		{
			player.sendMessage("Cannot restart while trading.");
			player.sendPacket(RestartResponse.valueOf(false));
			return;
		}
		
		// Check if player is in combat
		if (AttackStanceTaskManager.getInstance().hasAttackStanceTask(player) && (!player.isGM() || !Config.GM_RESTART_FIGHTING))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_RESTART_WHILE_IN_COMBAT);
			player.sendPacket(RestartResponse.valueOf(false));
			return;
		}
		
		// Check if player is registred on olympiad
		if ((player.getOlympiadGameId() > 0) || player.isInOlympiadMode() || Olympiad.getInstance().isRegistered(player))
		{
			player.sendMessage("You can't restart while in Olympiad.");
			player.sendPacket(RestartResponse.valueOf(false));
			return;
		}
		
		// Prevent player from restarting if they are a festival participant
		// and it is in progress, otherwise notify party members that the player
		// is not longer a participant.
		if (player.isFestivalParticipant())
		{
			if (SevenSignsFestival.getInstance().isFestivalInitialized())
			{
				player.sendPacket(SystemMessage.sendString("You cannot restart while you are a participant in a festival."));
				player.sendPacket(ActionFailed.STATIC_PACKET);
				player.sendPacket(RestartResponse.valueOf(false));
				return;
			}
			
			final Party playerParty = player.getParty();
			if (playerParty != null)
			{
				player.getParty().broadcastToPartyMembers(SystemMessage.sendString(player.getName() + " has been removed from the upcoming festival."));
			}
		}
		
		// Check if player is in Event
		if (player._inEventCTF || player._inEventDM || player._inEventTvT || player._inEventVIP)
		{
			player.sendMessage("You can't restart during Event.");
			player.sendPacket(RestartResponse.valueOf(false));
			return;
		}
		
		player.getInventory().updateDatabase();
		
		// Fix against exploit anti-target
		if (player.isCastingNow())
		{
			player.abortCast();
			player.sendPacket(new ActionFailed());
		}
		
		// Check if player is teleporting
		if (player.isTeleporting())
		{
			player.abortCast();
			player.setTeleporting(false);
		}
		
		// Check if player is trading
		if (player.getActiveRequester() != null)
		{
			player.getActiveRequester().onTradeCancel(player);
			player.onTradeCancel(player.getActiveRequester());
		}
		
		// Check if player are flying
		if (player.isFlying())
		{
			player.removeSkill(SkillTable.getInstance().getSkill(4289, 1));
		}
		
		if ((player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND) != null) && player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND).isAugmented())
		{
			player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND).getAugmentation().removeBonus(player);
		}
		
		// delete box from the world
		if (player._activeBoxes != -1)
		{
			player.decreaseBoxes();
		}
		
		// detach the client from the char so that the connection isnt closed in the deleteMe
		player.setClient(null);
		
		// removing player from the world
		player.deleteMe();
		player.store();
		
		client.setPlayer(null);
		
		// return the client to the authed status
		client.setConnectionState(ConnectionState.AUTHENTICATED);
		
		// Restart true
		client.sendPacket(RestartResponse.valueOf(true));
		
		// send char list
		final CharSelectInfo cl = new CharSelectInfo(client.getAccountName(), client.getSessionId().playOkID1);
		client.sendPacket(cl);
		client.setCharSelection(cl.getCharInfo());
	}
}