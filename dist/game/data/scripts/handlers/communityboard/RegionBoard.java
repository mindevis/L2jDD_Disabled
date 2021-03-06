
package handlers.communityboard;

import org.l2jdd.gameserver.cache.HtmCache;
import org.l2jdd.gameserver.data.sql.ClanTable;
import org.l2jdd.gameserver.enums.TaxType;
import org.l2jdd.gameserver.handler.CommunityBoardHandler;
import org.l2jdd.gameserver.handler.IWriteBoardHandler;
import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.util.Util;

/**
 * Region board.
 * @author Zoey76
 */
public class RegionBoard implements IWriteBoardHandler
{
	// Region data
	// @formatter:off
	private static final int[] REGIONS = { 1049, 1052, 1053, 1057, 1060, 1059, 1248, 1247, 1056 };
	// @formatter:on
	private static final String[] COMMANDS =
	{
		"_bbsloc"
	};
	
	@Override
	public String[] getCommunityBoardCommands()
	{
		return COMMANDS;
	}
	
	@Override
	public boolean parseCommunityBoardCommand(String command, PlayerInstance player)
	{
		if (command.equals("_bbsloc"))
		{
			CommunityBoardHandler.getInstance().addBypass(player, "Region", command);
			
			final String list = HtmCache.getInstance().getHtm(player, "data/html/CommunityBoard/region_list.html");
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < REGIONS.length; i++)
			{
				final Castle castle = CastleManager.getInstance().getCastleById(i + 1);
				final Clan clan = ClanTable.getInstance().getClan(castle.getOwnerId());
				String link = list.replace("%region_id%", String.valueOf(i));
				link = link.replace("%region_name%", String.valueOf(REGIONS[i]));
				link = link.replace("%region_owning_clan%", (clan != null ? clan.getName() : "NPC"));
				link = link.replace("%region_owning_clan_alliance%", ((clan != null) && (clan.getAllyName() != null) ? clan.getAllyName() : ""));
				link = link.replace("%region_tax_rate%", castle.getTaxPercent(TaxType.BUY) + "%");
				sb.append(link);
			}
			
			String html = HtmCache.getInstance().getHtm(player, "data/html/CommunityBoard/region.html");
			html = html.replace("%region_list%", sb.toString());
			CommunityBoardHandler.separateAndSend(html, player);
		}
		else if (command.startsWith("_bbsloc;"))
		{
			CommunityBoardHandler.getInstance().addBypass(player, "Region>", command);
			
			final String id = command.replace("_bbsloc;", "");
			if (!Util.isDigit(id))
			{
				LOG.warning(RegionBoard.class.getSimpleName() + ": Player " + player + " sent and invalid region bypass " + command + "!");
				return false;
			}
			
			// TODO: Implement.
		}
		return true;
	}
	
	@Override
	public boolean writeCommunityBoardCommand(PlayerInstance player, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
		// TODO: Implement.
		return false;
	}
}
