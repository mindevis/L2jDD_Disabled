
package handlers.admincommandhandlers;

import java.util.logging.Logger;

import org.l2jdd.gameserver.data.xml.BuyListData;
import org.l2jdd.gameserver.data.xml.MultisellData;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.buylist.ProductList;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;
import org.l2jdd.gameserver.network.serverpackets.BuyList;
import org.l2jdd.gameserver.network.serverpackets.ExBuySellList;
import org.l2jdd.gameserver.util.BuilderUtil;

/**
 * This class handles following admin commands:
 * <ul>
 * <li>gmshop = shows menu</li>
 * <li>buy id = shows shop with respective id</li>
 * </ul>
 */
public class AdminShop implements IAdminCommandHandler
{
	private static final Logger LOGGER = Logger.getLogger(AdminShop.class.getName());
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_buy",
		"admin_gmshop",
		"admin_multisell",
		"admin_exc_multisell"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		if (command.startsWith("admin_buy"))
		{
			try
			{
				handleBuyRequest(activeChar, command.substring(10));
			}
			catch (IndexOutOfBoundsException e)
			{
				BuilderUtil.sendSysMessage(activeChar, "Please specify buylist.");
			}
		}
		else if (command.equals("admin_gmshop"))
		{
			AdminHtml.showAdminHtml(activeChar, "gmshops.htm");
		}
		else if (command.startsWith("admin_multisell"))
		{
			try
			{
				final int listId = Integer.parseInt(command.substring(16).trim());
				MultisellData.getInstance().separateAndSend(listId, activeChar, null, false);
			}
			catch (NumberFormatException | IndexOutOfBoundsException e)
			{
				BuilderUtil.sendSysMessage(activeChar, "Please specify multisell list ID.");
			}
		}
		else if (command.toLowerCase().startsWith("admin_exc_multisell"))
		{
			try
			{
				final int listId = Integer.parseInt(command.substring(20).trim());
				MultisellData.getInstance().separateAndSend(listId, activeChar, null, true);
			}
			catch (NumberFormatException | IndexOutOfBoundsException e)
			{
				BuilderUtil.sendSysMessage(activeChar, "Please specify multisell list ID.");
			}
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void handleBuyRequest(PlayerInstance activeChar, String command)
	{
		int val = -1;
		try
		{
			val = Integer.parseInt(command);
		}
		catch (Exception e)
		{
			LOGGER.warning("admin buylist failed:" + command);
		}
		
		final ProductList buyList = BuyListData.getInstance().getBuyList(val);
		if (buyList != null)
		{
			activeChar.sendPacket(new BuyList(buyList, activeChar, 0));
			activeChar.sendPacket(new ExBuySellList(activeChar, false));
		}
		else
		{
			LOGGER.warning("no buylist with id:" + val);
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
		}
	}
}
