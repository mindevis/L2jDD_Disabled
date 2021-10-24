
package handlers.bypasshandlers;

import java.util.logging.Level;

import org.l2jdd.gameserver.handler.IBypassHandler;
import org.l2jdd.gameserver.instancemanager.events.GameEvent;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

public class EventEngine implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"event_participate",
		"event_unregister"
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
			if (command.equalsIgnoreCase("event_participate"))
			{
				GameEvent.registerPlayer(player);
				return true;
			}
			else if (command.equalsIgnoreCase("event_unregister"))
			{
				GameEvent.removeAndResetPlayer(player);
				return true;
			}
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
