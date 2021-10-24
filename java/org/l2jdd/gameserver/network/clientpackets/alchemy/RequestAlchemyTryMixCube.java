
package org.l2jdd.gameserver.network.clientpackets.alchemy;

import java.util.LinkedList;
import java.util.List;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.enums.PrivateStoreType;
import org.l2jdd.gameserver.enums.Race;
import org.l2jdd.gameserver.enums.TryMixCubeResultType;
import org.l2jdd.gameserver.enums.TryMixCubeType;
import org.l2jdd.gameserver.model.PlayerCondOverride;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.AlchemyResult;
import org.l2jdd.gameserver.model.holders.ItemHolder;
import org.l2jdd.gameserver.model.itemcontainer.Inventory;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.CommonSkill;
import org.l2jdd.gameserver.network.Disconnection;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.clientpackets.IClientIncomingPacket;
import org.l2jdd.gameserver.network.serverpackets.InventoryUpdate;
import org.l2jdd.gameserver.network.serverpackets.MagicSkillUse;
import org.l2jdd.gameserver.network.serverpackets.alchemy.ExTryMixCube;
import org.l2jdd.gameserver.taskmanager.AttackStanceTaskManager;

/**
 * @author Sdw
 */
public class RequestAlchemyTryMixCube implements IClientIncomingPacket
{
	// TODO: Figure out how much stones are given
	private static final int TEMPEST_STONE_AMOUNT = 1;
	
	private final List<ItemHolder> _items = new LinkedList<>();
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		final int itemsCount = packet.readD();
		if ((itemsCount <= 0) || (itemsCount > 4))
		{
			return false;
		}
		
