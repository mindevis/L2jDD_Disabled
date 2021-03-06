
package handlers.admincommandhandlers;

import org.l2jdd.gameserver.data.SpawnTable;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.instancemanager.DBSpawnManager;
import org.l2jdd.gameserver.model.Spawn;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.util.BuilderUtil;

/**
 * This class handles following admin commands: - delete = deletes target
 * @version $Revision: 1.2.2.1.2.4 $ $Date: 2005/04/11 10:05:56 $
 */
public class AdminDelete implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_delete"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		if (command.equals("admin_delete"))
		{
			handleDelete(activeChar);
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	// TODO: add possibility to delete any WorldObject (except PlayerInstance)
	private void handleDelete(PlayerInstance activeChar)
	{
		final WorldObject obj = activeChar.getTarget();
		if (obj instanceof Npc)
		{
			final Npc target = (Npc) obj;
			target.deleteMe();
			
			final Spawn spawn = target.getSpawn();
			if (spawn != null)
			{
				spawn.stopRespawn();
				
				if (DBSpawnManager.getInstance().isDefined(spawn.getId()))
				{
					DBSpawnManager.getInstance().deleteSpawn(spawn, true);
				}
				else
				{
					SpawnTable.getInstance().deleteSpawn(spawn, true);
				}
			}
			
			BuilderUtil.sendSysMessage(activeChar, "Deleted " + target.getName() + " from " + target.getObjectId() + ".");
		}
		else
		{
			BuilderUtil.sendSysMessage(activeChar, "Incorrect target.");
		}
	}
}
