
package org.l2jdd.gameserver.network.serverpackets.ranking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.l2jdd.Config;
import org.l2jdd.commons.database.DatabaseFactory;
import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.data.sql.ClanTable;
import org.l2jdd.gameserver.instancemanager.RankManager;
import org.l2jdd.gameserver.model.olympiad.Hero;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author NviX
 */
public class ExOlympiadHeroAndLegendInfo implements IClientOutgoingPacket
{
	// TODO: Move query and store data at RankManager.
	private static final String GET_HEROES = "SELECT characters.charId, characters.char_name, characters.race, characters.sex, characters.base_class, characters.level, characters.clanid, olympiad_nobles_eom.competitions_won, olympiad_nobles_eom.competitions_lost, olympiad_nobles_eom.olympiad_points, heroes.legend_count, heroes.count FROM heroes, characters, olympiad_nobles_eom WHERE characters.charId = heroes.charId AND characters.charId = olympiad_nobles_eom.charId ORDER BY olympiad_nobles_eom.olympiad_points DESC, characters.base_class ASC LIMIT " + RankManager.PLAYER_LIMIT;
	
	public ExOlympiadHeroAndLegendInfo()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_OLYMPIAD_HERO_AND_LEGEND_INFO.writeId(packet);
		
		if (Hero.getInstance().getHeroes().size() > 0)
		{
			try (Connection con = DatabaseFactory.getConnection();
				PreparedStatement statement = con.prepareStatement(GET_HEROES))
			{
				try (ResultSet rset = statement.executeQuery())
				{
					int i = 1;
					final boolean writedCount = false;
					while (rset.next())
					{
						if (i == 1)
						{
							packet.writeC(1); // ?? shows 78 on JP
							packet.writeC(1); // ?? shows 0 on JP
							
							packet.writeString(rset.getString("char_name"));
							final int clanId = rset.getInt("clanid");
							if (clanId > 0)
							{
								packet.writeString(ClanTable.getInstance().getClan(clanId).getName());
							}
							else
							{
								packet.writeString("");
							}
							packet.writeD(Config.SERVER_ID);
							packet.writeD(rset.getInt("race"));
							// a stupid, client uses 0 for female and 1 for male, while server no.
							final int sex = rset.getInt("sex");
							if (sex == 1)
							{
								packet.writeD(0);
							}
							else
							{
								packet.writeD(1);
							}
							packet.writeD(rset.getInt("base_class"));
							packet.writeD(rset.getInt("level"));
							packet.writeD(rset.getInt("legend_count"));
							packet.writeD(rset.getInt("competitions_won"));
							packet.writeD(rset.getInt("competitions_lost"));
							packet.writeD(rset.getInt("olympiad_points"));
							if (clanId > 0)
							{
								packet.writeD(ClanTable.getInstance().getClan(clanId).getLevel());
							}
							else
							{
								packet.writeD(0);
							}
							i++;
						}
						else
						{
							if (!writedCount)
							{
								packet.writeD(Hero.getInstance().getHeroes().size() - 1);
							}
							if (Hero.getInstance().getHeroes().size() > 1)
							{
								packet.writeString(rset.getString("char_name"));
								final int clanId = rset.getInt("clanid");
								if (clanId > 0)
								{
									packet.writeString(ClanTable.getInstance().getClan(clanId).getName());
								}
								else
								{
									packet.writeString("");
								}
								packet.writeD(Config.SERVER_ID);
								packet.writeD(rset.getInt("race"));
								// a stupid, client uses 0 for female and 1 for male, while server no.
								final int sex = rset.getInt("sex");
								if (sex == 1)
								{
									packet.writeD(0);
								}
								else
								{
									packet.writeD(1);
								}
								packet.writeD(rset.getInt("base_class"));
								packet.writeD(rset.getInt("level"));
								packet.writeD(rset.getInt("count"));
								packet.writeD(rset.getInt("competitions_won"));
								packet.writeD(rset.getInt("competitions_lost"));
								packet.writeD(rset.getInt("olympiad_points"));
								if (clanId > 0)
								{
									packet.writeD(ClanTable.getInstance().getClan(clanId).getLevel());
								}
								else
								{
									packet.writeD(0);
								}
							}
						}
					}
				}
			}
			catch (SQLException e)
			{
				LOGGER.warning("Hero and Legend Info: Couldnt load data: " + e.getMessage());
			}
		}
		return true;
	}
}