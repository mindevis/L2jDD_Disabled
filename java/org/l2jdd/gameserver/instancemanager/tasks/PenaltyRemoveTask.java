
package org.l2jdd.gameserver.instancemanager.tasks;

import org.l2jdd.gameserver.instancemanager.HandysBlockCheckerManager;

/**
 * Handys Block Checker penalty remove.
 * @author xban1x
 */
public class PenaltyRemoveTask implements Runnable
{
	private final int _objectId;
	
	public PenaltyRemoveTask(int id)
	{
		_objectId = id;
	}
	
	@Override
	public void run()
	{
		HandysBlockCheckerManager.getInstance().removePenalty(_objectId);
	}
}
