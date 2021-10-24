
package handlers.telnethandlers.server;

import org.l2jdd.gameserver.Shutdown;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;
import org.l2jdd.gameserver.util.Util;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class ServerShutdown implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "shutdown";
	}
	
	@Override
	public String getUsage()
	{
		return "Shutdown <time in seconds>";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		if ((args.length == 0) || !Util.isDigit(args[0]))
		{
			return null;
		}
		final int time = Integer.parseInt(args[0]);
		Shutdown.getInstance().startShutdown(null, time, false);
		return "Server will shutdown in " + time + " seconds!";
	}
}
