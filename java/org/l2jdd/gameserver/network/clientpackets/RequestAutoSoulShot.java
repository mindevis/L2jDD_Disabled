
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.enums.PrivateStoreType;
import org.l2jdd.gameserver.enums.ShotType;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.items.Item;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.items.type.ActionType;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExAutoSoulShot;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Unknown, UnAfraid
 */
public class RequestAutoSoulShot implements IClientIncomingPacket
{
	private int _itemId;
	private boolean _enable;
	private int _type;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_itemId = packet.readD();
		_enable = packet.readD() == 1;
		_type = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if ((player.getPrivateStoreType() == PrivateStoreType.NONE) && (player.getActiveRequester() == null) && !player.isDead())
		{
			final ItemInstance item = player.getInventory().getItemByItemId(_itemId);
			if (item == null)
			{
				return;
			}
			
			if (_enable)
			{
				if (!player.getInventory().canManipulateWithItemId(item.getId()))
				{
					player.sendMessage("Cannot use this item.");
					return;
				}
				
				if (isSummonShot(item.getItem()))
				{
					if (player.hasSummon())
					{
						final boolean isSoulshot = item.getEtcItem().getDefaultAction() == ActionType.SUMMON_SOULSHOT;
						final boolean isSpiritshot = item.getEtcItem().getDefaultAction() == ActionType.SUMMON_SPIRITSHOT;
						if (isSoulshot)
						{
							int soulshotCount = 0;
							final Summon pet = player.getPet();
							if (pet != null)
							{
								soulshotCount += pet.getSoulShotsPerHit();
							}
							for (Summon servitor : player.getServitors().values())
							{
								soulshotCount += servitor.getSoulShotsPerHit();
							}
							if (soulshotCount > item.getCount())
							{
								client.sendPacket(SystemMessageId.YOU_DON_T_HAVE_ENOUGH_SOULSHOTS_NEEDED_FOR_A_SERVITOR);
								return;
							}
						}
						else if (isSpiritshot)
						{
							int spiritshotCount = 0;
							final Summon pet = player.getPet();
							if (pet != null)
							{
								spiritshotCount += pet.getSpiritShotsPerHit();
							}
							for (Summon servitor : player.getServitors().values())
							{
								spiritshotCount += servitor.getSpiritShotsPerHit();
							}
							if (spiritshotCount > item.getCount())
							{
								client.sendPacket(SystemMessageId.YOU_DON_T_HAVE_ENOUGH_SOULSHOTS_NEEDED_FOR_A_SERVITOR);
								return;
							}
						}
						
						// Activate shots
						player.addAutoSoulShot(_itemId);
						client.sendPacket(new ExAutoSoulShot(_itemId, _enable, _type));
						
						// Recharge summon's shots
						final Summon pet = player.getPet();
						if (pet != null)
						{
							// Send message
							if (!pet.isChargedShot(item.getItem().getDefaultAction() == ActionType.SUMMON_SOULSHOT ? ShotType.SOULSHOTS : ((item.getId() == 6647) || (item.getId() == 20334)) ? ShotType.BLESSED_SPIRITSHOTS : ShotType.SPIRITSHOTS))
							{
								final SystemMessage sm = new SystemMessage(SystemMessageId.THE_AUTOMATIC_USE_OF_S1_HAS_BEEN_ACTIVATED);
								sm.addItemName(item);
								client.sendPacket(sm);
							}
							// Charge
							pet.rechargeShots(isSoulshot, isSpiritshot, false);
						}
						for (Summon summon : player.getServitors().values())
						{
							// Send message
							if (!summon.isChargedShot(item.getItem().getDefaultAction() == ActionType.SUMMON_SOULSHOT ? ShotType.SOULSHOTS : ((item.getId() == 6647) || (item.getId() == 20334)) ? ShotType.BLESSED_SPIRITSHOTS : ShotType.SPIRITSHOTS))
							{
								final SystemMessage sm = new SystemMessage(SystemMessageId.THE_AUTOMATIC_USE_OF_S1_HAS_BEEN_ACTIVATED);
								sm.addItemName(item);
								client.sendPacket(sm);
							}
							// Charge
							summon.rechargeShots(isSoulshot, isSpiritshot, false);
						}
					}
					else
					{
						client.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_A_SERVITOR_OR_PET_AND_THEREFORE_CANNOT_USE_THE_AUTOMATIC_USE_FUNCTION);
					}
				}
				else if (isPlayerShot(item.getItem()))
				{
					final boolean isSoulshot = item.getEtcItem().getDefaultAction() == ActionType.SOULSHOT;
					final boolean isSpiritshot = item.getEtcItem().getDefaultAction() == ActionType.SPIRITSHOT;
					final boolean isFishingshot = item.getEtcItem().getDefaultAction() == ActionType.FISHINGSHOT;
					if (player.getActiveWeaponItem() == player.getFistsWeaponItem())
					{
						client.sendPacket(isSoulshot ? SystemMessageId.THE_SOULSHOT_YOU_ARE_ATTEMPTING_TO_USE_DOES_NOT_MATCH_THE_GRADE_OF_YOUR_EQUIPPED_WEAPON : SystemMessageId.YOUR_SPIRITSHOT_DOES_NOT_MATCH_THE_WEAPON_S_GRADE);
						return;
					}
					
					// Activate shots
					player.addAutoSoulShot(_itemId);
					client.sendPacket(new ExAutoSoulShot(_itemId, _enable, _type));
					
					// Send message
					final SystemMessage sm = new SystemMessage(SystemMessageId.THE_AUTOMATIC_USE_OF_S1_HAS_BEEN_ACTIVATED);
					sm.addItemName(item);
					client.sendPacket(sm);
					
					// Recharge player's shots
					player.rechargeShots(isSoulshot, isSpiritshot, isFishingshot);
				}
			}
			else
			{
				// Cancel auto shots
				player.removeAutoSoulShot(_itemId);
				client.sendPacket(new ExAutoSoulShot(_itemId, _enable, _type));
				
				// Send message
				final SystemMessage sm = new SystemMessage(SystemMessageId.THE_AUTOMATIC_USE_OF_S1_HAS_BEEN_DEACTIVATED);
				sm.addItemName(item);
				client.sendPacket(sm);
			}
		}
	}
	
	public static boolean isPlayerShot(Item item)
	{
		switch (item.getDefaultAction())
		{
			case SPIRITSHOT:
			case SOULSHOT:
			case FISHINGSHOT:
			{
				return true;
			}
			default:
			{
				return false;
			}
		}
	}
	
	public static boolean isSummonShot(Item item)
	{
		switch (item.getDefaultAction())
		{
			case SUMMON_SPIRITSHOT:
			case SUMMON_SOULSHOT:
			{
				return true;
			}
			default:
			{
				return false;
			}
		}
	}
}
