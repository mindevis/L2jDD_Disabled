
package handlers.bypasshandlers;

import org.l2jdd.gameserver.handler.IBypassHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.ensoul.ExShowEnsoulExtractionWindow;
import org.l2jdd.gameserver.network.serverpackets.ensoul.ExShowEnsoulWindow;

/**
 * @author St3eT
 */
public class EnsoulWindow implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"show_ensoul_window",
		"show_extract_ensoul_window"
	};
	
	@Override
	public boolean useBypass(String command, PlayerInstance player, Creature target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		if (command.toLowerCase().startsWith(COMMANDS[0])) // show_ensoul_window
		{
			player.sendPacket(ExShowEnsoulWindow.STATIC_PACKET);
			return true;
		}
		else if (command.toLowerCase().startsWith(COMMANDS[1])) // show_extract_ensoul_window
		{
			player.sendPacket(ExShowEnsoulExtractionWindow.STATIC_PACKET);
			return true;
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
