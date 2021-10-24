
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
public class Unjail implements ITelnetCommand
{
	@Override
	public String getCommand()
	{
		return "unjail";
	}
	
	@Override
	public String getUsage()
	{
		return "Unjail <player name>";
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
			if (!PunishmentManager.getInstance().hasPunishment(objectId, PunishmentAffect.CHARACTER, PunishmentType.JAIL))
			{
				return "Player is not jailed at all.";
			}
			PunishmentManager.getInstance().stopPunishment(objectId, PunishmentAffect.CHARACTER, PunishmentType.JAIL);
			return "Player has been successfully unjailed.";
		}
		return "Couldn't find player with such name.";
	}
}
