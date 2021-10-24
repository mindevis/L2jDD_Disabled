
package handlers.telnethandlers.server;

import org.l2jdd.gameserver.network.telnet.ITelnetCommand;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class ForceGC implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "forcegc";
	}
	
	@Override
	public String getUsage()
	{
		return "ForceGC";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		System.gc();
		return "RAM Used: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576);
	}
}
