
package org.l2jdd.gameserver.model.instancezone.conditions;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.instancezone.InstanceTemplate;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * Instance party condition
 * @author malyelfik
 */
public class ConditionParty extends Condition
{
	public ConditionParty(InstanceTemplate template, StatSet parameters, boolean onlyLeader, boolean showMessageAndHtml)
	{
		super(template, parameters, true, showMessageAndHtml);
		setSystemMessage(SystemMessageId.YOU_ARE_NOT_CURRENTLY_IN_A_PARTY_SO_YOU_CANNOT_ENTER);
	}
	
	@Override
	public boolean test(PlayerInstance player, Npc npc)
	{
		return player.isInParty();
	}
}
