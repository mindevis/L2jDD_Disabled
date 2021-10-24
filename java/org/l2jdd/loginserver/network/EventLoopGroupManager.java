
package org.l2jdd.loginserver.network;

import org.l2jdd.Config;

import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author Nos
 */
public class EventLoopGroupManager
{
	private final NioEventLoopGroup _bossGroup = new NioEventLoopGroup(1);
	private final NioEventLoopGroup _workerGroup = new NioEventLoopGroup(Config.IO_PACKET_THREAD_CORE_SIZE);
	
	public NioEventLoopGroup getBossGroup()
	{
		return _bossGroup;
	}
	
	public NioEventLoopGroup getWorkerGroup()
	{
		return _workerGroup;
	}
	
	public void shutdown()
	{
		_bossGroup.shutdownGracefully();
		_workerGroup.shutdownGracefully();
	}
	
	public static EventLoopGroupManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final EventLoopGroupManager INSTANCE = new EventLoopGroupManager();
	}
}
