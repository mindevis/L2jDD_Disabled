
package handlers.telnethandlers.server;

import org.l2jdd.Config;
import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class Purge implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "purge";
	}
	
	@Override
	public String getUsage()
	{
		return "Purge";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		ThreadPool.purge();
		final StringBuilder sb = new StringBuilder("STATUS OF THREAD POOLS AFTER PURGE COMMAND:" + Config.EOL);
		for (String line : ThreadPool.getStats())
		{
			sb.append(line + Config.EOL);
		}
		return sb.toString();
	}
}
