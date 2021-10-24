
package handlers.targethandlers.affectscope;

import java.util.function.Consumer;

import org.l2jdd.gameserver.handler.AffectObjectHandler;
import org.l2jdd.gameserver.handler.IAffectObjectHandler;
import org.l2jdd.gameserver.handler.IAffectScopeHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.AffectScope;
import org.l2jdd.gameserver.util.Util;

/**
 * @author Nik
 */
public class SummonExceptMaster implements IAffectScopeHandler
{
	@Override
	public void forEachAffected(Creature creature, WorldObject target, Skill skill, Consumer<? super WorldObject> action)
	{
		final IAffectObjectHandler affectObject = AffectObjectHandler.getInstance().getHandler(skill.getAffectObject());
		final int affectRange = skill.getAffectRange();
		final int affectLimit = skill.getAffectLimit();
		
		if (target.isPlayable())
		{
			final PlayerInstance player = target.getActingPlayer();
			//@formatter:off
			player.getServitorsAndPets().stream()
			.filter(c -> !c.isDead())
			.filter(c -> (affectRange <= 0) || Util.checkIfInRange(affectRange, c, target, true))
			.filter(c -> (affectObject == null) || affectObject.checkAffectedObject(creature, c))
			.limit(affectLimit > 0 ? affectLimit : Long.MAX_VALUE)
			.forEach(action);
			//@formatter:on
		}
	}
	
	@Override
	public Enum<AffectScope> getAffectScopeType()
	{
		return AffectScope.SUMMON_EXCEPT_MASTER;
	}
}
