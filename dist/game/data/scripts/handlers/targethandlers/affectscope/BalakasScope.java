
package handlers.targethandlers.affectscope;

import java.util.function.Consumer;

import org.l2jdd.gameserver.handler.IAffectScopeHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.AffectScope;

/**
 * TODO: Valakas affect scope implementation.
 * @author Nik
 */
public class BalakasScope implements IAffectScopeHandler
{
	@Override
	public void forEachAffected(Creature creature, WorldObject target, Skill skill, Consumer<? super WorldObject> action)
	{
		// TODO Unknown affect scope.
	}
	
	@Override
	public Enum<AffectScope> getAffectScopeType()
	{
		return AffectScope.BALAKAS_SCOPE;
	}
}
