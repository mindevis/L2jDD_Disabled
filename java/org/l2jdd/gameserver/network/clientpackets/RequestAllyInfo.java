
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.ClanInfo;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.AllianceInfo;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @version $Revision: 1479 $ $Date: 2005-11-09 00:47:42 +0100 (mer., 09 nov. 2005) $
 */
public class RequestAllyInfo implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
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
		
		SystemMessage sm;
		final int allianceId = player.getAllyId();
		if (allianceId > 0)
		{
			final AllianceInfo ai = new AllianceInfo(allianceId);
			client.sendPacket(ai);
			
			// send for player
			sm = new SystemMessage(SystemMessageId.ALLIANCE_INFORMATION);
			client.sendPacket(sm);
			
			sm = new SystemMessage(SystemMessageId.ALLIANCE_NAME_S1);
			sm.addString(ai.getName());
			client.sendPacket(sm);
			
			sm = new SystemMessage(SystemMessageId.ALLIANCE_LEADER_S2_OF_S1);
			sm.addString(ai.getLeaderC());
			sm.addString(ai.getLeaderP());
			client.sendPacket(sm);
			
			sm = new SystemMessage(SystemMessageId.CONNECTION_S1_TOTAL_S2);
			sm.addInt(ai.getOnline());
			sm.addInt(ai.getTotal());
			client.sendPacket(sm);
			
			sm = new SystemMessage(SystemMessageId.AFFILIATED_CLANS_TOTAL_S1_CLAN_S);
			sm.addInt(ai.getAllies().length);
			client.sendPacket(sm);
			
			sm = new SystemMessage(SystemMessageId.CLAN_INFORMATION);
			for (ClanInfo aci : ai.getAllies())
			{
				client.sendPacket(sm);
				
				sm = new SystemMessage(SystemMessageId.CLAN_NAME_S1);
				sm.addString(aci.getClan().getName());
				client.sendPacket(sm);
				
				sm = new SystemMessage(SystemMessageId.CLAN_LEADER_S1);
				sm.addString(aci.getClan().getLeaderName());
				client.sendPacket(sm);
				
				sm = new SystemMessage(SystemMessageId.CLAN_LEVEL_S1);
				sm.addInt(aci.getClan().getLevel());
				client.sendPacket(sm);
				
				sm = new SystemMessage(SystemMessageId.CONNECTION_S1_TOTAL_S2);
				sm.addInt(aci.getOnline());
				sm.addInt(aci.getTotal());
				client.sendPacket(sm);
				
				sm = new SystemMessage(SystemMessageId.EMPTY_4);
			}
			
			sm = new SystemMessage(SystemMessageId.EMPTY_5);
			client.sendPacket(sm);
		}
		else
		{
			client.sendPacket(SystemMessageId.YOU_ARE_NOT_CURRENTLY_ALLIED_WITH_ANY_CLANS);
		}
	}
}
