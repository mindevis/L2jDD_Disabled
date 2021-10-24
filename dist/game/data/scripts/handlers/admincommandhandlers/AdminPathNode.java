
package handlers.admincommandhandlers;

import java.util.List;

import org.l2jdd.Config;
import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.util.BuilderUtil;

public class AdminPathNode implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_path_find"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		if (command.equals("admin_path_find"))
		{
			if (!Config.PATHFINDING)
			{
				BuilderUtil.sendSysMessage(activeChar, "PathFinding is disabled.");
				return true;
			}
			if (activeChar.getTarget() != null)
			{
				final List<Location> path = GeoEngine.getInstance().findPath(activeChar.getX(), activeChar.getY(), (short) activeChar.getZ(), activeChar.getTarget().getX(), activeChar.getTarget().getY(), (short) activeChar.getTarget().getZ(), activeChar.getInstanceWorld());
				if (path == null)
				{
					BuilderUtil.sendSysMessage(activeChar, "No Route!");
					return true;
				}
				for (Location a : path)
				{
					BuilderUtil.sendSysMessage(activeChar, "x:" + a.getX() + " y:" + a.getY() + " z:" + a.getZ());
				}
			}
			else
			{
				BuilderUtil.sendSysMessage(activeChar, "No Target!");
			}
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
