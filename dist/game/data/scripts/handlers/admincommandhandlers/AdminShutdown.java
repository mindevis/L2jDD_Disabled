
package handlers.admincommandhandlers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.l2jdd.gameserver.GameTimeController;
import org.l2jdd.gameserver.Shutdown;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jdd.gameserver.util.BuilderUtil;
import org.l2jdd.gameserver.util.Util;

/**
 * This class handles following admin commands: - server_shutdown [sec] = shows menu or shuts down server in sec seconds
 */
public class AdminShutdown implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_server_shutdown",
		"admin_server_restart",
		"admin_server_abort"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		if (command.startsWith("admin_server_shutdown"))
		{
			try
			{
				final String val = command.substring(22);
				if (Util.isDigit(val))
				{
					serverShutdown(activeChar, Integer.parseInt(val), false);
				}
				else
				{
					BuilderUtil.sendSysMessage(activeChar, "Usage: //server_shutdown <seconds>");
					sendHtmlForm(activeChar);
				}
			}
			catch (StringIndexOutOfBoundsException e)
			{
				sendHtmlForm(activeChar);
			}
		}
		else if (command.startsWith("admin_server_restart"))
		{
			try
			{
				final String val = command.substring(21);
				if (Util.isDigit(val))
				{
					serverShutdown(activeChar, Integer.parseInt(val), true);
				}
				else
				{
					BuilderUtil.sendSysMessage(activeChar, "Usage: //server_restart <seconds>");
					sendHtmlForm(activeChar);
				}
			}
			catch (StringIndexOutOfBoundsException e)
			{
				sendHtmlForm(activeChar);
			}
		}
		else if (command.startsWith("admin_server_abort"))
		{
			serverAbort(activeChar);
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void sendHtmlForm(PlayerInstance activeChar)
	{
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(0, 1);
		final int t = GameTimeController.getInstance().getGameTime();
		final int h = t / 60;
		final int m = t % 60;
		final SimpleDateFormat format = new SimpleDateFormat("h:mm a");
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, h);
		cal.set(Calendar.MINUTE, m);
		adminReply.setFile(activeChar, "data/html/admin/shutdown.htm");
		adminReply.replace("%count%", String.valueOf(World.getInstance().getPlayers().size()));
		adminReply.replace("%used%", String.valueOf(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
		adminReply.replace("%time%", format.format(cal.getTime()));
		activeChar.sendPacket(adminReply);
	}
	
	private void serverShutdown(PlayerInstance activeChar, int seconds, boolean restart)
	{
		Shutdown.getInstance().startShutdown(activeChar, seconds, restart);
	}
	
	private void serverAbort(PlayerInstance activeChar)
	{
		Shutdown.getInstance().abort(activeChar);
	}
}
