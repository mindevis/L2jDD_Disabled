
package handlers.telnethandlers.server;

import org.l2jdd.gameserver.network.telnet.ITelnetCommand;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class Status implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "status";
	}
	
	@Override
	public String getUsage()
	{
		return "Status";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		return Debug.getServerStatus();
	}
}
