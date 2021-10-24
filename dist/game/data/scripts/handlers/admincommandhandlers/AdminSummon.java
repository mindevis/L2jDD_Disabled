
package handlers.admincommandhandlers;

import org.l2jdd.gameserver.handler.AdminCommandHandler;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.util.BuilderUtil;

/**
 * @author poltomb
 */
public class AdminSummon implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_summon"
	};
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		int id;
		int count = 1;
		final String[] data = command.split(" ");
		try
		{
			id = Integer.parseInt(data[1]);
			if (data.length > 2)
			{
				count = Integer.parseInt(data[2]);
			}
		}
		catch (NumberFormatException nfe)
		{
			BuilderUtil.sendSysMessage(activeChar, "Incorrect format for command 'summon'");
			return false;
		}
		
		final String subCommand;
		if (id < 1000000)
		{
			subCommand = "admin_create_item";
		}
		else
		{
			subCommand = "admin_spawn_once";
			BuilderUtil.sendSysMessage(activeChar, "This is only a temporary spawn.  The mob(s) will NOT respawn.");
			id -= 1000000;
		}
		
		if ((id > 0) && (count > 0))
		{
			AdminCommandHandler.getInstance().useAdminCommand(activeChar, subCommand + " " + id + " " + count, true);
		}
		
		return true;
	}
}