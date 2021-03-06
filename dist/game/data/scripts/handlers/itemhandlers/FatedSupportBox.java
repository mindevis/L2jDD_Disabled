
package handlers.itemhandlers;

import org.l2jdd.gameserver.data.xml.CategoryData;
import org.l2jdd.gameserver.enums.CategoryType;
import org.l2jdd.gameserver.enums.ClassId;
import org.l2jdd.gameserver.enums.Race;
import org.l2jdd.gameserver.handler.IItemHandler;
import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.InventoryUpdate;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 */
public class FatedSupportBox implements IItemHandler
{
	// Items
	private static final int FATED_BOX_ERTHEIA_WIZARD = 26229;
	private static final int FATED_BOX_ERTHEIA_FIGHTER = 26230;
	private static final int FATED_BOX_FIGHTER = 37315;
	private static final int FATED_BOX_WIZARD = 37316;
	private static final int FATED_BOX_WARRIOR = 37317;
	private static final int FATED_BOX_ROGUE = 37318;
	private static final int FATED_BOX_KAMAEL = 37319;
	private static final int FATED_BOX_ORC_FIGHTER = 37320;
	private static final int FATED_BOX_ORC_WIZARD = 37321;
	
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		final PlayerInstance player = playable.getActingPlayer();
		final Race race = player.getRace();
		final ClassId classId = player.getClassId();
		
		if (!player.isInventoryUnder80(false))
		{
			player.sendPacket(SystemMessageId.YOU_HAVE_EXCEEDED_THE_ITEM_OWNERSHIP_LIMIT_AND_YOU_CANNOT_TAKE_THE_ITEM_CHECK_ITEM_OWNERSHIP_TIME_LIMITS_FOR_THE_INVENTORY_PLEASE);
			return false;
		}
		
		// Characters that have gone through their 2nd class transfer/1st liberation will be able to open the Fated Support Box at level 40.
		if ((player.getLevel() < 40) || player.isInCategory(CategoryType.FIRST_CLASS_GROUP) || ((race != Race.ERTHEIA) && player.isInCategory(CategoryType.SECOND_CLASS_GROUP)))
		{
			player.sendPacket(new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item));
			return false;
		}
		
		player.getInventory().destroyItem(getClass().getSimpleName(), item, 1, player, null);
		player.sendPacket(new InventoryUpdate(item));
		
		// It will stay in your inventory after use until you reach level 84.
		if (player.getLevel() > 84)
		{
			player.sendMessage("Fated Support Box was removed because your level has exceeded the maximum requirement."); // custom message
			return true;
		}
		
		switch (race)
		{
			case HUMAN:
			case ELF:
			case DARK_ELF:
			case DWARF:
			{
				if (player.isMageClass())
				{
					player.addItem(getClass().getSimpleName(), FATED_BOX_WIZARD, 1, player, true);
				}
				else if (CategoryData.getInstance().isInCategory(CategoryType.SUB_GROUP_ROGUE, classId.getId()))
				{
					player.addItem(getClass().getSimpleName(), FATED_BOX_ROGUE, 1, player, true);
				}
				else if (CategoryData.getInstance().isInCategory(CategoryType.SUB_GROUP_KNIGHT, classId.getId()))
				{
					player.addItem(getClass().getSimpleName(), FATED_BOX_FIGHTER, 1, player, true);
				}
				else
				{
					player.addItem(getClass().getSimpleName(), FATED_BOX_WARRIOR, 1, player, true);
				}
				break;
			}
			case ORC:
			{
				if (player.isMageClass())
				{
					player.addItem(getClass().getSimpleName(), FATED_BOX_ORC_WIZARD, 1, player, true);
				}
				else
				{
					player.addItem(getClass().getSimpleName(), FATED_BOX_ORC_FIGHTER, 1, player, true);
				}
				break;
			}
			case KAMAEL:
			{
				player.addItem(getClass().getSimpleName(), FATED_BOX_KAMAEL, 1, player, true);
				break;
			}
			case ERTHEIA:
			{
				if (player.isMageClass())
				{
					player.addItem(getClass().getSimpleName(), FATED_BOX_ERTHEIA_WIZARD, 1, player, true);
				}
				else
				{
					player.addItem(getClass().getSimpleName(), FATED_BOX_ERTHEIA_FIGHTER, 1, player, true);
				}
				break;
			}
		}
		return true;
	}
}
