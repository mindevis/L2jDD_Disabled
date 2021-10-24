
package handlers.usercommandhandlers;

import org.l2jdd.gameserver.handler.IVoicedCommandHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * @author xban1x
 */
public class ExperienceGain implements IVoicedCommandHandler
{
	private static final String[] COMMANDS = new String[]
	{
		"expoff",
		"expon",
	};
	
	@Override
	public boolean useVoicedCommand(String command, PlayerInstance player, String params)
	{
		if (command.equals("expoff"))
		{
			if (!player.getVariables().getBoolean("EXPOFF", false))
			{
				player.disableExpGain();
				player.getVariables().set("EXPOFF", true);
				player.sendMessage("Experience gain is disabled.");
			}
		}
		else if (command.equals("expon"))
		{
			if (player.getVariables().getBoolean("EXPOFF", false))
			{
				player.enableExpGain();
				player.getVariables().set("EXPOFF", false);
				player.sendMessage("Experience gain is enabled.");
			}
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return COMMANDS;
	}
}
