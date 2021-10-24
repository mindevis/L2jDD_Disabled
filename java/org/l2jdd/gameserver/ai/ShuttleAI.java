
package org.l2jdd.gameserver.ai;

import org.l2jdd.gameserver.model.actor.instance.ShuttleInstance;
import org.l2jdd.gameserver.network.serverpackets.shuttle.ExShuttleMove;

/**
 * @author UnAfraid
 */
public class ShuttleAI extends VehicleAI
{
	public ShuttleAI(ShuttleInstance shuttle)
	{
		super(shuttle);
	}
	
	@Override
	public void moveTo(int x, int y, int z)
	{
		if (!_actor.isMovementDisabled())
		{
			_clientMoving = true;
			_actor.moveToLocation(x, y, z, 0);
			_actor.broadcastPacket(new ExShuttleMove(getActor(), x, y, z));
		}
	}
	
	@Override
	public ShuttleInstance getActor()
	{
		return (ShuttleInstance) _actor;
	}
}
