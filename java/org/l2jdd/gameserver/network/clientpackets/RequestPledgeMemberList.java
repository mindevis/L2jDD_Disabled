
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.serverpackets.PledgeShowMemberListAll;

/**
 * @version $Revision: 1.5.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestPledgeMemberList implements IClientIncomingPacket
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
		
		final Clan clan = player.getClan();
		if (clan != null)
		{
			PledgeShowMemberListAll.sendAllTo(player);
		}
	}
}
