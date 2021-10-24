
package org.l2jdd.gameserver.network.serverpackets.pledgeV2;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.data.xml.ClanLevelData;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.AbstractItemPacket;

/**
 * @author Mobius
 */
public class ExPledgeShowInfoUpdate extends AbstractItemPacket
{
	final PlayerInstance _player;
	
	public ExPledgeShowInfoUpdate(PlayerInstance player)
	{
		_player = player;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		final Clan clan = _player.getClan();
		if (clan == null)
		{
			return false;
		}
		
		OutgoingPackets.EX_PLEDGE_SHOW_INFO_UPDATE.writeId(packet);
		packet.writeD(clan.getId()); // Clan ID
		packet.writeD(ClanLevelData.getLevelRequirement(clan.getLevel())); // Next level cost
		packet.writeD(ClanLevelData.getCommonMemberLimit(clan.getLevel())); // Max pledge members
		packet.writeD(ClanLevelData.getEliteMemberLimit(clan.getLevel())); // Max elite members
		return true;
	}
}
