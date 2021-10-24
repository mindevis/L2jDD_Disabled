
package handlers.conditions;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.conditions.ICondition;

/**
 * @author Sdw
 */
public class PlayerLevelCondition implements ICondition
{
	private final int _minLevel;
	private final int _maxLevel;
	
	public PlayerLevelCondition(StatSet params)
	{
		_minLevel = params.getInt("minLevel");
		_maxLevel = params.getInt("maxLevel");
	}
	
	@Override
	public boolean test(Creature creature, WorldObject object)
	{
		return creature.isPlayer() && (creature.getLevel() >= _minLevel) && (creature.getLevel() < _maxLevel);
	}
}
