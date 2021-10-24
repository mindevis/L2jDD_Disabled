
package org.l2jdd.gameserver.network.telnet;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public interface ITelnetCommand
{
	String getCommand();
	
	String getUsage();
	
	String handle(ChannelHandlerContext ctx, String[] args);
}
