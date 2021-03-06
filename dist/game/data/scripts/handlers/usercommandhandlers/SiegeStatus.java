
package handlers.usercommandhandlers;

import org.l2jdd.gameserver.handler.IUserCommandHandler;
import org.l2jdd.gameserver.instancemanager.SiegeManager;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.siege.Siege;
import org.l2jdd.gameserver.model.zone.type.SiegeZone;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Tryskell
 */
public class SiegeStatus implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		99
	};
	
	private static final String INSIDE_SIEGE_ZONE = "Castle Siege in Progress";
	private static final String OUTSIDE_SIEGE_ZONE = "No Castle Siege Area";
	
	@Override
	public boolean useUserCommand(int id, PlayerInstance player)
	{
		if (id != COMMAND_IDS[0])
		{
			return false;
		}
		
		if ((player.getNobleLevel() == 0) || !player.isClanLeader())
		{
			player.sendPacket(SystemMessageId.ONLY_A_CLAN_LEADER_THAT_IS_A_NOBLESSE_EXALTED_CAN_VIEW_THE_SIEGE_WAR_STATUS_WINDOW_DURING_A_SIEGE_WAR);
			return false;
		}
		
		for (Siege siege : SiegeManager.getInstance().getSieges())
		{
			if (!siege.isInProgress())
			{
				continue;
			}
			
			final Clan clan = player.getClan();
			if (!siege.checkIsAttacker(clan) && !siege.checkIsDefender(clan))
			{
				continue;
			}
			
			final SiegeZone siegeZone = siege.getCastle().getZone();
			final StringBuilder sb = new StringBuilder();
			for (PlayerInstance member : clan.getOnlineMembers(0))
			{
				sb.append("<tr><td width=170>");
				sb.append(member.getName());
				sb.append("</td><td width=100>");
				sb.append(siegeZone.isInsideZone(member) ? INSIDE_SIEGE_ZONE : OUTSIDE_SIEGE_ZONE);
				sb.append("</td></tr>");
			}
			
			final NpcHtmlMessage html = new NpcHtmlMessage();
			html.setFile(player, "data/html/siege/siege_status.htm");
			html.replace("%kill_count%", clan.getSiegeKills());
			html.replace("%death_count%", clan.getSiegeDeaths());
			html.replace("%member_list%", sb.toString());
			player.sendPacket(html);
			
			return true;
		}
		
		player.sendPacket(SystemMessageId.ONLY_A_CLAN_LEADER_THAT_IS_A_NOBLESSE_EXALTED_CAN_VIEW_THE_SIEGE_WAR_STATUS_WINDOW_DURING_A_SIEGE_WAR);
		
		return false;
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}
