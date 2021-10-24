
package org.l2jdd.gameserver.data.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import org.l2jdd.commons.util.IXmlReader;
import org.l2jdd.gameserver.data.ItemTable;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.holders.EquipmentUpgradeHolder;
import org.l2jdd.gameserver.model.holders.ItemHolder;

/**
 * @author Mobius
 */
public class EquipmentUpgradeData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(EquipmentUpgradeData.class.getName());
	private static final Map<Integer, EquipmentUpgradeHolder> _upgrades = new HashMap<>();
	
	protected EquipmentUpgradeData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_upgrades.clear();
		parseDatapackFile("data/EquipmentUpgradeData.xml");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _upgrades.size() + " upgrade equipment data.");
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "upgrade", upgradeNode ->
		{
			final StatSet set = new StatSet(parseAttributes(upgradeNode));
			final int id = set.getInt("id");
			final String[] item = set.getString("item").split(",");
			final int requiredItemId = Integer.parseInt(item[0]);
			final int requiredItemEnchant = Integer.parseInt(item[1]);
			final String materials = set.getString("materials", "");
			final List<ItemHolder> materialList = new ArrayList<>();
			if (!materials.isEmpty())
			{
				for (String mat : materials.split(";"))
				{
					final String[] matValues = mat.split(",");
					final int matItemId = Integer.parseInt(matValues[0]);
					if (ItemTable.getInstance().getTemplate(matItemId) == null)
					{
						LOGGER.info(getClass().getSimpleName() + ": Material item with id " + matItemId + " does not exist.");
					}
					else
					{
						materialList.add(new ItemHolder(matItemId, Long.parseLong(matValues[1])));
					}
				}
			}
			final long adena = set.getLong("adena", 0);
			final String[] resultItem = set.getString("result").split(",");
			final int resultItemId = Integer.parseInt(resultItem[0]);
			final int resultItemEnchant = Integer.parseInt(resultItem[1]);
			if (ItemTable.getInstance().getTemplate(requiredItemId) == null)
			{
				LOGGER.info(getClass().getSimpleName() + ": Required item with id " + requiredItemId + " does not exist.");
			}
			else
			{
				_upgrades.put(id, new EquipmentUpgradeHolder(id, requiredItemId, requiredItemEnchant, materialList, adena, resultItemId, resultItemEnchant));
			}
		}));
	}
	
	public EquipmentUpgradeHolder getUpgrade(int id)
	{
		return _upgrades.get(id);
	}
	
	public static EquipmentUpgradeData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final EquipmentUpgradeData INSTANCE = new EquipmentUpgradeData();
	}
}
