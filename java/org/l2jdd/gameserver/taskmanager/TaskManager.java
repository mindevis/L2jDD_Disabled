/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jdd.gameserver.taskmanager;

import static org.l2jdd.gameserver.taskmanager.TaskTypes.TYPE_FIXED_SHEDULED;
import static org.l2jdd.gameserver.taskmanager.TaskTypes.TYPE_GLOBAL_TASK;
import static org.l2jdd.gameserver.taskmanager.TaskTypes.TYPE_NONE;
import static org.l2jdd.gameserver.taskmanager.TaskTypes.TYPE_SHEDULED;
import static org.l2jdd.gameserver.taskmanager.TaskTypes.TYPE_SPECIAL;
import static org.l2jdd.gameserver.taskmanager.TaskTypes.TYPE_STARTUP;
import static org.l2jdd.gameserver.taskmanager.TaskTypes.TYPE_TIME;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.commons.database.DatabaseFactory;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.taskmanager.tasks.TaskCleanUp;
import org.l2jdd.gameserver.taskmanager.tasks.TaskOlympiadSave;
import org.l2jdd.gameserver.taskmanager.tasks.TaskRaidPointsReset;
import org.l2jdd.gameserver.taskmanager.tasks.TaskRecom;
import org.l2jdd.gameserver.taskmanager.tasks.TaskRestart;
import org.l2jdd.gameserver.taskmanager.tasks.TaskSevenSignsUpdate;
import org.l2jdd.gameserver.taskmanager.tasks.TaskShutdown;

/**
 * @author Layane
 */
public class TaskManager
{
	protected static final Logger LOGGER = Logger.getLogger(TaskManager.class.getName());
	
	protected static final String[] SQL_STATEMENTS =
	{
		"SELECT id,task,type,last_activation,param1,param2,param3 FROM global_tasks",
		"UPDATE global_tasks SET last_activation=? WHERE id=?",
		"SELECT id FROM global_tasks WHERE task=?",
		"INSERT INTO global_tasks (task,type,last_activation,param1,param2,param3) VALUES(?,?,?,?,?,?)"
	};
	
	private final Map<Integer, Task> _tasks = new HashMap<>();
	protected final List<ExecutedTask> _currentTasks = new ArrayList<>();
	
	public class ExecutedTask implements Runnable
	{
		int id;
		long lastActivation;
		Task task;
		TaskTypes type;
		String[] params;
		ScheduledFuture<?> scheduled;
		
		public ExecutedTask(Task ptask, TaskTypes ptype, ResultSet rset) throws SQLException
		{
			task = ptask;
			type = ptype;
			id = rset.getInt("id");
			lastActivation = rset.getLong("last_activation");
			params = new String[]
			{
				rset.getString("param1"),
				rset.getString("param2"),
				rset.getString("param3")
			};
		}
		
		@Override
		public void run()
		{
			task.onTimeElapsed(this);
			lastActivation = Chronos.currentTimeMillis();
			
			try (Connection con = DatabaseFactory.getConnection())
			{
				final PreparedStatement statement = con.prepareStatement(SQL_STATEMENTS[1]);
				statement.setLong(1, lastActivation);
				statement.setInt(2, id);
				statement.executeUpdate();
				statement.close();
			}
			catch (SQLException e)
			{
				LOGGER.warning("cannot updated the Global Task " + id + ": " + e.getMessage());
			}
			
			if ((type == TYPE_SHEDULED) || (type == TYPE_TIME))
			{
				stopTask();
			}
		}
		
		@Override
		public boolean equals(Object object)
		{
			if (object == null)
			{
				return false;
			}
			return id == ((ExecutedTask) object).id;
		}
		
		@Override
		public int hashCode()
		{
			return id;
		}
		
		public Task getTask()
		{
			return task;
		}
		
		public TaskTypes getType()
		{
			return type;
		}
		
		public int getId()
		{
			return id;
		}
		
		public String[] getParams()
		{
			return params;
		}
		
		public long getLastActivation()
		{
			return lastActivation;
		}
		
		public void stopTask()
		{
			task.onDestroy();
			
			if (scheduled != null)
			{
				scheduled.cancel(true);
			}
			
			_currentTasks.remove(this);
		}
	}
	
	protected TaskManager()
	{
		initializate();
		startAllTasks();
	}
	
	private void initializate()
	{
		registerTask(new TaskCleanUp());
		// registerTask(new TaskJython());
		registerTask(new TaskOlympiadSave());
		registerTask(new TaskRaidPointsReset());
		registerTask(new TaskRecom());
		registerTask(new TaskRestart());
		registerTask(new TaskSevenSignsUpdate());
		registerTask(new TaskShutdown());
	}
	
	public void registerTask(Task task)
	{
		final int key = task.getName().hashCode();
		if (!_tasks.containsKey(key))
		{
			_tasks.put(key, task);
			task.initializate();
		}
	}
	
