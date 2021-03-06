
package org.l2jdd.gameserver.data.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.l2jdd.commons.util.IXmlReader;
import org.l2jdd.gameserver.enums.ClanHallGrade;
import org.l2jdd.gameserver.enums.ClanHallType;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.instance.DoorInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.holders.ClanHallTeleportHolder;
import org.l2jdd.gameserver.model.residences.ClanHall;

/**
 * @author St3eT
 */
public class ClanHallData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(ClanHallData.class.getName());
	private static final Map<Integer, ClanHall> _clanHalls = new ConcurrentHashMap<>();
	
	protected ClanHallData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseDatapackDirectory("data/residences/clanHalls", true);
		LOGGER.info(getClass().getSimpleName() + ": Succesfully loaded " + _clanHalls.size() + " clan halls.");
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		final List<DoorInstance> doors = new ArrayList<>();
		final List<Integer> npcs = new ArrayList<>();
		final List<ClanHallTeleportHolder> teleports = new ArrayList<>();
		final StatSet params = new StatSet();
		for (Node listNode = doc.getFirstChild(); listNode != null; listNode = listNode.getNextSibling())
		{
			if ("list".equals(listNode.getNodeName()))
			{
				for (Node clanHallNode = listNode.getFirstChild(); clanHallNode != null; clanHallNode = clanHallNode.getNextSibling())
				{
					if ("clanHall".equals(clanHallNode.getNodeName()))
					{
						params.set("id", parseInteger(clanHallNode.getAttributes(), "id"));
						params.set("name", parseString(clanHallNode.getAttributes(), "name", "None"));
						params.set("grade", parseEnum(clanHallNode.getAttributes(), ClanHallGrade.class, "grade", ClanHallGrade.GRADE_NONE));
						params.set("type", parseEnum(clanHallNode.getAttributes(), ClanHallType.class, "type", ClanHallType.OTHER));
						for (Node tpNode = clanHallNode.getFirstChild(); tpNode != null; tpNode = tpNode.getNextSibling())
						{
							switch (tpNode.getNodeName())
							{
								case "auction":
								{
									final NamedNodeMap at = tpNode.getAttributes();
									params.set("minBid", parseInteger(at, "minBid"));
									params.set("lease", parseInteger(at, "lease"));
									params.set("deposit", parseInteger(at, "deposit"));
									break;
								}
								case "npcs":
								{
									for (Node npcNode = tpNode.getFirstChild(); npcNode != null; npcNode = npcNode.getNextSibling())
									{
										if ("npc".equals(npcNode.getNodeName()))
										{
											final NamedNodeMap np = npcNode.getAttributes();
											final int npcId = parseInteger(np, "id");
											npcs.add(npcId);
										}
									}
									params.set("npcList", npcs);
									break;
								}
								case "doorlist":
								{
									for (Node npcNode = tpNode.getFirstChild(); npcNode != null; npcNode = npcNode.getNextSibling())
									{
										if ("door".equals(npcNode.getNodeName()))
										{
											final NamedNodeMap np = npcNode.getAttributes();
											final int doorId = parseInteger(np, "id");
											final DoorInstance door = DoorData.getInstance().getDoor(doorId);
											if (door != null)
											{
												doors.add(door);
											}
										}
									}
									params.set("doorList", doors);
									break;
								}
								case "teleportList":
								{
									for (Node npcNode = tpNode.getFirstChild(); npcNode != null; npcNode = npcNode.getNextSibling())
									{
										if ("teleport".equals(npcNode.getNodeName()))
										{
											final NamedNodeMap np = npcNode.getAttributes();
											final int npcStringId = parseInteger(np, "npcStringId");
											final int x = parseInteger(np, "x");
											final int y = parseInteger(np, "y");
											final int z = parseInteger(np, "z");
											final int minFunctionLevel = parseInteger(np, "minFunctionLevel");
											final int cost = parseInteger(np, "cost");
											teleports.add(new ClanHallTeleportHolder(npcStringId, x, y, z, minFunctionLevel, cost));
										}
									}
									params.set("teleportList", teleports);
									break;
								}
								case "ownerRestartPoint":
								{
									final NamedNodeMap ol = tpNode.getAttributes();
									params.set("owner_loc", new Location(parseInteger(ol, "x"), parseInteger(ol, "y"), parseInteger(ol, "z")));
									break;
								}
								case "banishPoint":
								{
									final NamedNodeMap bl = tpNode.getAttributes();
									params.set("banish_loc", new Location(parseInteger(bl, "x"), parseInteger(bl, "y"), parseInteger(bl, "z")));
									break;
								}
							}
						}
					}
				}
			}
		}
		_clanHalls.put(params.getInt("id"), new ClanHall(params));
	}
	
	public ClanHall getClanHallById(int clanHallId)
	{
		return _clanHalls.get(clanHallId);
	}
	
	public Collection<ClanHall> getClanHalls()
	{
		return _clanHalls.values();
	}
	
	public ClanHall getClanHallByNpcId(int npcId)
	{
		for (ClanHall ch : _clanHalls.values())
		{
			if (ch.getNpcs().contains(npcId))
			{
				return ch;
			}
		}
		return null;
	}
	
	public ClanHall getClanHallByClan(Clan clan)
	{
		for (ClanHall ch : _clanHalls.values())
		{
			if (ch.getOwner() == clan)
			{
				return ch;
			}
		}
		return null;
	}
	
	public ClanHall getClanHallByDoorId(int doorId)
	{
		final DoorInstance door = DoorData.getInstance().getDoor(doorId);
		for (ClanHall ch : _clanHalls.values())
		{
			final List<DoorInstance> doors = ch.getDoors();
			if ((doors != null) && doors.contains(door))
			{
				return ch;
			}
		}
		return null;
	}
	
	public List<ClanHall> getFreeAuctionableHall()
	{
		return _clanHalls.values().stream().filter(ch -> (ch.getType() == ClanHallType.AUCTIONABLE) && (ch.getOwner() == null)).sorted(Comparator.comparingInt(ClanHall::getResidenceId)).collect(Collectors.toList());
	}
	
	/**
	 * Gets the single instance of ClanHallData.
	 * @return single instance of ClanHallData
	 */
	public static ClanHallData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ClanHallData INSTANCE = new ClanHallData();
	}
}
