
package handlers.admincommandhandlers;

import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.itemcontainer.PlayerInventory;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.serverpackets.InventoryUpdate;

/**
 * @author Mobius
 */
public class AdminDestroyItems implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_destroy_items",
		"admin_destroy_all_items",
		"admin_destroyitems",
		"admin_destroyallitems"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		final PlayerInventory inventory = activeChar.getInventory();
		final InventoryUpdate iu = new InventoryUpdate();
		for (ItemInstance item : inventory.getItems())
		{
			if (item.isEquipped() && !command.contains("all"))
			{
				continue;
			}
			iu.addRemovedItem(item);
			inventory.destroyItem("Admin Destroy", item, activeChar, null);
		}
		activeChar.sendPacket(iu);
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
