
package handlers.itemhandlers;

import org.l2jdd.Config;
import org.l2jdd.gameserver.data.xml.RecipeData;
import org.l2jdd.gameserver.handler.IItemHandler;
import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.RecipeHolder;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Zoey76
 */
public class Recipes implements IItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		if (!Config.IS_CRAFTING_ENABLED)
		{
			playable.sendMessage("Crafting is disabled, you cannot register this recipe.");
			return false;
		}
		
		final PlayerInstance player = playable.getActingPlayer();
		if (player.isCrafting())
		{
			player.sendPacket(SystemMessageId.YOU_MAY_NOT_ALTER_YOUR_RECIPE_BOOK_WHILE_ENGAGED_IN_MANUFACTURING);
			return false;
		}
		
		final RecipeHolder rp = RecipeData.getInstance().getRecipeByRecipeItemId(item.getId());
		if (rp == null)
		{
			player.sendPacket(SystemMessageId.THE_RECIPE_IS_INCORRECT);
			return false;
		}
		
		if (player.hasRecipeList(rp.getId()))
		{
			player.sendPacket(SystemMessageId.THAT_RECIPE_IS_ALREADY_REGISTERED);
			return false;
		}
		
		boolean canCraft = false;
		boolean recipeLevel = false;
		boolean recipeLimit = false;
		if (rp.isDwarvenRecipe())
		{
			canCraft = player.getCreateItemLevel() > 0;
			recipeLevel = (rp.getLevel() > player.getCreateItemLevel());
			recipeLimit = (player.getDwarvenRecipeBook().size() >= player.getDwarfRecipeLimit());
		}
		else
		{
			canCraft = player.getCreateCommonItemLevel() > 0;
			recipeLevel = (rp.getLevel() > player.getCreateCommonItemLevel());
			recipeLimit = (player.getCommonRecipeBook().size() >= player.getCommonRecipeLimit());
		}
		
		if (!canCraft)
		{
			player.sendPacket(SystemMessageId.THE_RECIPE_CANNOT_BE_REGISTERED_YOU_DO_NOT_HAVE_THE_ABILITY_TO_CREATE_ITEMS);
			return false;
		}
		
		if (recipeLevel)
		{
			player.sendPacket(SystemMessageId.YOUR_CREATE_ITEM_LEVEL_IS_TOO_LOW_TO_REGISTER_THIS_RECIPE);
			return false;
		}
		
		if (recipeLimit)
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.UP_TO_S1_RECIPES_CAN_BE_REGISTERED);
			sm.addInt(rp.isDwarvenRecipe() ? player.getDwarfRecipeLimit() : player.getCommonRecipeLimit());
			player.sendPacket(sm);
			return false;
		}
		
		if (rp.isDwarvenRecipe())
		{
			player.registerDwarvenRecipeList(rp, true);
		}
		else
		{
			player.registerCommonRecipeList(rp, true);
		}
		
		player.destroyItem("Consume", item.getObjectId(), 1, null, false);
		final SystemMessage sm = new SystemMessage(SystemMessageId.S1_HAS_BEEN_ADDED);
		sm.addItemName(item);
		player.sendPacket(sm);
		return true;
	}
}
