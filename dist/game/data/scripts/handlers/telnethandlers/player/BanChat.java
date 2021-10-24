
package handlers.telnethandlers.player;

import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.data.sql.CharNameTable;
import org.l2jdd.gameserver.instancemanager.PunishmentManager;
import org.l2jdd.gameserver.model.punishment.PunishmentAffect;
import org.l2jdd.gameserver.model.punishment.PunishmentTask;
import org.l2jdd.gameserver.model.punishment.PunishmentType;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;
import org.l2jdd.gameserver.util.Util;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author lion
 */
public class BanChat implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "ban_chat";
	}
	
	@Override
	public String getUsage()
	{
		return "ban_chat <player name> [time in minutes]";
	}
	
	@Override
	public String handle(ChannelHandlerContext ctx, String[] args)
	{
		if ((args.length == 0) || args[0].isEmpty())
		{
			return null;
		}
		final int objectId = CharNameTable.getInstance().getIdByName(args[0]);
		if (objectId > 0)
		{
			if (PunishmentManager.getInstance().hasPunishment(objectId, PunishmentAffect.CHARACTER, PunishmentType.CHAT_BAN))
			{
				return "Player is already chat banned.";
			}
			long time = -1;
			String reason = "Your chat have been banned by telnet admin.";
			if (args.length > 1)
			{
				final String token = args[1];
				if (Util.isDigit(token))
				{
					time = Integer.parseInt(token) * 60 * 1000;
					time += Chronos.currentTimeMillis();
				}
				if (args.length > 2)
				{
					reason = args[2];
					for (int i = 3; i < args.length; i++)
					{
						reason += " " + args[i];
					}
				}
			}
			PunishmentManager.getInstance().startPunishment(new PunishmentTask(objectId, PunishmentAffect.CHARACTER, PunishmentType.CHAT_BAN, time, reason, "Telnet Admin"));
			return "Player has been successfully banned.";
		}
		return "Couldn't find player with such name.";
	}
}
