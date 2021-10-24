
package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.PlayerCondOverride;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jdd.gameserver.util.BuilderUtil;
import org.l2jdd.gameserver.util.Util;

/**
 * Handler provides ability to override server's conditions for admin.<br>
 * Note: //setparam command uses any XML value and ignores case sensitivity.<br>
 * For best results by //setparam enable the maximum stats PcCondOverride here.
 * @author UnAfraid, Nik
 */
public class AdminPcCondOverride implements IAdminCommandHandler
{
	// private static final int SETPARAM_ORDER = 0x90;
	
	private static final String[] COMMANDS =
	{
		"admin_exceptions",
		"admin_set_exception",
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		final StringTokenizer st = new StringTokenizer(command);
		if (st.hasMoreTokens())
		{
			switch (st.nextToken())
			// command
			{
				case "admin_exceptions":
				{
					final NpcHtmlMessage msg = new NpcHtmlMessage(0, 1);
					msg.setFile(activeChar, "data/html/admin/cond_override.htm");
					final StringBuilder sb = new StringBuilder();
					for (PlayerCondOverride ex : PlayerCondOverride.values())
					{
						sb.append("<tr><td fixwidth=\"180\">" + ex.getDescription() + ":</td><td><a action=\"bypass -h admin_set_exception " + ex.ordinal() + "\">" + (activeChar.canOverrideCond(ex) ? "Disable" : "Enable") + "</a></td></tr>");
					}
					msg.replace("%cond_table%", sb.toString());
					activeChar.sendPacket(msg);
					break;
				}
				case "admin_set_exception":
				{
					if (st.hasMoreTokens())
					{
						final String token = st.nextToken();
						if (Util.isDigit(token))
						{
							final PlayerCondOverride ex = PlayerCondOverride.getCondOverride(Integer.parseInt(token));
							if (ex != null)
							{
								if (activeChar.canOverrideCond(ex))
								{
									activeChar.removeOverridedCond(ex);
									BuilderUtil.sendSysMessage(activeChar, "You've disabled " + ex.getDescription());
								}
								else
								{
									activeChar.addOverrideCond(ex);
									BuilderUtil.sendSysMessage(activeChar, "You've enabled " + ex.getDescription());
								}
							}
						}
						else
						{
							switch (token)
							{
								case "enable_all":
								{
									for (PlayerCondOverride ex : PlayerCondOverride.values())
									{
										if (!activeChar.canOverrideCond(ex))
										{
											activeChar.addOverrideCond(ex);
										}
									}
									BuilderUtil.sendSysMessage(activeChar, "All condition exceptions have been enabled.");
									break;
								}
								case "disable_all":
								{
									for (PlayerCondOverride ex : PlayerCondOverride.values())
									{
										if (activeChar.canOverrideCond(ex))
										{
											activeChar.removeOverridedCond(ex);
										}
									}
									BuilderUtil.sendSysMessage(activeChar, "All condition exceptions have been disabled.");
									break;
								}
							}
						}
						useAdminCommand(COMMANDS[0], activeChar);
					}
					break;
				}
			}
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return COMMANDS;
	}
}
