
package handlers.admincommandhandlers;

import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;
import org.l2jdd.gameserver.util.BuilderUtil;
import org.l2jdd.gameserver.util.Util;

/**
 * Allows Game Masters to test System Messages.<br>
 * admin_msg display the raw message.<br>
 * admin_msgx is an extended version that allows to set parameters.
 * @author Zoey76
 */
public class AdminMessages implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_msg",
		"admin_msgx"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		if (command.startsWith("admin_msg "))
		{
			try
			{
				activeChar.sendPacket(new SystemMessage(Integer.parseInt(command.substring(10).trim())));
				return true;
			}
			catch (Exception e)
			{
				BuilderUtil.sendSysMessage(activeChar, "Command format: //msg <SYSTEM_MSG_ID>");
			}
		}
		else if (command.startsWith("admin_msgx "))
		{
			final String[] tokens = command.split(" ");
			if ((tokens.length <= 2) || !Util.isDigit(tokens[1]))
			{
				BuilderUtil.sendSysMessage(activeChar, "Command format: //msgx <SYSTEM_MSG_ID> [item:Id] [skill:Id] [npc:Id] [zone:x,y,x] [castle:Id] [str:'text']");
				return false;
			}
			
			final SystemMessage sm = new SystemMessage(Integer.parseInt(tokens[1]));
			String val;
			int lastPos = 0;
			for (int i = 2; i < tokens.length; i++)
			{
				try
				{
					val = tokens[i];
					if (val.startsWith("item:"))
					{
						sm.addItemName(Integer.parseInt(val.substring(5)));
					}
					else if (val.startsWith("skill:"))
					{
						sm.addSkillName(Integer.parseInt(val.substring(6)));
					}
					else if (val.startsWith("npc:"))
					{
						sm.addNpcName(Integer.parseInt(val.substring(4)));
					}
					else if (val.startsWith("zone:"))
					{
						final int x = Integer.parseInt(val.substring(5, val.indexOf(',')));
						final int y = Integer.parseInt(val.substring(val.indexOf(',') + 1, val.lastIndexOf(',')));
						final int z = Integer.parseInt(val.substring(val.lastIndexOf(',') + 1, val.length()));
						sm.addZoneName(x, y, z);
					}
					else if (val.startsWith("castle:"))
					{
						sm.addCastleId(Integer.parseInt(val.substring(7)));
					}
					else if (val.startsWith("str:"))
					{
						final int pos = command.indexOf("'", lastPos + 1);
						lastPos = command.indexOf("'", pos + 1);
						sm.addString(command.substring(pos + 1, lastPos));
					}
				}
				catch (Exception e)
				{
					BuilderUtil.sendSysMessage(activeChar, "Exception: " + e.getMessage());
				}
			}
			activeChar.sendPacket(sm);
		}
		return false;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}