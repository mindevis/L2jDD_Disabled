
package org.l2jdd.gameserver.model.actor.request;

import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * @author Mobius
 */
public class RewardRequest extends AbstractRequest
{
	public RewardRequest(PlayerInstance player)
	{
		super(player);
	}
	
	@Override
	public boolean isUsing(int objectId)
	{
		return false;
	}
}
