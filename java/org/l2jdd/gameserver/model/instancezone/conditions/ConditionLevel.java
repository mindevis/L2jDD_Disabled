
package org.l2jdd.gameserver.model.instancezone.conditions;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.instancezone.InstanceTemplate;
import org.l2jdd.gameserver.network.SystemMessageId;

/**
 * Instance level condition
 * @author malyelfik
 */
public class ConditionLevel extends Condition
{
	private final int _min;
	private final int _max;
	
	public ConditionLevel(InstanceTemplate template, StatSet parameters, boolean onlyLeader, boolean showMessageAndHtml)
	{
		super(template, parameters, onlyLeader, showMessageAndHtml);
		// Load params
		_min = Math.min(Config.PLAYER_MAXIMUM_LEVEL, parameters.getInt("min", 1));
		_max = Math.min(Config.PLAYER_MAXIMUM_LEVEL, parameters.getInt("max", Integer.MAX_VALUE));
		// Set message
		setSystemMessage(SystemMessageId.C1_S_LEVEL_DOES_NOT_CORRESPOND_TO_THE_REQUIREMENTS_FOR_ENTRY, (msg, player) -> msg.addString(player.getName()));
	}
	
	@Override
	protected boolean test(PlayerInstance player, Npc npc)
	{
		return (player.getLevel() >= _min) && (player.getLevel() <= _max);
	}
}
