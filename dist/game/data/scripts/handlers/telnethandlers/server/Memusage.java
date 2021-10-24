
package handlers.telnethandlers.server;

import java.text.DecimalFormat;

import org.l2jdd.Config;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class Memusage implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "memusage";
	}
	
	@Override
	public String getUsage()
	{
		return "MemUsage";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		final double max = Runtime.getRuntime().maxMemory() / 1024; // maxMemory is the upper
		// limit the jvm can use
		final double allocated = Runtime.getRuntime().totalMemory() / 1024; // totalMemory the
		// size of the current allocation pool
		final double nonAllocated = max - allocated; // non allocated memory till jvm limit
		final double cached = Runtime.getRuntime().freeMemory() / 1024; // freeMemory the
		// unused memory in the allocation pool
		final double used = allocated - cached; // really used memory
		final double useable = max - used; // allocated, but non-used and non-allocated memory
		final StringBuilder sb = new StringBuilder();
		final DecimalFormat df = new DecimalFormat(" (0.0000'%')");
		final DecimalFormat df2 = new DecimalFormat(" # 'KB'");
		sb.append("+----" + Config.EOL); // ...
		sb.append("| Allowed Memory:" + df2.format(max) + Config.EOL);
		sb.append("|    |= Allocated Memory:" + df2.format(allocated) + df.format((allocated / max) * 100) + Config.EOL);
		sb.append("|    |= Non-Allocated Memory:" + df2.format(nonAllocated) + df.format((nonAllocated / max) * 100) + Config.EOL);
		sb.append("| Allocated Memory:" + df2.format(allocated) + Config.EOL);
		sb.append("|    |= Used Memory:" + df2.format(used) + df.format((used / max) * 100) + Config.EOL);
		sb.append("|    |= Unused (cached) Memory:" + df2.format(cached) + df.format((cached / max) * 100) + Config.EOL);
		sb.append("| Useable Memory:" + df2.format(useable) + df.format((useable / max) * 100) + Config.EOL); // ...
		sb.append("+----" + Config.EOL);
		return sb.toString();
	}
}
