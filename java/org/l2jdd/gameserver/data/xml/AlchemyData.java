
package org.l2jdd.gameserver.data.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.l2jdd.commons.util.IXmlReader;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.alchemy.AlchemyCraftData;
import org.l2jdd.gameserver.model.holders.ItemHolder;

/**
 * @author Sdw
 */
public class AlchemyData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(AlchemyData.class.getName());
	
	private final Map<Long, AlchemyCraftData> _alchemy = new HashMap<>();
	
	protected AlchemyData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_alchemy.clear();
		parseDatapackFile("data/AlchemyData.xml");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _alchemy.size() + " alchemy craft skills.");
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		StatSet set;
		Node att;
		NamedNodeMap attrs;
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equalsIgnoreCase(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("skill".equalsIgnoreCase(d.getNodeName()))
					{
						attrs = d.getAttributes();
						set = new StatSet();
						for (int i = 0; i < attrs.getLength(); i++)
						{
							att = attrs.item(i);
							set.set(att.getNodeName(), att.getNodeValue());
						}
						
						final AlchemyCraftData alchemyCraft = new AlchemyCraftData(set);
						for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling())
						{
							if ("ingredients".equalsIgnoreCase(c.getNodeName()))
							{
								for (Node b = c.getFirstChild(); b != null; b = b.getNextSibling())
								{
									if ("item".equalsIgnoreCase(b.getNodeName()))
									{
										final int ingId = Integer.parseInt(b.getAttributes().getNamedItem("id").getNodeValue());
										final int ingCount = Integer.parseInt(b.getAttributes().getNamedItem("count").getNodeValue());
										alchemyCraft.addIngredient(new ItemHolder(ingId, ingCount));
									}
								}
							}
							else if ("production".equalsIgnoreCase(c.getNodeName()))
							{
								for (Node b = c.getFirstChild(); b != null; b = b.getNextSibling())
								{
									if ("item".equalsIgnoreCase(b.getNodeName()))
									{
										final String type = b.getAttributes().getNamedItem("type").getNodeValue();
										final int prodId = Integer.parseInt(b.getAttributes().getNamedItem("id").getNodeValue());
										final int prodCount = Integer.parseInt(b.getAttributes().getNamedItem("count").getNodeValue());
										if (type.equalsIgnoreCase("ON_SUCCESS"))
										{
											alchemyCraft.setProductionSuccess(new ItemHolder(prodId, prodCount));
										}
										else if (type.equalsIgnoreCase("ON_FAILURE"))
										{
											alchemyCraft.setProductionFailure(new ItemHolder(prodId, prodCount));
										}
									}
								}
							}
						}
						_alchemy.put(SkillData.getSkillHashCode(set.getInt("id"), set.getInt("level")), alchemyCraft);
					}
				}
			}
		}
	}
	
	public AlchemyCraftData getCraftData(int skillId, int skillLevel)
	{
		return _alchemy.get(SkillData.getSkillHashCode(skillId, skillLevel));
	}
	
	/**
	 * Gets the single instance of AlchemyData.
	 * @return single instance of AlchemyData
	 */
	public static AlchemyData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final AlchemyData INSTANCE = new AlchemyData();
	}
}
