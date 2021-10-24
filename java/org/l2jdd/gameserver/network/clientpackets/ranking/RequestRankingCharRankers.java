
package org.l2jdd.gameserver.network.clientpackets.ranking;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;
import org.l2jdd.gameserver.network.serverpackets.ranking.ExRankingCharRankers;

/**
 * @author JoeAlisson
 */
public class RequestRankingCharRankers implements IClientIncomingPacket
{
	private int _group;
	private int _scope;
	private int _race;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_group = packet.readC(); // Tab Id
		_scope = packet.readC(); // All or personal
		_race = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		client.sendPacket(new ExRankingCharRankers(client.getPlayer(), _group, _scope, _race));
	}
}
