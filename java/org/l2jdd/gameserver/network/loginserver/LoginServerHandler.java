
package org.l2jdd.gameserver.network.loginserver;

import org.l2jdd.commons.network.ChannelInboundHandler;
import org.l2jdd.commons.network.IIncomingPacket;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author NosBit
 */
public class LoginServerHandler extends ChannelInboundHandler<LoginServerHandler>
{
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IIncomingPacket<LoginServerHandler> msg) throws Exception
	{
		msg.run(this);
	}
}
