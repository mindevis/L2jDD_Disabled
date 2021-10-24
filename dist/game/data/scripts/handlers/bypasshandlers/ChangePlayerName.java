
package handlers.bypasshandlers;

import org.l2jdd.Config;
import org.l2jdd.gameserver.data.sql.CharNameTable;
import org.l2jdd.gameserver.handler.IBypassHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.itemcontainer.PlayerInventory;
import org.l2jdd.gameserver.network.serverpackets.PartySmallWindowAll;
import org.l2jdd.gameserver.network.serverpackets.PartySmallWindowDeleteAll;
import org.l2jdd.gameserver.util.Util;

/**
 * @author Mobius
 */
public class ChangePlayerName implements IBypassHandler
{
	private static final int NAME_CHANGE_TICKET = 23622;
	
	private static final String[] COMMANDS =
	{
		"ChangePlayerName"
	};
	
	@Override
	public boolean useBypass(String command, PlayerInstance player, Creature target)
	{
		// Need to have at least one Name Change Ticket in order to proceed.
		final PlayerInventory inventory = player.getInventory();
		if (inventory.getAllItemsByItemId(NAME_CHANGE_TICKET).isEmpty())
		{
			return false;
		}
		
		final String newName = command.split(" ")[1].trim();
		if (!Util.isAlphaNumeric(newName))
		{
			player.sendMessage("Name must only contain alphanumeric characters.");
			return false;
		}
		if (CharNameTable.getInstance().doesCharNameExist(newName))
		{
			player.sendMessage("Name " + newName + " already exists.");
			return false;
		}
		
		// Destroy item.
		player.destroyItemByItemId("ChangePlayerName to " + newName, NAME_CHANGE_TICKET, 1, player, true);
		
		// Set name and proceed.
		player.setName(newName);
		if (Config.CACHE_CHAR_NAMES)
		{
			CharNameTable.getInstance().addName(player);
		}
		player.storeMe();
		
		player.sendMessage("Your name has been changed.");
		player.broadcastUserInfo();
		
		if (player.isInParty())
		{
			// Delete party window for other party members
			player.getParty().broadcastToPartyMembers(player, PartySmallWindowDeleteAll.STATIC_PACKET);
			for (PlayerInstance member : player.getParty().getMembers())
			{
				// And re-add
				if (member != player)
				{
					member.sendPacket(new PartySmallWindowAll(member, player.getParty()));
				}
			}
		}
		if (player.getClan() != null)
		{
			player.getClan().broadcastClanStatus();
		}
		
		return true;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
