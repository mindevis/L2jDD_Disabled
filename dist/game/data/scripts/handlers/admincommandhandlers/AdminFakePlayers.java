
package handlers.admincommandhandlers;

import org.l2jdd.gameserver.data.xml.FakePlayerData;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.instancemanager.FakePlayerChatManager;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.util.BuilderUtil;

/**
 * @author Mobius
 */
public class AdminFakePlayers implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_fakechat"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		if (command.startsWith("admin_fakechat"))
		{
			final String[] words = command.substring(15).split(" ");
			if (words.length < 3)
			{
				BuilderUtil.sendSysMessage(activeChar, "Usage: //fakechat playername fpcname message");
				return false;
			}
			final PlayerInstance player = World.getInstance().getPlayer(words[0]);
			if (player == null)
			{
				BuilderUtil.sendSysMessage(activeChar, "Player not found.");
				return false;
			}
			final String fpcName = FakePlayerData.getInstance().getProperName(words[1]);
			if (fpcName == null)
			{
				BuilderUtil.sendSysMessage(activeChar, "Fake player not found.");
				return false;
			}
			String message = "";
			for (int i = 0; i < words.length; i++)
			{
				if (i < 2)
				{
					continue;
				}
				message += (words[i] + " ");
			}
			FakePlayerChatManager.getInstance().sendChat(player, fpcName, message);
			BuilderUtil.sendSysMessage(activeChar, "Your message has been sent.");
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
