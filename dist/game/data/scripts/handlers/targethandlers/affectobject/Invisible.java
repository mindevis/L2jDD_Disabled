
package handlers.targethandlers.affectobject;

import org.l2jdd.gameserver.handler.IAffectObjectHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.targets.AffectObject;

/**
 * Invisible affect object implementation.
 * @author Nik
 */
public class Invisible implements IAffectObjectHandler
{
	@Override
	public boolean checkAffectedObject(Creature creature, Creature target)
	{
		return target.isInvisible();
	}
	
	@Override
	public Enum<AffectObject> getAffectObjectType()
	{
		return AffectObject.INVISIBLE;
	}
}
