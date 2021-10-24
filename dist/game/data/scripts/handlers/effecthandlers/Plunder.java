
package handlers.effecthandlers;

import java.util.Collection;

import org.l2jdd.gameserver.ai.CtrlEvent;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.MonsterInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.holders.ItemHolder;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Formulas;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * @author Sdw
 */
public class Plunder extends AbstractEffect
{
	public Plunder(StatSet params)
	{
	}
	
	@Override
	public boolean calcSuccess(Creature effector, Creature effected, Skill skill)
	{
		return Formulas.calcMagicSuccess(effector, effected, skill);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (!effector.isPlayer())
		{
			return;
		}
		else if (!effected.isMonster() || effected.isDead())
		{
			effector.sendPacket(SystemMessageId.INVALID_TARGET);
			return;
		}
		
		final MonsterInstance monster = (MonsterInstance) effected;
		final PlayerInstance player = effector.getActingPlayer();
		
		if (monster.isSpoiled())
		{
			effector.sendPacket(SystemMessageId.PLUNDER_SKILL_HAS_BEEN_ALREADY_USED_ON_THIS_TARGET);
			return;
		}
		
		monster.setPlundered(player);
		
		if (!player.getInventory().checkInventorySlotsAndWeight(monster.getSpoilLootItems(), false, false))
		{
			return;
		}
		
		final Collection<ItemHolder> items = monster.takeSweep();
		if (items != null)
		{
			final boolean lucky = player.tryLuck();
			for (ItemHolder sweepedItem : items)
			{
				final ItemHolder rewardedItem = new ItemHolder(sweepedItem.getId(), sweepedItem.getCount() * (lucky ? 2 : 1));
				final Party party = effector.getParty();
				if (party != null)
				{
					party.distributeItem(player, rewardedItem, true, monster);
				}
				else
				{
					player.addItem("Plunder", rewardedItem, effected, true);
				}
			}
		}
		
		monster.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, effector);
	}
}
