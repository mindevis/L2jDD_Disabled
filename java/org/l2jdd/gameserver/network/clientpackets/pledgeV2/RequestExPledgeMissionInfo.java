
package org.l2jdd.gameserver.network.clientpackets.pledgeV2;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;
import org.l2jdd.gameserver.network.serverpackets.pledgeV2.ExPledgeMissionInfo;
import org.l2jdd.gameserver.network.serverpackets.pledgeV2.ExPledgeMissionRewardCount;

/**
 * @author Bonux (bonuxq@gmail.com)
 * @date 29.09.2019
 **/
public class RequestExPledgeMissionInfo implements IClientIncomingPacket
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
		
		client.sendPacket(new ExPledgeMissionRewardCount(player));
		client.sendPacket(new ExPledgeMissionInfo(player));
	}
}
