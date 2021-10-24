
package handlers.bypasshandlers;

import org.l2jdd.gameserver.handler.IBypassHandler;
import org.l2jdd.gameserver.handler.IVoicedCommandHandler;
import org.l2jdd.gameserver.handler.VoicedCommandHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * @author DS
 */
public class VoiceCommand implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"voice"
	};
	
	@Override
	public boolean useBypass(String command, PlayerInstance player, Creature target)
	{
		// only voice commands allowed
		if ((command.length() > 7) && (command.charAt(6) == '.'))
		{
			final String vc;
			final String vparams;
			final int endOfCommand = command.indexOf(' ', 7);
			if (endOfCommand > 0)
			{
				vc = command.substring(7, endOfCommand).trim();
				vparams = command.substring(endOfCommand).trim();
			}
			else
			{
				vc = command.substring(7).trim();
				vparams = null;
			}
			
			if (vc.length() > 0)
			{
				final IVoicedCommandHandler vch = VoicedCommandHandler.getInstance().getHandler(vc);
				if (vch != null)
				{
					return vch.useVoicedCommand(vc, player, vparams);
				}
			}
		}
		
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
