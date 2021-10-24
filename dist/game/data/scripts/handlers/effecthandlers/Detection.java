
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.AbnormalType;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Detection effect implementation.
 * @author UnAfraid
 */
public class Detection extends AbstractEffect
{
	public Detection(StatSet params)
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
		if (!effector.isPlayer() || !effected.isPlayer())
		{
			return;
		}
		
		final PlayerInstance player = effector.getActingPlayer();
		final PlayerInstance target = effected.getActingPlayer();
		final boolean hasParty = player.isInParty();
		final boolean hasClan = player.getClanId() > 0;
		final boolean hasAlly = player.getAllyId() > 0;
		
		if (target.isInvisible())
		{
			if (hasParty && (target.isInParty()) && (player.getParty().getLeaderObjectId() == target.getParty().getLeaderObjectId()))
			{
				return;
			}
			else if (hasClan && (player.getClanId() == target.getClanId()))
			{
				return;
			}
			else if (hasAlly && (player.getAllyId() == target.getAllyId()))
			{
				return;
			}
			
			// Remove Hide.
			target.getEffectList().stopEffects(AbnormalType.HIDE);
		}
	}
}
