
package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.Disconnection;
import org.l2jdd.gameserver.util.BuilderUtil;

public class AdminKick implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_kick",
		"admin_kick_non_gm"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		if (command.startsWith("admin_kick"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			if (st.countTokens() > 1)
			{
				st.nextToken();
				final String player = st.nextToken();
				final PlayerInstance plyr = World.getInstance().getPlayer(player);
				if (plyr != null)
				{
					Disconnection.of(plyr).defaultSequence(false);
					BuilderUtil.sendSysMessage(activeChar, "You kicked " + plyr.getName() + " from the game.");
				}
			}
		}
		if (command.startsWith("admin_kick_non_gm"))
		{
			int counter = 0;
			for (PlayerInstance player : World.getInstance().getPlayers())
			{
				if (!player.isGM())
				{
					counter++;
					Disconnection.of(player).defaultSequence(false);
				}
			}
			BuilderUtil.sendSysMessage(activeChar, "Kicked " + counter + " players.");
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
