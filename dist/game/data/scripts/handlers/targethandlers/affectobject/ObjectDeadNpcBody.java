
package handlers.targethandlers.affectobject;

import org.l2jdd.gameserver.handler.IAffectObjectHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.targets.AffectObject;

/**
 * @author Nik
 */
public class ObjectDeadNpcBody implements IAffectObjectHandler
{
	@Override
	public boolean checkAffectedObject(Creature creature, Creature target)
	{
		if (creature == target)
		{
			return false;
		}
		return target.isNpc() && target.isDead();
	}
	
	@Override
	public Enum<AffectObject> getAffectObjectType()
	{
		return AffectObject.OBJECT_DEAD_NPC_BODY;
	}
}
