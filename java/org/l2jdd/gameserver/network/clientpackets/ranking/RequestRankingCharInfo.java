
package org.l2jdd.gameserver.network.clientpackets.ranking;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;
import org.l2jdd.gameserver.network.serverpackets.ranking.ExRankingCharInfo;

/**
 * @author JoeAlisson
 */
public class RequestRankingCharInfo implements IClientIncomingPacket
{
	private short _unk;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_unk = packet.readC();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		client.sendPacket(new ExRankingCharInfo(client.getPlayer(), _unk));
	}
}
