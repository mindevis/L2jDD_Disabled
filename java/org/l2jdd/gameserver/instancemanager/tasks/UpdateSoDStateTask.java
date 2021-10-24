
package org.l2jdd.gameserver.instancemanager.tasks;

import org.l2jdd.gameserver.instancemanager.GraciaSeedsManager;

/**
 * Task which updates Seed of Destruction state.
 * @author xban1x
 */
public class UpdateSoDStateTask implements Runnable
{
	@Override
	public void run()
	{
		final GraciaSeedsManager manager = GraciaSeedsManager.getInstance();
		manager.setSoDState(1, true);
		manager.updateSodState();
	}
}
