
package handlers.admincommandhandlers;

import org.l2jdd.gameserver.data.xml.AdminData;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.util.BuilderUtil;

/**
 * This class handles following admin commands: - GM = turns GM mode off
 * @version $Revision: 1.2.4.4 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminGm implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_gm"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		if (command.equals("admin_gm") && activeChar.isGM())
		{
			AdminData.getInstance().deleteGm(activeChar);
			activeChar.setAccessLevel(0, true, false);
			BuilderUtil.sendSysMessage(activeChar, "You deactivated your GM access for this session, if you login again you will be GM again, in order to remove your access completely please shift yourself and set your accesslevel to 0.");
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
