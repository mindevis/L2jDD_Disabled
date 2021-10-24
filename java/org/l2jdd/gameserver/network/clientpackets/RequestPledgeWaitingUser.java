
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.instancemanager.ClanEntryManager;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.entry.PledgeApplicantInfo;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.serverpackets.ExPledgeWaitingList;
import org.l2jdd.gameserver.network.serverpackets.ExPledgeWaitingUser;

/**
 * @author Sdw
 */
public class RequestPledgeWaitingUser implements IClientIncomingPacket
{
	private int _clanId;
	private int _playerId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_clanId = packet.readD();
		_playerId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if ((player == null) || (player.getClanId() != _clanId))
		{
			return;
		}
		
		final PledgeApplicantInfo infos = ClanEntryManager.getInstance().getPlayerApplication(_clanId, _playerId);
		if (infos == null)
		{
			client.sendPacket(new ExPledgeWaitingList(_clanId));
		}
		else
		{
			client.sendPacket(new ExPledgeWaitingUser(infos));
		}
	}
}