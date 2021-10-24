
package handlers.telnethandlers.chat;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.CreatureSay;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class Msg implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "msg";
	}
	
	@Override
	public String getUsage()
	{
		return "Msg <player> <text>";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		if ((args.length < 2) || args[0].isEmpty())
		{
			return null;
		}
		final PlayerInstance player = World.getInstance().getPlayer(args[0]);
		if (player != null)
		{
			final StringBuilder sb = new StringBuilder();
			for (int i = 1; i < args.length; i++)
			{
				sb.append(args[i] + " ");
			}
			player.sendPacket(new CreatureSay(null, ChatType.WHISPER, "Telnet Priv", sb.toString()));
			return "Announcement sent!";
		}
		return "Couldn't find player with such name.";
	}
}
