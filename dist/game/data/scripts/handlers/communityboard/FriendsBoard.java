
package handlers.communityboard;

import org.l2jdd.gameserver.cache.HtmCache;
import org.l2jdd.gameserver.handler.CommunityBoardHandler;
import org.l2jdd.gameserver.handler.IParseBoardHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * Friends board.
 * @author Zoey76
 */
public class FriendsBoard implements IParseBoardHandler
{
	private static final String[] COMMANDS =
	{
		"_friendlist",
		"_friendblocklist"
	};
	
	@Override
	public String[] getCommunityBoardCommands()
	{
		return COMMANDS;
	}
	
	@Override
	public boolean parseCommunityBoardCommand(String command, PlayerInstance player)
	{
		if (command.equals("_friendlist"))
		{
			CommunityBoardHandler.getInstance().addBypass(player, "Friends List", command);
			final String html = HtmCache.getInstance().getHtm(player, "data/html/CommunityBoard/friends_list.html");
			CommunityBoardHandler.separateAndSend(html, player);
		}
		else if (command.equals("_friendblocklist"))
		{
			CommunityBoardHandler.getInstance().addBypass(player, "Ignore list", command);
			final String html = HtmCache.getInstance().getHtm(player, "data/html/CommunityBoard/friends_block_list.html");
			CommunityBoardHandler.separateAndSend(html, player);
		}
		return true;
	}
}
