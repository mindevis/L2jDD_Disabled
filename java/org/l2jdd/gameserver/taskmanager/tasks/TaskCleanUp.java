
package org.l2jdd.gameserver.taskmanager.tasks;

import org.l2jdd.gameserver.taskmanager.Task;
import org.l2jdd.gameserver.taskmanager.TaskManager.ExecutedTask;

/**
 * @author Tempy
 */
public class TaskCleanUp extends Task
{
	private static final String NAME = "clean_up";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		System.runFinalization();
		System.gc();
	}
}
