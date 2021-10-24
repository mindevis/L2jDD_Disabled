
package org.l2jdd.gameserver.data.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.l2jdd.commons.util.IXmlReader;
import org.l2jdd.gameserver.enums.CastleSide;
import org.l2jdd.gameserver.enums.SiegeGuardType;
import org.l2jdd.gameserver.model.holders.CastleSpawnHolder;
import org.l2jdd.gameserver.model.holders.SiegeGuardHolder;

/**
 * @author St3eT
 */
public class CastleData implements IXmlReader
{
	private final Map<Integer, List<CastleSpawnHolder>> _spawns = new ConcurrentHashMap<>();
	private static final Map<Integer, List<SiegeGuardHolder>> _siegeGuards = new ConcurrentHashMap<>();
	
	protected CastleData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_spawns.clear();
		_siegeGuards.clear();
		parseDatapackDirectory("data/residences/castles", true);
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		for (Node listNode = doc.getFirstChild(); listNode != null; listNode = listNode.getNextSibling())
		{
			if ("list".equals(listNode.getNodeName()))
			{
				for (Node castleNode = listNode.getFirstChild(); castleNode != null; castleNode = castleNode.getNextSibling())
				{
					if ("castle".equals(castleNode.getNodeName()))
					{
						final int castleId = parseInteger(castleNode.getAttributes(), "id");
						for (Node tpNode = castleNode.getFirstChild(); tpNode != null; tpNode = tpNode.getNextSibling())
						{
							final List<CastleSpawnHolder> spawns = new ArrayList<>();
							if ("spawns".equals(tpNode.getNodeName()))
							{
								for (Node npcNode = tpNode.getFirstChild(); npcNode != null; npcNode = npcNode.getNextSibling())
								{
									if ("npc".equals(npcNode.getNodeName()))
									{
										final NamedNodeMap np = npcNode.getAttributes();
										final int npcId = parseInteger(np, "id");
										final CastleSide side = parseEnum(np, CastleSide.class, "castleSide", CastleSide.NEUTRAL);
										final int x = parseInteger(np, "x");
										final int y = parseInteger(np, "y");
										final int z = parseInteger(np, "z");
										final int heading = parseInteger(np, "heading");
										spawns.add(new CastleSpawnHolder(npcId, side, x, y, z, heading));
									}
								}
								_spawns.put(castleId, spawns);
							}
							else if ("siegeGuards".equals(tpNode.getNodeName()))
							{
								final List<SiegeGuardHolder> guards = new ArrayList<>();
								for (Node npcNode = tpNode.getFirstChild(); npcNode != null; npcNode = npcNode.getNextSibling())
								{
									if ("guard".equals(npcNode.getNodeName()))
									{
										final NamedNodeMap np = npcNode.getAttributes();
										final int itemId = parseInteger(np, "itemId");
										final SiegeGuardType type = parseEnum(tpNode.getAttributes(), SiegeGuardType.class, "type");
										final boolean stationary = parseBoolean(np, "stationary", false);
										final int npcId = parseInteger(np, "npcId");
										final int npcMaxAmount = parseInteger(np, "npcMaxAmount");
										guards.add(new SiegeGuardHolder(castleId, itemId, type, stationary, npcId, npcMaxAmount));
									}
								}
								_siegeGuards.put(castleId, guards);
							}
						}
					}
				}
			}
		}
	}
	
	public List<CastleSpawnHolder> getSpawnsForSide(int castleId, CastleSide side)
	{
		final List<CastleSpawnHolder> result = new ArrayList<>();
		if (_spawns.containsKey(castleId))
		{
			for (CastleSpawnHolder spawn : _spawns.get(castleId))
			{
				if (spawn.getSide() == side)
				{
					result.add(spawn);
				}
			}
		}
		return result;
	}
	
	public List<SiegeGuardHolder> getSiegeGuardsForCastle(int castleId)
	{
		return _siegeGuards.getOrDefault(castleId, Collections.emptyList());
	}
	
	public Map<Integer, List<SiegeGuardHolder>> getSiegeGuards()
	{
		return _siegeGuards;
	}
	
	/**
	 * Gets the single instance of CastleData.
	 * @return single instance of CastleData
	 */
	public static CastleData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final CastleData INSTANCE = new CastleData();
	}
}
