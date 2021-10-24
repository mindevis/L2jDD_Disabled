
package handlers.admincommandhandlers;

import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.ControllableMobInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.taskmanager.DecayTaskManager;
import org.l2jdd.gameserver.util.BuilderUtil;

/**
 * This class handles following admin commands: - res = resurrects target Creature
 * @version $Revision: 1.2.4.5 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminRes implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_res",
		"admin_res_monster"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		if (command.startsWith("admin_res "))
		{
			handleRes(activeChar, command.split(" ")[1]);
		}
		else if (command.equals("admin_res"))
		{
			handleRes(activeChar);
		}
		else if (command.startsWith("admin_res_monster "))
		{
			handleNonPlayerRes(activeChar, command.split(" ")[1]);
		}
		else if (command.equals("admin_res_monster"))
		{
			handleNonPlayerRes(activeChar);
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void handleRes(PlayerInstance activeChar)
	{
		handleRes(activeChar, null);
	}
	
	private void handleRes(PlayerInstance activeChar, String resParam)
	{
		WorldObject obj = activeChar.getTarget();
		if (resParam != null)
		{
			// Check if a player name was specified as a param.
			final PlayerInstance plyr = World.getInstance().getPlayer(resParam);
			if (plyr != null)
			{
				obj = plyr;
			}
			else
			{
				// Otherwise, check if the param was a radius.
				try
				{
					final int radius = Integer.parseInt(resParam);
					World.getInstance().forEachVisibleObjectInRange(activeChar, PlayerInstance.class, radius, this::doResurrect);
					BuilderUtil.sendSysMessage(activeChar, "Resurrected all players within a " + radius + " unit radius.");
					return;
				}
				catch (NumberFormatException e)
				{
					BuilderUtil.sendSysMessage(activeChar, "Enter a valid player name or radius.");
					return;
				}
			}
		}
		
		if (obj == null)
		{
			obj = activeChar;
		}
		
		if (obj instanceof ControllableMobInstance)
		{
			activeChar.sendPacket(SystemMessageId.INVALID_TARGET);
			return;
		}
		
		doResurrect((Creature) obj);
	}
	
	private void handleNonPlayerRes(PlayerInstance activeChar)
	{
		handleNonPlayerRes(activeChar, "");
	}
	
	private void handleNonPlayerRes(PlayerInstance activeChar, String radiusStr)
	{
		final WorldObject obj = activeChar.getTarget();
		
		try
		{
			int radius = 0;
			if (!radiusStr.isEmpty())
			{
				radius = Integer.parseInt(radiusStr);
				World.getInstance().forEachVisibleObjectInRange(activeChar, Creature.class, radius, knownChar ->
				{
					if (!knownChar.isPlayer() && !(knownChar instanceof ControllableMobInstance))
					{
						doResurrect(knownChar);
					}
				});
				
				BuilderUtil.sendSysMessage(activeChar, "Resurrected all non-players within a " + radius + " unit radius.");
			}
		}
		catch (NumberFormatException e)
		{
			BuilderUtil.sendSysMessage(activeChar, "Enter a valid radius.");
			return;
		}
		
		if ((obj == null) || (obj.isPlayer()) || (obj instanceof ControllableMobInstance))
		{
			activeChar.sendPacket(SystemMessageId.INVALID_TARGET);
			return;
		}
		
		doResurrect((Creature) obj);
	}
	
	private void doResurrect(Creature targetChar)
	{
		if (!targetChar.isDead())
		{
			return;
		}
		
		// If the target is a player, then restore the XP lost on death.
		if (targetChar.isPlayer())
		{
			((PlayerInstance) targetChar).restoreExp(100.0);
		}
		else
		{
			DecayTaskManager.getInstance().cancel(targetChar);
		}
		
		targetChar.doRevive();
	}
}
