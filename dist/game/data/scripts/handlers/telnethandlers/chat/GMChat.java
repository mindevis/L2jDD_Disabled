
package handlers.telnethandlers.chat;

import org.l2jdd.gameserver.data.xml.AdminData;
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.network.serverpackets.CreatureSay;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class GMChat implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "gmchat";
	}
	
	@Override
	public String getUsage()
	{
		return "Gmchat <text>";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		if ((args.length == 0) || args[0].isEmpty())
		{
			return null;
		}
		final StringBuilder sb = new StringBuilder();
		for (String str : args)
		{
			sb.append(str + " ");
		}
		AdminData.getInstance().broadcastToGMs(new CreatureSay(null, ChatType.ALLIANCE, "Telnet GM Broadcast", sb.toString()));
		return "GMChat sent!";
	}
}
