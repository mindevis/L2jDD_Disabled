
package handlers.communityboard;

import org.l2jdd.gameserver.cache.HtmCache;
import org.l2jdd.gameserver.handler.CommunityBoardHandler;
import org.l2jdd.gameserver.handler.IParseBoardHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * Homepage board.
 * @author Zoey76
 */
public class HomepageBoard implements IParseBoardHandler
{
	private static final String[] COMMANDS =
	{
		"_bbslink"
	};
	
	@Override
	public String[] getCommunityBoardCommands()
	{
		return COMMANDS;
	}
	
	@Override
	public boolean parseCommunityBoardCommand(String command, PlayerInstance player)
	{
		CommunityBoardHandler.separateAndSend(HtmCache.getInstance().getHtm(player, "data/html/CommunityBoard/homepage.html"), player);
		return true;
	}
}
