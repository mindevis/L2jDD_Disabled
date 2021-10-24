
package handlers.telnethandlers.player;

import org.l2jdd.gameserver.data.sql.CharNameTable;
import org.l2jdd.gameserver.instancemanager.PunishmentManager;
import org.l2jdd.gameserver.model.punishment.PunishmentAffect;
import org.l2jdd.gameserver.model.punishment.PunishmentType;
import org.l2jdd.gameserver.network.telnet.ITelnetCommand;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author UnAfraid
 */
public class Unban implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "unban";
	}
	
	@Override
	public String getUsage()
	{
		return "Unban <player name>";
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
			if (!PunishmentManager.getInstance().hasPunishment(objectId, PunishmentAffect.CHARACTER, PunishmentType.BAN))
			{
				return "Player is not banned at all.";
			}
			PunishmentManager.getInstance().stopPunishment(objectId, PunishmentAffect.CHARACTER, PunishmentType.BAN);
			return "Player has been successfully unbanned.";
		}
		return "Couldn't find player with such name.";
	}
}
