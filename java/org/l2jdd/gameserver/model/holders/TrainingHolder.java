
package org.l2jdd.gameserver.model.holders;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.l2jdd.Config;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * @author Sdw
 */
public class TrainingHolder implements Serializable
{
	private final int _objectId;
	private final int _classIndex;
	private final int _level;
	private final long _startTime;
	private long _endTime = -1;
	private static final long TRAINING_DIVIDER = TimeUnit.SECONDS.toMinutes(Config.TRAINING_CAMP_MAX_DURATION);
	
	public TrainingHolder(int objectId, int classIndex, int level, long startTime, long endTime)
	{
		_objectId = objectId;
		_classIndex = classIndex;
		_level = level;
		_startTime = startTime;
		_endTime = endTime;
	}
	
	public long getEndTime()
	{
		return _endTime;
	}
	
	public void setEndTime(long endTime)
	{
		_endTime = endTime;
	}
	
	public int getObjectId()
	{
		return _objectId;
	}
	
	public int getClassIndex()
	{
		return _classIndex;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public long getStartTime()
	{
		return _startTime;
	}
	
	public boolean isTraining()
	{
		return _endTime == -1;
	}
	
	public boolean isValid(PlayerInstance player)
	{
		return Config.TRAINING_CAMP_ENABLE && (player.getObjectId() == _objectId) && (player.getClassIndex() == _classIndex);
	}
	
	public long getElapsedTime()
	{
		return TimeUnit.SECONDS.convert(Chronos.currentTimeMillis() - _startTime, TimeUnit.MILLISECONDS);
	}
	
	public long getRemainingTime()
	{
		return TimeUnit.SECONDS.toMinutes(Config.TRAINING_CAMP_MAX_DURATION - getElapsedTime());
	}
	
	public long getTrainingTime(TimeUnit unit)
	{
		return Math.min(unit.convert(Config.TRAINING_CAMP_MAX_DURATION, TimeUnit.SECONDS), unit.convert(_endTime - _startTime, TimeUnit.MILLISECONDS));
	}
	
	public static long getTrainingDivider()
	{
		return TRAINING_DIVIDER;
	}
}
