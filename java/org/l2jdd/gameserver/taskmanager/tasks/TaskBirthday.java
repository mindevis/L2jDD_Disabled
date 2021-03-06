
package org.l2jdd.gameserver.taskmanager.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import org.l2jdd.Config;
import org.l2jdd.commons.database.DatabaseFactory;
import org.l2jdd.gameserver.data.sql.CharNameTable;
import org.l2jdd.gameserver.enums.MailType;
import org.l2jdd.gameserver.instancemanager.MailManager;
import org.l2jdd.gameserver.model.Message;
import org.l2jdd.gameserver.model.itemcontainer.Mail;
import org.l2jdd.gameserver.taskmanager.Task;
import org.l2jdd.gameserver.taskmanager.TaskManager;
import org.l2jdd.gameserver.taskmanager.TaskManager.ExecutedTask;
import org.l2jdd.gameserver.taskmanager.TaskTypes;
import org.l2jdd.gameserver.util.Util;

/**
 * @author Nyaran
 */
public class TaskBirthday extends Task
{
	private static final String NAME = "birthday";
	private static final String QUERY = "SELECT charId, createDate FROM characters WHERE createDate LIKE ?";
	private static final Calendar TODAY = Calendar.getInstance();
	private int _count = 0;
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		final Calendar lastExecDate = Calendar.getInstance();
		final long lastActivation = task.getLastActivation();
		if (lastActivation > 0)
		{
			lastExecDate.setTimeInMillis(lastActivation);
		}
		
		final String rangeDate = "[" + Util.getDateString(lastExecDate.getTime()) + "] - [" + Util.getDateString(TODAY.getTime()) + "]";
		for (; !TODAY.before(lastExecDate); lastExecDate.add(Calendar.DATE, 1))
		{
			checkBirthday(lastExecDate.get(Calendar.YEAR), lastExecDate.get(Calendar.MONTH), lastExecDate.get(Calendar.DATE));
		}
		
		LOGGER.info("BirthdayManager: " + _count + " gifts sent. " + rangeDate);
	}
	
	private void checkBirthday(int year, int month, int day)
	{
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement(QUERY))
		{
			statement.setString(1, "%-" + getNum(month + 1) + "-" + getNum(day));
			try (ResultSet rset = statement.executeQuery())
			{
				while (rset.next())
				{
					final int playerId = rset.getInt("charId");
					final Calendar createDate = Calendar.getInstance();
					createDate.setTime(rset.getDate("createDate"));
					
					final int age = year - createDate.get(Calendar.YEAR);
					if (age <= 0)
					{
						continue;
					}
					
					String text = Config.ALT_BIRTHDAY_MAIL_TEXT;
					if (text.contains("$c1"))
					{
						text = text.replace("$c1", CharNameTable.getInstance().getNameById(playerId));
					}
					if (text.contains("$s1"))
					{
						text = text.replace("$s1", String.valueOf(age));
					}
					
					final Message msg = new Message(playerId, Config.ALT_BIRTHDAY_MAIL_SUBJECT, text, MailType.BIRTHDAY);
					final Mail attachments = msg.createAttachments();
					attachments.addItem("Birthday", Config.ALT_BIRTHDAY_GIFT, 1, null, null);
					MailManager.getInstance().sendMessage(msg);
					_count++;
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.log(Level.WARNING, "Error checking birthdays. ", e);
		}
		
		// If character birthday is 29-Feb and year isn't leap, send gift on 28-feb
		final GregorianCalendar calendar = new GregorianCalendar();
		if ((month == Calendar.FEBRUARY) && (day == 28) && !calendar.isLeapYear(TODAY.get(Calendar.YEAR)))
		{
			checkBirthday(year, Calendar.FEBRUARY, 29);
		}
	}
	
	/**
	 * @param num the number to format.
	 * @return the formatted number starting with a 0 if it is lower or equal than 10.
	 */
	private String getNum(int num)
	{
		return (num <= 9) ? "0" + num : String.valueOf(num);
	}
	
	@Override
	public void initializate()
	{
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_GLOBAL_TASK, "1", "06:30:00", "");
	}
}
