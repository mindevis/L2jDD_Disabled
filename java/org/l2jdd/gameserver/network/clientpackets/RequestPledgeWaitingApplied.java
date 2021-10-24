
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.instancemanager.ClanEntryManager;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.serverpackets.ExPledgeWaitingListApplied;

/**
 * @author Sdw
 */
public class RequestPledgeWaitingApplied implements IClientIncomingPacket
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
		if ((player == null) || (player.getClan() != null))
		{
			return;
		}
		
		final int clanId = ClanEntryManager.getInstance().getClanIdForPlayerApplication(player.getObjectId());
		if (clanId > 0)
		{
			player.sendPacket(new ExPledgeWaitingListApplied(clanId, player.getObjectId()));
		}
	}
}
