
package org.l2jdd.gameserver.network.serverpackets;

import java.util.Collection;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.data.sql.ClanTable;
import org.l2jdd.gameserver.data.xml.ClanHallData;
import org.l2jdd.gameserver.model.residences.ClanHall;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author KenM
 */
public class ExShowAgitInfo implements IClientOutgoingPacket
{
	public static final ExShowAgitInfo STATIC_PACKET = new ExShowAgitInfo();
	
	private ExShowAgitInfo()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SHOW_AGIT_INFO.writeId(packet);
		
		final Collection<ClanHall> clanHalls = ClanHallData.getInstance().getClanHalls();
		packet.writeD(clanHalls.size());
		clanHalls.forEach(clanHall ->
		{
			packet.writeD(clanHall.getResidenceId());
			packet.writeS(clanHall.getOwnerId() <= 0 ? "" : ClanTable.getInstance().getClan(clanHall.getOwnerId()).getName()); // owner clan name
			packet.writeS(clanHall.getOwnerId() <= 0 ? "" : ClanTable.getInstance().getClan(clanHall.getOwnerId()).getLeaderName()); // leader name
			packet.writeD(clanHall.getType().getClientVal()); // Clan hall type
		});
		return true;
	}
}
