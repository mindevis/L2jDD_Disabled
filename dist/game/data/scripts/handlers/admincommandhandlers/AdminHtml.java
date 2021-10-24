
package handlers.admincommandhandlers;

import java.io.File;
import java.util.StringTokenizer;

import org.l2jdd.Config;
import org.l2jdd.gameserver.cache.HtmCache;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jdd.gameserver.util.BuilderUtil;

/**
 * @author NosBit
 */
public class AdminHtml implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_html",
		"admin_loadhtml"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		final String actualCommand = st.nextToken();
		switch (actualCommand.toLowerCase())
		{
			case "admin_html":
			{
				if (!st.hasMoreTokens())
				{
					BuilderUtil.sendSysMessage(activeChar, "Usage: //html path");
					return false;
				}
				
				final String path = st.nextToken();
				showAdminHtml(activeChar, path);
				break;
			}
			case "admin_loadhtml":
			{
				if (!st.hasMoreTokens())
				{
					BuilderUtil.sendSysMessage(activeChar, "Usage: //loadhtml path");
					return false;
				}
				
				final String path = st.nextToken();
				showHtml(activeChar, path, true);
				break;
			}
		}
		return true;
	}
	
	/**
	 * Shows a html message to activeChar
	 * @param activeChar activeChar where html is shown
	 * @param path relative path from directory data/html/admin/ to html
	 */
	static void showAdminHtml(PlayerInstance activeChar, String path)
	{
		showHtml(activeChar, "data/html/admin/" + path, false);
	}
	
	/**
	 * Shows a html message to activeChar.
	 * @param activeChar activeChar where html message is shown.
	 * @param path relative path from Config.DATAPACK_ROOT to html.
	 * @param reload {@code true} will reload html and show it {@code false} will show it from cache.
	 */
	private static void showHtml(PlayerInstance activeChar, String path, boolean reload)
	{
		String content = null;
		if (!reload)
		{
			content = HtmCache.getInstance().getHtm(activeChar, path);
		}
		else
		{
			content = HtmCache.getInstance().loadFile(new File(Config.DATAPACK_ROOT, path));
		}
		final NpcHtmlMessage html = new NpcHtmlMessage(0, 1);
		if (content != null)
		{
			html.setHtml(content);
		}
		else
		{
			html.setHtml("<html><body>My text is missing:<br>" + path + "</body></html>");
		}
		activeChar.sendPacket(html);
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
