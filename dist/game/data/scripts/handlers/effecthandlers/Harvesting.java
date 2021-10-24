
package handlers.effecthandlers;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.MonsterInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.holders.ItemHolder;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Harvesting effect implementation.
 * @author l3x, Zoey76
 */
public class Harvesting extends AbstractEffect
{
	public Harvesting(StatSet params)
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
		if (!effector.isPlayer() || !effected.isMonster() || !effected.isDead())
		{
			return;
		}
		
		final PlayerInstance player = effector.getActingPlayer();
		final MonsterInstance monster = (MonsterInstance) effected;
		if (player.getObjectId() != monster.getSeederId())
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_HARVEST);
		}
		else if (monster.isSeeded())
		{
			if (calcSuccess(player, monster))
			{
				final ItemHolder harvestedItem = monster.takeHarvest();
				if (harvestedItem != null)
				{
					// Add item
					player.getInventory().addItem("Harvesting", harvestedItem.getId(), harvestedItem.getCount(), player, monster);
					
					// Send system msg
					SystemMessage sm = null;
					if (item.getCount() == 1)
					{
						sm = new SystemMessage(SystemMessageId.YOU_HAVE_OBTAINED_S1);
						sm.addItemName(harvestedItem.getId());
					}
					else
					{
						sm = new SystemMessage(SystemMessageId.YOU_HAVE_OBTAINED_S2_S1);
						sm.addItemName(item.getId());
						sm.addLong(harvestedItem.getCount());
					}
					player.sendPacket(sm);
					
					// Send msg to party
					final Party party = player.getParty();
					if (party != null)
					{
						if (item.getCount() == 1)
						{
							sm = new SystemMessage(SystemMessageId.C1_HAS_OBTAINED_S2_2);
							sm.addString(player.getName());
							sm.addItemName(harvestedItem.getId());
						}
						else
						{
							sm = new SystemMessage(SystemMessageId.C1_HARVESTED_S3_S2_S);
							sm.addString(player.getName());
							sm.addLong(harvestedItem.getCount());
							sm.addItemName(harvestedItem.getId());
						}
						party.broadcastToPartyMembers(player, sm);
					}
				}
			}
			else
			{
				player.sendPacket(SystemMessageId.THE_HARVEST_HAS_FAILED);
			}
		}
		else
		{
			player.sendPacket(SystemMessageId.THE_HARVEST_FAILED_BECAUSE_THE_SEED_WAS_NOT_SOWN);
		}
	}
	
	private static boolean calcSuccess(PlayerInstance player, MonsterInstance target)
	{
		final int levelPlayer = player.getLevel();
		final int levelTarget = target.getLevel();
		
		int diff = (levelPlayer - levelTarget);
		if (diff < 0)
		{
			diff = -diff;
		}
		
		// apply penalty, target <=> player levels
		// 5% penalty for each level
		int basicSuccess = 100;
		if (diff > 5)
		{
			basicSuccess -= (diff - 5) * 5;
		}
		
		// success rate can't be less than 1%
		if (basicSuccess < 1)
		{
			basicSuccess = 1;
		}
		return Rnd.get(99) < basicSuccess;
	}
}
