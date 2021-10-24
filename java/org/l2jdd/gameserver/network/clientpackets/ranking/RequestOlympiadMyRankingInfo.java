
package org.l2jdd.gameserver.network.clientpackets.ranking;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;
import org.l2jdd.gameserver.network.serverpackets.ranking.ExOlympiadMyRankingInfo;

/**
 * @author NviX
 */
public class RequestOlympiadMyRankingInfo implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		client.sendPacket(new ExOlympiadMyRankingInfo(client.getPlayer()));
	}
}
