
package org.l2jdd.gameserver.instancemanager.tasks;

import org.l2jdd.gameserver.instancemanager.GrandBossManager;

/**
 * @author xban1x
 */
public class GrandBossManagerStoreTask implements Runnable
{
	@Override
	public void run()
	{
		GrandBossManager.getInstance().storeMe();
	}
}