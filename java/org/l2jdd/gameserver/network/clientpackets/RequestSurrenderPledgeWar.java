
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.sql.ClanTable;
import org.l2jdd.gameserver.enums.ClanWarState;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.clan.ClanMember;
import org.l2jdd.gameserver.model.clan.ClanPrivilege;
import org.l2jdd.gameserver.model.clan.ClanWar;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

public class RequestSurrenderPledgeWar implements IClientIncomingPacket
{
	private String _pledgeName;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_pledgeName = packet.readS();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Clan myClan = player.getClan();
		if (myClan == null)
		{
			return;
		}
		
		for (ClanMember member : myClan.getMembers())
		{
			if ((member != null) && member.isOnline() && member.getPlayerInstance().isInCombat())
			{
				player.sendPacket(SystemMessageId.A_CEASE_FIRE_DURING_A_CLAN_WAR_CAN_NOT_BE_CALLED_WHILE_MEMBERS_OF_YOUR_CLAN_ARE_ENGAGED_IN_BATTLE);
				client.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
		}
		
		final Clan targetClan = ClanTable.getInstance().getClanByName(_pledgeName);
		if (targetClan == null)
		{
			player.sendMessage("No such clan.");
			client.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		else if (!player.hasClanPrivilege(ClanPrivilege.CL_PLEDGE_WAR))
		{
			client.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			client.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final ClanWar clanWar = myClan.getWarWith(targetClan.getId());
		if (clanWar == null)
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_HAVE_NOT_DECLARED_A_CLAN_WAR_AGAINST_THE_CLAN_S1);
			sm.addString(targetClan.getName());
			player.sendPacket(sm);
			client.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (clanWar.getState() == ClanWarState.BLOOD_DECLARATION)
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_DECLARE_DEFEAT_AS_IT_HAS_NOT_BEEN_7_DAYS_SINCE_STARTING_A_CLAN_WAR_WITH_CLAN_S1);
			client.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		clanWar.cancel(player, myClan);
	}
}