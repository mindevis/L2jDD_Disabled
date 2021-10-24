
package handlers.telnethandlers.chat;

import org.l2jdd.gameserver.network.telnet.ITelnetCommand;
import org.l2jdd.gameserver.util.Broadcast;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class Announce implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "announce";
	}
	
	@Override
	public String getUsage()
	{
		return "Announce <text>";
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
		Broadcast.toAllOnlinePlayers(sb.toString());
		return "Announcement sent!";
	}
}
