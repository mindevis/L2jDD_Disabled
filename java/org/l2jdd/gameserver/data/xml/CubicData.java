
package org.l2jdd.gameserver.data.xml;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.l2jdd.commons.util.IXmlReader;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.templates.CubicTemplate;
import org.l2jdd.gameserver.model.cubic.CubicSkill;
import org.l2jdd.gameserver.model.cubic.ICubicConditionHolder;
import org.l2jdd.gameserver.model.cubic.conditions.HealthCondition;
import org.l2jdd.gameserver.model.cubic.conditions.HpCondition;
import org.l2jdd.gameserver.model.cubic.conditions.HpCondition.HpConditionType;
import org.l2jdd.gameserver.model.cubic.conditions.RangeCondition;

/**
 * @author UnAfraid
 */
public class CubicData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(CubicData.class.getName());
	
	private final Map<Integer, Map<Integer, CubicTemplate>> _cubics = new ConcurrentHashMap<>();
	
	protected CubicData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_cubics.clear();
		parseDatapackDirectory("data/stats/cubics", true);
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _cubics.size() + " cubics.");
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "cubic", cubicNode -> parseTemplate(cubicNode, new CubicTemplate(new StatSet(parseAttributes(cubicNode))))));
	}
	
	/**
	 * @param cubicNode
	 * @param template
	 */
	private void parseTemplate(Node cubicNode, CubicTemplate template)
	{
		forEach(cubicNode, IXmlReader::isNode, innerNode ->
		{
			switch (innerNode.getNodeName())
			{
				case "conditions":
				{
					parseConditions(innerNode, template, template);
					break;
				}
				case "skills":
				{
					parseSkills(innerNode, template);
					break;
				}
			}
		});
		_cubics.computeIfAbsent(template.getId(), key -> new HashMap<>()).put(template.getLevel(), template);
	}
	
	/**
	 * @param cubicNode
	 * @param template
	 * @param holder
	 */
	private void parseConditions(Node cubicNode, CubicTemplate template, ICubicConditionHolder holder)
	{
		forEach(cubicNode, IXmlReader::isNode, conditionNode ->
		{
			switch (conditionNode.getNodeName())
			{
				case "hp":
				{
					final HpConditionType type = parseEnum(conditionNode.getAttributes(), HpConditionType.class, "type");
					final int hpPer = parseInteger(conditionNode.getAttributes(), "percent");
					holder.addCondition(new HpCondition(type, hpPer));
					break;
				}
				case "range":
				{
					final int range = parseInteger(conditionNode.getAttributes(), "value");
					holder.addCondition(new RangeCondition(range));
					break;
				}
				case "healthPercent":
				{
					final int min = parseInteger(conditionNode.getAttributes(), "min");
					final int max = parseInteger(conditionNode.getAttributes(), "max");
					holder.addCondition(new HealthCondition(min, max));
					break;
				}
				default:
				{
					LOGGER.warning("Attempting to use not implemented condition: " + conditionNode.getNodeName() + " for cubic id: " + template.getId() + " level: " + template.getLevel());
					break;
				}
			}
		});
	}
	
	/**
	 * @param cubicNode
	 * @param template
	 */
	private void parseSkills(Node cubicNode, CubicTemplate template)
	{
		forEach(cubicNode, "skill", skillNode ->
		{
			final CubicSkill skill = new CubicSkill(new StatSet(parseAttributes(skillNode)));
			forEach(cubicNode, "conditions", conditionNode -> parseConditions(cubicNode, template, skill));
			template.getSkills().add(skill);
		});
	}
	
	/**
	 * @param id
	 * @param level
	 * @return the CubicTemplate for specified id and level
	 */
	public CubicTemplate getCubicTemplate(int id, int level)
	{
		return _cubics.getOrDefault(id, Collections.emptyMap()).get(level);
	}
	
	/**
	 * Gets the single instance of CubicData.
	 * @return single instance of CubicData
	 */
	public static CubicData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final CubicData INSTANCE = new CubicData();
	}
}
