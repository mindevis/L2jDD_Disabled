
package handlers.conditions;

import java.util.List;

import org.l2jdd.gameserver.enums.CategoryType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.conditions.ICondition;

/**
 * @author Sdw
 */
public class CategoryTypeCondition implements ICondition
{
	private final List<CategoryType> _categoryTypes;
	
	public CategoryTypeCondition(StatSet params)
	{
		_categoryTypes = params.getEnumList("category", CategoryType.class);
	}
	
	@Override
	public boolean test(Creature creature, WorldObject target)
	{
		return _categoryTypes.stream().anyMatch(creature::isInCategory);
	}
}
