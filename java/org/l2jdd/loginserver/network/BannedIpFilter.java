
package org.l2jdd.loginserver.network;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.l2jdd.loginserver.LoginController;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;

/**
 * @author lord_rex
 */
@Sharable
public class BannedIpFilter extends AbstractRemoteAddressFilter<InetSocketAddress>
{
	@Override
	protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) throws UnknownHostException
	{
		return !LoginController.getInstance().isBannedAddress(remoteAddress.getAddress());
	}
}
