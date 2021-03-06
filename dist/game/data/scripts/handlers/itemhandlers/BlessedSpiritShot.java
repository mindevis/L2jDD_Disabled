
package handlers.itemhandlers;

import java.util.List;

import org.l2jdd.gameserver.enums.ItemSkillType;
import org.l2jdd.gameserver.enums.ShotType;
import org.l2jdd.gameserver.handler.IItemHandler;
import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.ItemSkillHolder;
import org.l2jdd.gameserver.model.items.Weapon;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.MagicSkillUse;
import org.l2jdd.gameserver.util.Broadcast;

public class BlessedSpiritShot implements IItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		final PlayerInstance player = playable.getActingPlayer();
		final ItemInstance weaponInst = player.getActiveWeaponInstance();
		final Weapon weaponItem = player.getActiveWeaponItem();
		final List<ItemSkillHolder> skills = item.getItem().getSkills(ItemSkillType.NORMAL);
		if (skills == null)
		{
			LOGGER.warning(getClass().getSimpleName() + ": is missing skills!");
			return false;
		}
		
		final int itemId = item.getId();
		
		// Check if Blessed SpiritShot can be used
		if ((weaponInst == null) || (weaponItem.getSpiritShotCount() == 0))
		{
			if (!player.getAutoSoulShot().contains(itemId))
			{
				player.sendPacket(SystemMessageId.YOU_MAY_NOT_USE_SPIRITSHOTS);
			}
			return false;
		}
		
		// Check if Blessed SpiritShot is already active (it can be charged over SpiritShot)
		if (player.isChargedShot(ShotType.BLESSED_SPIRITSHOTS))
		{
			return false;
		}
		
		// Consume Blessed SpiritShot if player has enough of them
		if (!player.destroyItemWithoutTrace("Consume", item.getObjectId(), weaponItem.getSpiritShotCount(), null, false))
		{
			if (!player.disableAutoShot(itemId))
			{
				player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_SPIRITSHOT_FOR_THAT);
			}
			return false;
		}
		
		// Charge Spirit shot
		player.chargeShot(ShotType.SPIRITSHOTS);
		
		// Send message to client
		if (!player.getAutoSoulShot().contains(item.getId()))
		{
			player.sendPacket(SystemMessageId.YOUR_SPIRITSHOT_HAS_BEEN_ENABLED);
		}
		
		// Visual effect change if player has equipped Sapphire level 3 or higher
		if (player.getActiveShappireJewel() != null)
		{
			Broadcast.toSelfAndKnownPlayersInRadius(player, new MagicSkillUse(player, player, player.getActiveShappireJewel().getEffectId(), 1, 0, 0), 600);
		}
		else
		{
			skills.forEach(holder -> Broadcast.toSelfAndKnownPlayersInRadius(player, new MagicSkillUse(player, player, holder.getSkillId(), holder.getSkillLevel(), 0, 0), 600));
		}
		return true;
	}
}
