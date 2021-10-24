
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Call Party effect implementation.
 * @author Adry_85
 */
public class CallParty extends AbstractEffect
{
	public CallParty(StatSet params)
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
		final Party party = effector.getParty();
		if (party == null)
		{
			return;
		}
		
		for (PlayerInstance partyMember : party.getMembers())
		{
			if (CallPc.checkSummonTargetStatus(partyMember, effector.getActingPlayer()) && (effector != partyMember))
			{
				partyMember.teleToLocation(effector.getLocation(), true);
			}
		}
	}
}
