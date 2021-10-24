
package handlers.targethandlers.affectobject;

import org.l2jdd.gameserver.data.xml.CategoryData;
import org.l2jdd.gameserver.enums.CategoryType;
import org.l2jdd.gameserver.handler.IAffectObjectHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.targets.AffectObject;

/**
 * @author Nik
 */
public class WyvernObject implements IAffectObjectHandler
{
	@Override
	public boolean checkAffectedObject(Creature creature, Creature target)
	{
		// TODO Check if this is proper. Not sure if this is the object we are looking for.
		return CategoryData.getInstance().isInCategory(CategoryType.WYVERN_GROUP, target.getId());
	}
	
	@Override
	public Enum<AffectObject> getAffectObjectType()
	{
		return AffectObject.WYVERN_OBJECT;
	}
}
