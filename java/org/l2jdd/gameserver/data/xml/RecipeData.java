
package org.l2jdd.gameserver.data.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.l2jdd.commons.util.IXmlReader;
import org.l2jdd.gameserver.enums.StatusUpdateType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.holders.ItemChanceHolder;
import org.l2jdd.gameserver.model.holders.ItemHolder;
import org.l2jdd.gameserver.model.holders.RecipeHolder;

/**
 * @author Nik
 */
public class RecipeData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(RecipeData.class.getName());
	
	private final Map<Integer, RecipeHolder> _recipes = new HashMap<>();
	
	protected RecipeData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_recipes.clear();
		parseDatapackFile("data/Recipes.xml");
		LOGGER.info(getClass().getSimpleName() + ": Loaded " + _recipes.size() + " recipes.");
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
					if ("recipe".equalsIgnoreCase(d.getNodeName()))
					{
						attrs = d.getAttributes();
						set = new StatSet();
						for (int i = 0; i < attrs.getLength(); i++)
						{
							att = attrs.item(i);
							set.set(att.getNodeName(), att.getNodeValue());
						}
						
						final int recipeId = set.getInt("id");
						List<ItemHolder> materials = Collections.emptyList();
						List<ItemChanceHolder> productGroup = Collections.emptyList();
						List<ItemHolder> npcFee = Collections.emptyList();
						final Map<StatusUpdateType, Double> statUse = new HashMap<>();
						
						for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling())
						{
							if ("materials".equalsIgnoreCase(c.getNodeName()))
							{
								materials = getItemList(c);
							}
							else if ("product".equalsIgnoreCase(c.getNodeName()))
							{
								productGroup = getItemList(c).stream().map(ItemChanceHolder.class::cast).collect(Collectors.toList());
							}
							else if ("npcFee".equalsIgnoreCase(c.getNodeName()))
							{
								npcFee = getItemList(c);
							}
							else if ("statUse".equalsIgnoreCase(c.getNodeName()))
							{
								for (Node b = c.getFirstChild(); b != null; b = b.getNextSibling())
								{
									if ("stat".equalsIgnoreCase(b.getNodeName()))
									{
										final StatusUpdateType stat = StatusUpdateType.valueOf(b.getAttributes().getNamedItem("name").getNodeValue());
										final double value = Double.parseDouble(b.getAttributes().getNamedItem("val").getNodeValue());
										statUse.put(stat, value);
									}
								}
							}
						}
						
						_recipes.put(recipeId, new RecipeHolder(set, materials, productGroup, npcFee, statUse));
					}
				}
			}
		}
	}
	
	private List<ItemHolder> getItemList(Node c)
	{
		final List<ItemHolder> items = new ArrayList<>();
		for (Node b = c.getFirstChild(); b != null; b = b.getNextSibling())
		{
			if ("item".equalsIgnoreCase(b.getNodeName()))
			{
				final int itemId = Integer.parseInt(b.getAttributes().getNamedItem("id").getNodeValue());
				final long itemCount = Long.parseLong(b.getAttributes().getNamedItem("count").getNodeValue());
				
				if (b.getAttributes().getNamedItem("chance") != null)
				{
					final double chance = Double.parseDouble(b.getAttributes().getNamedItem("chance").getNodeValue());
					items.add(new ItemChanceHolder(itemId, chance, itemCount));
				}
				else
				{
					items.add(new ItemHolder(itemId, itemCount));
				}
			}
		}
		
		return items;
	}
	
	/**
	 * Gets the recipe by recipe item id.
	 * @param itemId the recipe's item id
	 * @return {@code RecipeHolder} for the given recipe item id {@code null} if there is no recipe data connected with this recipe item id.
	 */
	public RecipeHolder getRecipeByRecipeItemId(int itemId)
	{
		return _recipes.values().stream().filter(r -> r.getItemId() == itemId).findAny().orElse(null);
	}
	
	/**
	 * @param recipeId the id of the recipe, NOT the recipe item id.
	 * @return {@code RecipeHolder} containing all the info necessary for crafting a recipe or {@code null} if there is no data for this recipeId.
	 */
	public RecipeHolder getRecipe(int recipeId)
	{
		return _recipes.get(recipeId);
	}
	
	/**
	 * Gets the single instance of RecipeData.
	 * @return single instance of RecipeData
	 */
	public static RecipeData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	/**
	 * The Class SingletonHolder.
	 */
	private static class SingletonHolder
	{
		protected static final RecipeData INSTANCE = new RecipeData();
	}
}