		int id;
		long count;
		for (int i = 0; i < itemsCount; i++)
		{
			id = packet.readD();
			count = packet.readQ();
			if ((count > 0) && (count < Long.MAX_VALUE))
			{
				_items.add(new ItemHolder(id, count));
			}
			else // Player used packet injection tool.
			{
				final PlayerInstance player = client.getPlayer();
				LOGGER.warning("Kicked " + player + " for using packet injection tool with " + getClass().getSimpleName());
				Disconnection.of(player).defaultSequence(true);
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if ((player == null) || (player.getRace() != Race.ERTHEIA))
		{
			return;
		}
		
		if (AttackStanceTaskManager.getInstance().hasAttackStanceTask(player))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_USE_ALCHEMY_DURING_BATTLE);
			return;
		}
		else if (player.isInStoreMode() || (player.getPrivateStoreType() != PrivateStoreType.NONE))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_USE_ALCHEMY_WHILE_TRADING_OR_USING_A_PRIVATE_STORE_OR_SHOP);
			return;
		}
		else if (player.isDead())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_USE_ALCHEMY_WHILE_DEAD);
			return;
		}
		else if (player.isMovementDisabled())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_USE_ALCHEMY_WHILE_IMMOBILE);
			return;
		}
		
		if ((player.getKnownSkill(CommonSkill.ALCHEMY_CUBE.getId()) == null) && !player.canOverrideCond(PlayerCondOverride.SKILL_CONDITIONS))
		{
			player.sendPacket(new ExTryMixCube(TryMixCubeType.FAIL_SKILL_WRONG));
			return;
		}
		
		int position = 0;
		long itemsPrice = 0;
		
		// First loop for safety check + price calculation
		for (ItemHolder item : _items)
		{
			final ItemInstance itemInstance = player.getInventory().getItemByObjectId(item.getId());
			if (itemInstance == null)
			{
				return;
			}
			
			if ((itemInstance.getCount() <= 0) || (itemInstance.getCount() < item.getCount()))
			{
				player.sendPacket(new ExTryMixCube(TryMixCubeType.FAIL_ITEM_WRONG));
				return;
			}
			
			final long price = itemInstance.getReferencePrice();
			if (itemInstance.getReferencePrice() == 0)
			{
				player.sendPacket(SystemMessageId.THIS_ITEM_CANNOT_BE_COMBINED);
				player.sendPacket(new ExTryMixCube(TryMixCubeType.FAIL_ITEM_WRONG));
				return;
			}
			
			if ((itemInstance.getEnchantLevel() > 0) || itemInstance.isAugmented())
			{
				player.sendPacket(SystemMessageId.YOU_CANNOT_COMBINE_ITEMS_THAT_HAVE_BEEN_ENCHANTED_OR_AUGMENTED);
				player.sendPacket(new ExTryMixCube(TryMixCubeType.FAIL_ITEM_WRONG));
				return;
			}
			
			itemsPrice += price * item.getCount();
			position++;
			
			if ((position == 4) && (itemInstance.getId() != Inventory.ELCYUM_CRYSTAL_ID))
			{
				player.sendPacket(new ExTryMixCube(TryMixCubeType.FAIL_ITEM_WRONG));
				return;
			}
		}
		
		// Calculate the amount of air stones the player should received based on the total price of items he mixed.
		int airStonesCount = (int) Math.floor((itemsPrice / 5_000) * (_items.size() < 3 ? 0.3f : 0.5f));
		
		// Process only if there is at least one air stone to give
		if (airStonesCount > 0)
		{
			final InventoryUpdate iu = new InventoryUpdate();
			long elcyumCrystals = 0;
			
			// Second loop for items deletion if we're still in the game
			for (ItemHolder item : _items)
			{
				final ItemInstance itemInstance = player.getInventory().getItemByObjectId(item.getId());
				if (itemInstance == null)
				{
					return;
				}
				
				if (itemInstance.getCount() < item.getCount())
				{
					return;
				}
				
				if (itemInstance.getId() == Inventory.ELCYUM_CRYSTAL_ID)
				{
					elcyumCrystals = item.getCount();
				}
				
				player.getInventory().destroyItem("Alchemy", itemInstance, item.getCount(), player, null);
				iu.addItem(itemInstance);
			}
			
			final ExTryMixCube mixCubeResult = new ExTryMixCube(TryMixCubeType.SUCCESS_NORMAL);
			
			// Whenever there is Elcyum Crystal applied there's a chance to receive Tempest Stone
			// TODO: Figure out the chance
			if ((elcyumCrystals > 0) && (Rnd.get(100) < 50))
			{
				// Broadcast animation on success
				player.broadcastPacket(new MagicSkillUse(player, CommonSkill.ALCHEMY_CUBE_RANDOM_SUCCESS.getId(), TEMPEST_STONE_AMOUNT, 500, 1500));
				
				// Give Tempest Stone to the player
				final ItemInstance tempestStonesInstance = player.addItem("Alchemy", Inventory.TEMPEST_STONE_ID, TEMPEST_STONE_AMOUNT, null, true);
				iu.addItem(tempestStonesInstance);
				
				// Add the alchemy result entry to the packet
				mixCubeResult.addItem(new AlchemyResult(Inventory.TEMPEST_STONE_ID, TEMPEST_STONE_AMOUNT, TryMixCubeResultType.EXTRA));
			}
			
			// Calculate the elcyum crystals bonus
			final boolean bonusSuccess = ((100. * Rnd.nextDouble()) < (elcyumCrystals / 1000));
			if (bonusSuccess)
			{
				airStonesCount *= Math.min(elcyumCrystals, 2);
			}
			
			final ItemInstance airStonesInstance = player.addItem("Alchemy", Inventory.AIR_STONE_ID, airStonesCount, null, true);
			iu.addItem(airStonesInstance);
			
			// Add the Air Stones
			mixCubeResult.addItem(new AlchemyResult(Inventory.AIR_STONE_ID, airStonesCount, bonusSuccess ? TryMixCubeResultType.BONUS : TryMixCubeResultType.NORMAL));
			
			// send packets
			player.sendPacket(mixCubeResult);
			player.sendInventoryUpdate(iu);
		}
		else
		{
			player.sendPacket(new ExTryMixCube(TryMixCubeType.FAIL_ITEM_WRONG));
		}
	}
}
