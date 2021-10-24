
package handlers.telnethandlers.server;

import org.l2jdd.Config;
import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class Performance implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "performance";
	}
	
	@Override
	public String getUsage()
	{
		return "Performance";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		// ThreadPoolManager.purge();
		final StringBuilder sb = new StringBuilder();
		for (String line : ThreadPool.getStats())
		{
			sb.append(line + Config.EOL);
		}
		return sb.toString();
	}
}
