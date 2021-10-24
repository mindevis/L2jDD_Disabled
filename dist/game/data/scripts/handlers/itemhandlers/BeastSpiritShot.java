
package handlers.itemhandlers;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.gameserver.enums.ItemSkillType;
import org.l2jdd.gameserver.enums.ShotType;
import org.l2jdd.gameserver.handler.IItemHandler;
import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.ItemSkillHolder;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.MagicSkillUse;
import org.l2jdd.gameserver.util.Broadcast;

/**
 * Beast SpiritShot Handler
 * @author Tempy
 */
public class BeastSpiritShot implements IItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		final PlayerInstance activeOwner = playable.getActingPlayer();
		if (!activeOwner.hasSummon())
		{
			activeOwner.sendPacket(SystemMessageId.SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME);
			return false;
		}
		
		final Summon pet = playable.getPet();
		if ((pet != null) && pet.isDead())
		{
			activeOwner.sendPacket(SystemMessageId.SOULSHOTS_AND_SPIRITSHOTS_ARE_NOT_AVAILABLE_FOR_A_DEAD_PET_OR_SERVITOR_SAD_ISN_T_IT);
			return false;
		}
		
		final List<Summon> aliveServitor = new ArrayList<>();
		for (Summon s : playable.getServitors().values())
		{
			if (!s.isDead())
			{
				aliveServitor.add(s);
			}
		}
		
		if ((pet == null) && aliveServitor.isEmpty())
		{
			activeOwner.sendPacket(SystemMessageId.SOULSHOTS_AND_SPIRITSHOTS_ARE_NOT_AVAILABLE_FOR_A_DEAD_PET_OR_SERVITOR_SAD_ISN_T_IT);
			return false;
		}
		
		final int itemId = item.getId();
		final boolean isBlessed = ((itemId == 6647) || (itemId == 20334)); // TODO: Unhardcode these!
		final List<ItemSkillHolder> skills = item.getItem().getSkills(ItemSkillType.NORMAL);
		final ShotType shotType = isBlessed ? ShotType.BLESSED_SPIRITSHOTS : ShotType.SPIRITSHOTS;
		short shotConsumption = 0;
		if ((pet != null) && !pet.isChargedShot(shotType))
		{
			shotConsumption += pet.getSpiritShotsPerHit();
		}
		
		for (Summon servitors : aliveServitor)
		{
			if (!servitors.isChargedShot(shotType))
			{
				shotConsumption += servitors.getSpiritShotsPerHit();
			}
		}
		
		if (skills == null)
		{
			LOGGER.warning(getClass().getSimpleName() + ": is missing skills!");
			return false;
		}
		
		final long shotCount = item.getCount();
		if (shotCount < shotConsumption)
		{
			// Not enough SpiritShots to use.
			if (!activeOwner.disableAutoShot(itemId))
			{
				activeOwner.sendPacket(SystemMessageId.YOU_DON_T_HAVE_ENOUGH_SPIRITSHOTS_NEEDED_FOR_A_PET_SERVITOR);
			}
			return false;
		}
		
		if (!activeOwner.destroyItemWithoutTrace("Consume", item.getObjectId(), shotConsumption, null, false))
		{
			if (!activeOwner.disableAutoShot(itemId))
			{
				activeOwner.sendPacket(SystemMessageId.YOU_DON_T_HAVE_ENOUGH_SPIRITSHOTS_NEEDED_FOR_A_PET_SERVITOR);
			}
			return false;
		}
		
		// Pet uses the power of spirit.
		if ((pet != null) && !pet.isChargedShot(shotType))
		{
			activeOwner.sendMessage(isBlessed ? "Your pet uses blessed spiritshot." : "Your pet uses spiritshot."); // activeOwner.sendPacket(SystemMessageId.YOUR_PET_USES_SPIRITSHOT);
			pet.chargeShot(shotType);
			// Visual effect change if player has equipped Sapphire level 3 or higher
			if (activeOwner.getActiveShappireJewel() != null)
			{
				Broadcast.toSelfAndKnownPlayersInRadius(activeOwner, new MagicSkillUse(pet, pet, activeOwner.getActiveShappireJewel().getEffectId(), 2, 0, 0), 600);
			}
			else
			{
				skills.forEach(holder -> Broadcast.toSelfAndKnownPlayersInRadius(activeOwner, new MagicSkillUse(pet, pet, holder.getSkillId(), holder.getSkillLevel(), 0, 0), 600));
			}
		}
		
		aliveServitor.forEach(s ->
		{
			if (!s.isChargedShot(shotType))
			{
				activeOwner.sendMessage(isBlessed ? "Your servitor uses blessed spiritshot." : "Your servitor uses spiritshot."); // activeOwner.sendPacket(SystemMessageId.YOUR_PET_USES_SPIRITSHOT);
				s.chargeShot(shotType);
				// Visual effect change if player has equipped Sapphire level 3 or higher
				if (activeOwner.getActiveShappireJewel() != null)
				{
					Broadcast.toSelfAndKnownPlayersInRadius(activeOwner, new MagicSkillUse(s, s, activeOwner.getActiveShappireJewel().getEffectId(), 2, 0, 0), 600);
				}
				else
				{
					skills.forEach(holder -> Broadcast.toSelfAndKnownPlayersInRadius(activeOwner, new MagicSkillUse(s, s, holder.getSkillId(), holder.getSkillLevel(), 0, 0), 600));
				}
			}
		});
		return true;
	}
}
