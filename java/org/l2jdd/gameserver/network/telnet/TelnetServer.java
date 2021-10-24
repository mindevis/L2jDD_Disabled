
package org.l2jdd.gameserver.network.telnet;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.l2jdd.Config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author UnAfraid
 */
public class TelnetServer
{
	private static final Logger LOGGER = Logger.getLogger(TelnetServer.class.getName());
	private final Map<String, ITelnetCommand> _commands = new LinkedHashMap<>();
	private final EventLoopGroup _workerGroup = new NioEventLoopGroup(1);
	
	protected TelnetServer()
	{
		if (Config.TELNET_ENABLED)
		{
			init();
		}
		else
		{
			LOGGER.info("Telnet server is currently disabled.");
		}
	}
	
	private void init()
	{
		addHandler(new ITelnetCommand()
		{
			@Override
			public String getCommand()
			{
				return "help";
			}
			
			@Override
			public String getUsage()
			{
				return "help [command]";
			}
			
			@Override
			public String handle(ChannelHandlerContext ctx, String[] args)
			{
				if (args.length == 0)
				{
					final StringBuilder sb = new StringBuilder("Available commands:" + Config.EOL);
					for (ITelnetCommand cmd : TelnetServer.getInstance().getCommands())
					{
						sb.append(cmd.getCommand() + Config.EOL);
					}
					return sb.toString();
				}
				final ITelnetCommand cmd = TelnetServer.getInstance().getCommand(args[0]);
				if (cmd == null)
				{
					return "Unknown command." + Config.EOL;
				}
				return "Usage:" + Config.EOL + cmd.getUsage() + Config.EOL;
			}
		});
		
		try
		{
			final InetSocketAddress socket = Config.TELNET_HOSTNAME.equals("*") ? new InetSocketAddress(Config.TELNET_PORT) : new InetSocketAddress(Config.TELNET_HOSTNAME, Config.TELNET_PORT);
			//@formatter:off
			new ServerBootstrap().group(_workerGroup)
				.channel(NioServerSocketChannel.class)
				//.option(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new TelnetServerInitializer())
				.bind(socket);
			//@formatter:on
			LOGGER.info(getClass().getSimpleName() + ": Listening on " + Config.TELNET_HOSTNAME + ":" + Config.TELNET_PORT);
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	public void addHandler(ITelnetCommand handler)
	{
		_commands.put(handler.getCommand(), handler);
	}
	
	public ITelnetCommand getCommand(String command)
	{
		return _commands.get(command);
	}
	
	public Collection<ITelnetCommand> getCommands()
	{
		return _commands.values();
	}
	
	public void shutdown()
	{
		_workerGroup.shutdownGracefully();
		LOGGER.info("Shutting down..");
	}
	
	public static TelnetServer getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final TelnetServer INSTANCE = new TelnetServer();
	}
}
