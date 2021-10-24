
package org.l2jdd.gameserver.network.clientpackets.ranking;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;
import org.l2jdd.gameserver.network.serverpackets.ranking.ExOlympiadRankingInfo;

/**
 * @author NviX
 */
public class RequestOlympiadRankingInfo implements IClientIncomingPacket
{
	private int _tabId;
	private int _rankingType;
	private int _unk;
	private int _classId;
	private int _serverId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_tabId = packet.readC();
		_rankingType = packet.readC();
		_unk = packet.readC();
		_classId = packet.readD();
		_serverId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		client.sendPacket(new ExOlympiadRankingInfo(client.getPlayer(), _tabId, _rankingType, _unk, _classId, _serverId));
	}
}
