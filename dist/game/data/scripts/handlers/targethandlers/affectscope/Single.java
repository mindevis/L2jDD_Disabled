
package handlers.targethandlers.affectscope;

import java.util.function.Consumer;

import org.l2jdd.gameserver.handler.AffectObjectHandler;
import org.l2jdd.gameserver.handler.IAffectObjectHandler;
import org.l2jdd.gameserver.handler.IAffectScopeHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.AffectScope;
import org.l2jdd.gameserver.model.skills.targets.TargetType;

/**
 * Single target affect scope implementation.
 * @author Nik
 */
public class Single implements IAffectScopeHandler
{
	@Override
	public void forEachAffected(Creature creature, WorldObject target, Skill skill, Consumer<? super WorldObject> action)
	{
		final IAffectObjectHandler affectObject = AffectObjectHandler.getInstance().getHandler(skill.getAffectObject());
		
		if (target.isCreature())
		{
			if (skill.getTargetType() == TargetType.GROUND)
			{
				action.accept(creature); // Return yourself to mark that effects can use your current skill's world position.
			}
			if (((affectObject == null) || affectObject.checkAffectedObject(creature, (Creature) target)))
			{
				action.accept(target); // Return yourself to mark that effects can use your current skill's world position.
			}
		}
		else if (target.isItem())
		{
			action.accept(target); // Return yourself to mark that effects can use your current skill's world position.
		}
	}
	
	@Override
	public Enum<AffectScope> getAffectScopeType()
	{
		return AffectScope.SINGLE;
	}
}
