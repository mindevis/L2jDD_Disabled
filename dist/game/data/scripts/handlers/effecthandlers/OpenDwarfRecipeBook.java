
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.PrivateStoreType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.RecipeBookItemList;

/**
 * Open Dwarf Recipe Book effect implementation.
 * @author Adry_85
 */
public class OpenDwarfRecipeBook extends AbstractEffect
{
	public OpenDwarfRecipeBook(StatSet params)
	{
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final PlayerInstance casterPlayer = effector.getActingPlayer();
		if (casterPlayer == null)
		{
			return;
		}
		
		if (casterPlayer.getPrivateStoreType() == PrivateStoreType.MANUFACTURE)
		{
			casterPlayer.sendPacket(SystemMessageId.YOU_MAY_NOT_ALTER_YOUR_RECIPE_BOOK_WHILE_ENGAGED_IN_MANUFACTURING);
			return;
		}
		
		if (casterPlayer.isProcessingTransaction())
		{
			casterPlayer.sendPacket(SystemMessageId.ITEM_CREATION_IS_NOT_POSSIBLE_WHILE_ENGAGED_IN_A_TRADE);
			return;
		}
		
		casterPlayer.sendPacket(new RecipeBookItemList(casterPlayer, true));
	}
}
