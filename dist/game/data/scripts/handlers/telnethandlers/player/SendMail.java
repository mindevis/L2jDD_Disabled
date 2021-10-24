
package handlers.telnethandlers.player;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.gameserver.data.sql.CharNameTable;
import org.l2jdd.gameserver.enums.MailType;
import org.l2jdd.gameserver.instancemanager.MailManager;
import org.l2jdd.gameserver.model.Message;
import org.l2jdd.gameserver.model.holders.ItemHolder;
import org.l2jdd.gameserver.model.itemcontainer.Mail;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;
import org.l2jdd.gameserver.util.Util;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author Mobius
 */
public class SendMail implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "sendmail";
	}
	
	@Override
	public String getUsage()
	{
		return "sendmail <player name> <mail subject (use _ for spaces)> <mail message (use _ for spaces)> <item(s) (optional) e.g. 57x1000000>";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		if ((args.length < 3) || args[0].isEmpty())
		{
			return null;
		}
		final int objectId = CharNameTable.getInstance().getIdByName(args[0]);
		if (objectId > 0)
		{
			final Message msg = new Message(objectId, args[1].replace("_", " "), args[2].replace("_", " "), args.length > 3 ? MailType.PRIME_SHOP_GIFT : MailType.REGULAR);
			final List<ItemHolder> itemHolders = new ArrayList<>();
			int counter = -1;
			for (String str : args)
			{
				counter++;
				if (counter < 3)
				{
					continue;
				}
				if (str.toLowerCase().contains("x"))
				{
					final String itemId = str.toLowerCase().split("x")[0];
					final String itemCount = str.toLowerCase().split("x")[1];
					if (Util.isDigit(itemId) && Util.isDigit(itemCount))
					{
						itemHolders.add(new ItemHolder(Integer.parseInt(itemId), Long.parseLong(itemCount)));
					}
				}
				else if (Util.isDigit(str))
				{
					itemHolders.add(new ItemHolder(Integer.parseInt(str), 1));
				}
			}
			if (!itemHolders.isEmpty())
			{
				final Mail attachments = msg.createAttachments();
				for (ItemHolder itemHolder : itemHolders)
				{
					attachments.addItem("Telnet-Mail", itemHolder.getId(), itemHolder.getCount(), null, null);
				}
			}
			MailManager.getInstance().sendMessage(msg);
			return "An ingame mail has been sent to " + args[0] + ".";
		}
		return "Couldn't find player with such name.";
	}
}
