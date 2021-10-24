
package handlers.telnethandlers.player;

import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.Disconnection;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class Kick implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "kick";
	}
	
	@Override
	public String getUsage()
	{
		return "Kick <player name>";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		if ((args.length == 0) || args[0].isEmpty())
		{
			return null;
		}
		final PlayerInstance player = World.getInstance().getPlayer(args[0]);
		if (player != null)
		{
			Disconnection.of(player).defaultSequence(false);
			return "Player has been successfully kicked.";
		}
		return "Couldn't find player with such name.";
	}
}