	private void startAllTasks()
	{
		try (Connection con = DatabaseFactory.getConnection())
		{
			final PreparedStatement statement = con.prepareStatement(SQL_STATEMENTS[0]);
			final ResultSet rset = statement.executeQuery();
			
			while (rset.next())
			{
				final Task task = _tasks.get(rset.getString("task").trim().toLowerCase().hashCode());
				if (task == null)
				{
					continue;
				}
				
				final TaskTypes type = TaskTypes.valueOf(rset.getString("type"));
				if (type != TYPE_NONE)
				{
					final ExecutedTask current = new ExecutedTask(task, type, rset);
					if (launchTask(current))
					{
						_currentTasks.add(current);
					}
				}
			}
			
			rset.close();
			statement.close();
		}
		catch (Exception e)
		{
			LOGGER.warning("Error while loading Global Task table " + e);
		}
	}
	
	private boolean launchTask(ExecutedTask task)
	{
		final TaskTypes type = task.getType();
		if (type == TYPE_STARTUP)
		{
			task.run();
			return false;
		}
		else if (type == TYPE_SHEDULED)
		{
			final long delay = Long.parseLong(task.getParams()[0]);
			task.scheduled = ThreadPool.schedule(task, delay);
			return true;
		}
		else if (type == TYPE_FIXED_SHEDULED)
		{
			final long delay = Long.parseLong(task.getParams()[0]);
			final long interval = Long.parseLong(task.getParams()[1]);
			task.scheduled = ThreadPool.scheduleAtFixedRate(task, delay, interval);
			return true;
		}
		else if (type == TYPE_TIME)
		{
			try
			{
				final Date desired = DateFormat.getInstance().parse(task.getParams()[0]);
				final long diff = desired.getTime() - Chronos.currentTimeMillis();
				if (diff >= 0)
				{
					task.scheduled = ThreadPool.schedule(task, diff);
					return true;
				}
				LOGGER.info("Task " + task.getId() + " is obsoleted.");
			}
			catch (Exception e)
			{
			}
		}
		else if (type == TYPE_SPECIAL)
		{
			final ScheduledFuture<?> result = task.getTask().launchSpecial(task);
			if (result != null)
			{
				task.scheduled = result;
				return true;
			}
		}
		else if (type == TYPE_GLOBAL_TASK)
		{
			final long interval = Long.parseLong(task.getParams()[0]) * 86400000;
			final String[] hour = task.getParams()[1].split(":");
			if (hour.length != 3)
			{
				LOGGER.warning("Task " + task.getId() + " has incorrect parameters");
				return false;
			}
			
			final Calendar check = Calendar.getInstance();
			check.setTimeInMillis(task.getLastActivation() + interval);
			
			final Calendar min = Calendar.getInstance();
			try
			{
				min.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour[0]));
				min.set(Calendar.MINUTE, Integer.parseInt(hour[1]));
				min.set(Calendar.SECOND, Integer.parseInt(hour[2]));
			}
			catch (Exception e)
			{
				LOGGER.warning("Bad parameter on task " + task.getId() + ": " + e.getMessage());
				return false;
			}
			
			long delay = min.getTimeInMillis() - Chronos.currentTimeMillis();
			if (check.after(min) || (delay < 0))
			{
				delay += interval;
			}
			
			task.scheduled = ThreadPool.scheduleAtFixedRate(task, delay, interval);
			return true;
		}
		
		return false;
	}
	
	public static boolean addUniqueTask(String task, TaskTypes type, String param1, String param2, String param3)
	{
		return addUniqueTask(task, type, param1, param2, param3, 0);
	}
	
	public static boolean addUniqueTask(String task, TaskTypes type, String param1, String param2, String param3, long lastActivation)
	{
		boolean output = false;
		
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement statement = con.prepareStatement(SQL_STATEMENTS[2]);
			statement.setString(1, task);
			final ResultSet rset = statement.executeQuery();
			if (!rset.next())
			{
				statement = con.prepareStatement(SQL_STATEMENTS[3]);
				statement.setString(1, task);
				statement.setString(2, type.toString());
				statement.setLong(3, lastActivation);
				statement.setString(4, param1);
				statement.setString(5, param2);
				statement.setString(6, param3);
				statement.execute();
			}
			
			rset.close();
			statement.close();
			
			output = true;
		}
		catch (SQLException e)
		{
			LOGGER.warning("cannot add the unique task: " + e.getMessage());
		}
		
		return output;
	}
	
	public static boolean addTask(String task, TaskTypes type, String param1, String param2, String param3)
	{
		return addTask(task, type, param1, param2, param3, 0);
	}
	
	public static boolean addTask(String task, TaskTypes type, String param1, String param2, String param3, long lastActivation)
	{
		boolean output = false;
		
		try (Connection con = DatabaseFactory.getConnection())
		{
			final PreparedStatement statement = con.prepareStatement(SQL_STATEMENTS[3]);
			statement.setString(1, task);
			statement.setString(2, type.toString());
			statement.setLong(3, lastActivation);
			statement.setString(4, param1);
			statement.setString(5, param2);
			statement.setString(6, param3);
			statement.execute();
			
			statement.close();
			
			output = true;
		}
		catch (SQLException e)
		{
			LOGGER.warning("cannot add the task:  " + e.getMessage());
		}
		return output;
	}
	
	public static TaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final TaskManager INSTANCE = new TaskManager();
	}
}
