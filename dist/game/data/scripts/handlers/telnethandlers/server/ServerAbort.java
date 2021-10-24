
package handlers.telnethandlers.server;

import org.l2jdd.gameserver.Shutdown;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class ServerAbort implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "abort";
	}
	
	@Override
	public String getUsage()
	{
		return "Abort";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		Shutdown.getInstance().abort(null);
		return "Server shutdown/restart aborted!";
	}
}
