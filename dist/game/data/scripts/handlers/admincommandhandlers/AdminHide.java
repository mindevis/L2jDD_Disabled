
package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.util.BuilderUtil;

/**
 * @author lord_rex
 */
public class AdminHide implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_hide"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance player)
	{
		final StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		
		try
		{
			final String param = st.nextToken();
			switch (param)
			{
				case "on":
				{
					if (!BuilderUtil.setHiding(player, true))
					{
						BuilderUtil.sendSysMessage(player, "Currently, you cannot be seen.");
						return true;
					}
					
					BuilderUtil.sendSysMessage(player, "Now, you cannot be seen.");
					return true;
				}
				case "off":
				{
					if (!BuilderUtil.setHiding(player, false))
					{
						BuilderUtil.sendSysMessage(player, "Currently, you can be seen.");
						return true;
					}
					
					BuilderUtil.sendSysMessage(player, "Now, you can be seen.");
					return true;
				}
				default:
				{
					BuilderUtil.sendSysMessage(player, "//hide [on|off]");
					return true;
				}
			}
		}
		catch (Exception e)
		{
			BuilderUtil.sendSysMessage(player, "//hide [on|off]");
			return true;
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
