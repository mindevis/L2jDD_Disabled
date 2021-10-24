
package handlers.telnethandlers.player;

import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;
import org.l2jdd.gameserver.util.Util;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class AccessLevel implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "accesslevel";
	}
	
	@Override
	public String getUsage()
	{
		return "AccessLevel <player name> <access level>";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		if ((args.length < 2) || args[0].isEmpty() || !Util.isDigit(args[1]))
		{
			return null;
		}
		final PlayerInstance player = World.getInstance().getPlayer(args[0]);
		if (player != null)
		{
			final int level = Integer.parseInt(args[1]);
			player.setAccessLevel(level, true, true);
			return "Player " + player.getName() + "'s access level has been changed to: " + level;
		}
		return "Couldn't find player with such name.";
	}
}
