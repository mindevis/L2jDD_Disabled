
package handlers.communityboard;

import org.l2jdd.gameserver.cache.HtmCache;
import org.l2jdd.gameserver.handler.CommunityBoardHandler;
import org.l2jdd.gameserver.handler.IWriteBoardHandler;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * Memo board.
 * @author Zoey76
 */
public class MemoBoard implements IWriteBoardHandler
{
	private static final String[] COMMANDS =
	{
		"_bbsmemo",
		"_bbstopics"
	};
	
	@Override
	public String[] getCommunityBoardCommands()
	{
		return COMMANDS;
	}
	
	@Override
	public boolean parseCommunityBoardCommand(String command, PlayerInstance player)
	{
		CommunityBoardHandler.getInstance().addBypass(player, "Memo Command", command);
		
		final String html = HtmCache.getInstance().getHtm(player, "data/html/CommunityBoard/memo.html");
		CommunityBoardHandler.separateAndSend(html, player);
		return true;
	}
	
	@Override
	public boolean writeCommunityBoardCommand(PlayerInstance player, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
		// TODO: Implement.
		return false;
	}
}
