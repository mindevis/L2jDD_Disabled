
package handlers.bypasshandlers;

import java.util.logging.Level;

import org.l2jdd.gameserver.data.xml.MultisellData;
import org.l2jdd.gameserver.handler.IBypassHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

public class Multisell implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"multisell",
		"exc_multisell"
	};
	
	@Override
	public boolean useBypass(String command, PlayerInstance player, Creature target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		try
		{
			int listId;
			if (command.toLowerCase().startsWith(COMMANDS[0])) // multisell
			{
				listId = Integer.parseInt(command.substring(9).trim());
				MultisellData.getInstance().separateAndSend(listId, player, (Npc) target, false);
				return true;
			}
			else if (command.toLowerCase().startsWith(COMMANDS[1])) // exc_multisell
			{
				listId = Integer.parseInt(command.substring(13).trim());
				MultisellData.getInstance().separateAndSend(listId, player, (Npc) target, true);
				return true;
			}
			return false;
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
