
package handlers.targethandlers.affectobject;

import org.l2jdd.gameserver.handler.IAffectObjectHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.targets.AffectObject;

/**
 * @author Nik
 */
public class All implements IAffectObjectHandler
{
	@Override
	public boolean checkAffectedObject(Creature creature, Creature target)
	{
		return true;
	}
	
	@Override
	public Enum<AffectObject> getAffectObjectType()
	{
		return AffectObject.ALL;
	}
}
