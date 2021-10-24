
package org.l2jdd.gameserver.network.serverpackets;

import java.util.Collection;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.data.sql.ClanTable;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.clan.ClanInfo;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.clientpackets.RequestAllyInfo;

/**
 * Sent in response to {@link RequestAllyInfo}, if applicable.<br>
 * @author afk5min
 */
public class AllianceInfo implements IClientOutgoingPacket
{
	private final String _name;
	private final int _total;
	private final int _online;
	private final String _leaderC;
	private final String _leaderP;
	private final ClanInfo[] _allies;
	
	public AllianceInfo(int allianceId)
	{
		final Clan leader = ClanTable.getInstance().getClan(allianceId);
		_name = leader.getAllyName();
		_leaderC = leader.getName();
		_leaderP = leader.getLeaderName();
		
		final Collection<Clan> allies = ClanTable.getInstance().getClanAllies(allianceId);
		_allies = new ClanInfo[allies.size()];
		int idx = 0;
		int total = 0;
		int online = 0;
		for (Clan clan : allies)
		{
			final ClanInfo ci = new ClanInfo(clan);
			_allies[idx++] = ci;
			total += ci.getTotal();
			online += ci.getOnline();
		}
		
		_total = total;
		_online = online;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.ALLIANCE_INFO.writeId(packet);
		
		packet.writeS(_name);
		packet.writeD(_total);
		packet.writeD(_online);
		packet.writeS(_leaderC);
		packet.writeS(_leaderP);
		
		packet.writeD(_allies.length);
		for (ClanInfo aci : _allies)
		{
			packet.writeS(aci.getClan().getName());
			packet.writeD(0x00);
			packet.writeD(aci.getClan().getLevel());
			packet.writeS(aci.getClan().getLeaderName());
			packet.writeD(aci.getTotal());
			packet.writeD(aci.getOnline());
		}
		return true;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getTotal()
	{
		return _total;
	}
	
	public int getOnline()
	{
		return _online;
	}
	
	public String getLeaderC()
	{
		return _leaderC;
	}
	
	public String getLeaderP()
	{
		return _leaderP;
	}
	
	public ClanInfo[] getAllies()
	{
		return _allies;
	}
}