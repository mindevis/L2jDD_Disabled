
package org.l2jdd.gameserver.data.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.l2jdd.Config;
import org.l2jdd.commons.util.IXmlReader;
import org.l2jdd.gameserver.enums.ClassId;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.templates.PlayerTemplate;

/**
 * Loads player's base stats.
 * @author Forsaiken, Zoey76, GKR
 */
public class PlayerTemplateData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(PlayerTemplateData.class.getName());
	
	private final Map<ClassId, PlayerTemplate> _playerTemplates = new ConcurrentHashMap<>();
	
	private int _dataCount = 0;
	private int _autoGeneratedCount = 0;
	
	protected PlayerTemplateData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_playerTemplates.clear();
		parseDatapackDirectory("data/stats/chars/baseStats", false);
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _playerTemplates.size() + " character templates.");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _dataCount + " level up gain records.");
		if (_autoGeneratedCount > 0)
		{
			LOGGER.info(getClass().getSimpleName() + ": Generated " + _autoGeneratedCount + " level up gain records.");
		}
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		NamedNodeMap attrs;
		int classId = 0;
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equalsIgnoreCase(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("classId".equalsIgnoreCase(d.getNodeName()))
					{
						classId = Integer.parseInt(d.getTextContent());
					}
					else if ("staticData".equalsIgnoreCase(d.getNodeName()))
					{
						final StatSet set = new StatSet();
						set.set("classId", classId);
						final List<Location> creationPoints = new ArrayList<>();
						for (Node nd = d.getFirstChild(); nd != null; nd = nd.getNextSibling())
						{
							// Skip odd nodes
							if (nd.getNodeName().equals("#text"))
							{
								continue;
							}
							
							if (nd.getChildNodes().getLength() > 1)
							{
								for (Node cnd = nd.getFirstChild(); cnd != null; cnd = cnd.getNextSibling())
								{
									// use CreatureTemplate(superclass) fields for male collision height and collision radius
									if (nd.getNodeName().equalsIgnoreCase("collisionMale"))
									{
										if (cnd.getNodeName().equalsIgnoreCase("radius"))
										{
											set.set("collision_radius", cnd.getTextContent());
										}
										else if (cnd.getNodeName().equalsIgnoreCase("height"))
										{
											set.set("collision_height", cnd.getTextContent());
										}
									}
									if ("node".equalsIgnoreCase(cnd.getNodeName()))
									{
										attrs = cnd.getAttributes();
										creationPoints.add(new Location(parseInteger(attrs, "x"), parseInteger(attrs, "y"), parseInteger(attrs, "z")));
									}
									else if ("walk".equalsIgnoreCase(cnd.getNodeName()))
									{
										set.set("baseWalkSpd", cnd.getTextContent());
									}
									else if ("run".equalsIgnoreCase(cnd.getNodeName()))
									{
										set.set("baseRunSpd", cnd.getTextContent());
									}
									else if ("slowSwim".equals(cnd.getNodeName()))
									{
										set.set("baseSwimWalkSpd", cnd.getTextContent());
									}
									else if ("fastSwim".equals(cnd.getNodeName()))
									{
										set.set("baseSwimRunSpd", cnd.getTextContent());
									}
									else if (!cnd.getNodeName().equals("#text"))
									{
										set.set((nd.getNodeName() + cnd.getNodeName()), cnd.getTextContent());
									}
								}
							}
							else
							{
								set.set(nd.getNodeName(), nd.getTextContent());
							}
						}
						// calculate total pdef and mdef from parts
						set.set("basePDef", (set.getInt("basePDefchest", 0) + set.getInt("basePDeflegs", 0) + set.getInt("basePDefhead", 0) + set.getInt("basePDeffeet", 0) + set.getInt("basePDefgloves", 0) + set.getInt("basePDefunderwear", 0) + set.getInt("basePDefcloak", 0) + set.getInt("basePDefhair", 0)));
						set.set("baseMDef", (set.getInt("baseMDefrear", 0) + set.getInt("baseMDeflear", 0) + set.getInt("baseMDefrfinger", 0) + set.getInt("baseMDefrfinger", 0) + set.getInt("baseMDefneck", 0)));
						_playerTemplates.put(ClassId.getClassId(classId), new PlayerTemplate(set, creationPoints));
					}
					else if ("lvlUpgainData".equalsIgnoreCase(d.getNodeName()))
					{
						int level = 0;
						for (Node lvlNode = d.getFirstChild(); lvlNode != null; lvlNode = lvlNode.getNextSibling())
						{
							if ("level".equalsIgnoreCase(lvlNode.getNodeName()))
							{
								attrs = lvlNode.getAttributes();
								level = parseInteger(attrs, "val");
								for (Node valNode = lvlNode.getFirstChild(); valNode != null; valNode = valNode.getNextSibling())
								{
									final String nodeName = valNode.getNodeName();
									if ((level < Config.PLAYER_MAXIMUM_LEVEL) && (nodeName.startsWith("hp") || nodeName.startsWith("mp") || nodeName.startsWith("cp")) && _playerTemplates.containsKey(ClassId.getClassId(classId)))
									{
										_playerTemplates.get(ClassId.getClassId(classId)).setUpgainValue(nodeName, level, Double.parseDouble(valNode.getTextContent()));
										_dataCount++;
									}
								}
							}
						}
						// Generate missing stats automatically.
						while (level < (Config.PLAYER_MAXIMUM_LEVEL - 1))
						{
							level++;
							_autoGeneratedCount++;
							final double hpM1 = _playerTemplates.get(ClassId.getClassId(classId)).getBaseHpMax(level - 1);
							_playerTemplates.get(ClassId.getClassId(classId)).setUpgainValue("hp", level, (((hpM1 * level) / (level - 1)) + ((hpM1 * (level + 1)) / (level - 1))) / 2);
							final double mpM1 = _playerTemplates.get(ClassId.getClassId(classId)).getBaseMpMax(level - 1);
							_playerTemplates.get(ClassId.getClassId(classId)).setUpgainValue("mp", level, (((mpM1 * level) / (level - 1)) + ((mpM1 * (level + 1)) / (level - 1))) / 2);
							final double cpM1 = _playerTemplates.get(ClassId.getClassId(classId)).getBaseCpMax(level - 1);
							_playerTemplates.get(ClassId.getClassId(classId)).setUpgainValue("cp", level, (((cpM1 * level) / (level - 1)) + ((cpM1 * (level + 1)) / (level - 1))) / 2);
							final double hpRegM1 = _playerTemplates.get(ClassId.getClassId(classId)).getBaseHpRegen(level - 1);
							final double hpRegM2 = _playerTemplates.get(ClassId.getClassId(classId)).getBaseHpRegen(level - 2);
							_playerTemplates.get(ClassId.getClassId(classId)).setUpgainValue("hpRegen", level, (hpRegM1 * 2) - hpRegM2);
							final double mpRegM1 = _playerTemplates.get(ClassId.getClassId(classId)).getBaseMpRegen(level - 1);
							final double mpRegM2 = _playerTemplates.get(ClassId.getClassId(classId)).getBaseMpRegen(level - 2);
							_playerTemplates.get(ClassId.getClassId(classId)).setUpgainValue("mpRegen", level, (mpRegM1 * 2) - mpRegM2);
							final double cpRegM1 = _playerTemplates.get(ClassId.getClassId(classId)).getBaseCpRegen(level - 1);
							final double cpRegM2 = _playerTemplates.get(ClassId.getClassId(classId)).getBaseCpRegen(level - 2);
							_playerTemplates.get(ClassId.getClassId(classId)).setUpgainValue("cpRegen", level, (cpRegM1 * 2) - cpRegM2);
						}
					}
				}
			}
		}
	}
	
	public PlayerTemplate getTemplate(ClassId classId)
	{
		return _playerTemplates.get(classId);
	}
	
	public PlayerTemplate getTemplate(int classId)
	{
		return _playerTemplates.get(ClassId.getClassId(classId));
	}
	
	public static PlayerTemplateData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final PlayerTemplateData INSTANCE = new PlayerTemplateData();
	}
}
