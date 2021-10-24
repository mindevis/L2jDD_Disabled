
package org.l2jdd.gameserver.model.instancezone.conditions;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.instancezone.InstanceTemplate;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * Command channel condition
 * @author malyelfik
 */
public class ConditionCommandChannel extends Condition
{
	public ConditionCommandChannel(InstanceTemplate template, StatSet parameters, boolean onlyLeader, boolean showMessageAndHtml)
	{
		super(template, parameters, true, showMessageAndHtml);
		setSystemMessage(SystemMessageId.YOU_CANNOT_ENTER_BECAUSE_YOU_ARE_NOT_ASSOCIATED_WITH_THE_CURRENT_COMMAND_CHANNEL);
	}
	
	@Override
	public boolean test(PlayerInstance player, Npc npc)
	{
		return player.isInCommandChannel();
	}
}
