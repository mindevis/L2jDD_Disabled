
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * GM Effect: Call Target's Party around target effect implementation.
 * @author Nik
 */
public class CallTargetParty extends AbstractEffect
{
	public CallTargetParty(StatSet params)
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
		final PlayerInstance player = effected.getActingPlayer();
		if ((player == null))
		{
			return;
		}
		
		final Party party = player.getParty();
		if (party != null)
		{
			for (PlayerInstance member : party.getMembers())
			{
				if ((member != player) && CallPc.checkSummonTargetStatus(member, effector.getActingPlayer()))
				{
					member.teleToLocation(player.getLocation(), true);
				}
			}
		}
	}
}
