
package handlers.telnethandlers.player;

import org.l2jdd.gameserver.data.ItemTable;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;
import org.l2jdd.gameserver.util.Util;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class Give implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "give";
	}
	
	@Override
	public String getUsage()
	{
		return "Give <player name> <item id> [item amount] [item enchant] [donators]";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		if ((args.length < 2) || args[0].isEmpty() || !Util.isDigit(args[1]))
		{
			return null;
		}
		final PlayerInstance player = World.getInstance().getPlayer(args[0]);
		if (player != null)
		{
			final int itemId = Integer.parseInt(args[1]);
			long amount = 1;
			int enchanted = 0;
			if (args.length > 2)
			{
				String token = args[2];
				if (Util.isDigit(token))
				{
					amount = Long.parseLong(token);
				}
				if (args.length > 3)
				{
					token = args[3];
					if (Util.isDigit(token))
					{
						enchanted = Integer.parseInt(token);
					}
				}
			}
			
			final ItemInstance item = ItemTable.getInstance().createItem("Telnet-Admin", itemId, amount, player, null);
			if (enchanted > 0)
			{
				item.setEnchantLevel(enchanted);
			}
			player.addItem("Telnet-Admin", item, null, true);
			return "Item has been successfully given to the player.";
		}
		return "Couldn't find player with such name.";
	}
}
