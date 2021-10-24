
package handlers.targethandlers.affectobject;

import org.l2jdd.gameserver.handler.IAffectObjectHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.targets.AffectObject;

/**
 * Undead enemy npc affect object implementation.
 * @author Nik
 */
public class UndeadRealEnemy implements IAffectObjectHandler
{
	@Override
	public boolean checkAffectedObject(Creature creature, Creature target)
	{
		// You are not an enemy of yourself.
		if (creature == target)
		{
			return false;
		}
		return target.isUndead() && target.isAutoAttackable(creature);
	}
	
	@Override
	public Enum<AffectObject> getAffectObjectType()
	{
		return AffectObject.UNDEAD_REAL_ENEMY;
	}
}
