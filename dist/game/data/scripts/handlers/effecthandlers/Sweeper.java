
package handlers.effecthandlers;

import java.util.Collection;

import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.holders.ItemHolder;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Sweeper effect implementation.
 * @author Zoey76
 */
public class Sweeper extends AbstractEffect
{
	public Sweeper(StatSet params)
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
		if (!effector.isPlayer() || !effected.isAttackable())
		{
			return;
		}
		
		final PlayerInstance player = effector.getActingPlayer();
		final Attackable monster = (Attackable) effected;
		if (!monster.checkSpoilOwner(player, false))
		{
			return;
		}
		
		if (!player.getInventory().checkInventorySlotsAndWeight(monster.getSpoilLootItems(), false, false))
		{
			return;
		}
		
		final Collection<ItemHolder> items = monster.takeSweep();
		if (items != null)
		{
			for (ItemHolder sweepedItem : items)
			{
				final Party party = player.getParty();
				if (party != null)
				{
					party.distributeItem(player, sweepedItem, true, monster);
				}
				else
				{
					player.addItem("Sweeper", sweepedItem, effected, true);
				}
			}
		}
	}
}
