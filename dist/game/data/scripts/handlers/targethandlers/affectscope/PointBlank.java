
package handlers.targethandlers.affectscope;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.handler.AffectObjectHandler;
import org.l2jdd.gameserver.handler.IAffectObjectHandler;
import org.l2jdd.gameserver.handler.IAffectScopeHandler;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.AffectObject;
import org.l2jdd.gameserver.model.skills.targets.AffectScope;
import org.l2jdd.gameserver.model.skills.targets.TargetType;

/**
 * Point Blank affect scope implementation. Gathers targets in specific radius except initial target.
 * @author Nik
 */
public class PointBlank implements IAffectScopeHandler
{
	@Override
	public void forEachAffected(Creature creature, WorldObject target, Skill skill, Consumer<? super WorldObject> action)
	{
		final IAffectObjectHandler affectObject = AffectObjectHandler.getInstance().getHandler(skill.getAffectObject());
		final int affectRange = skill.getAffectRange();
		final int affectLimit = skill.getAffectLimit();
		
		// Target checks.
		final AtomicInteger affected = new AtomicInteger(0);
		final Predicate<Creature> filter = c ->
		{
			if ((affectLimit > 0) && (affected.get() >= affectLimit))
			{
				return false;
			}
			if (affectObject != null)
			{
				if (c.isDead() && (skill.getAffectObject() != AffectObject.OBJECT_DEAD_NPC_BODY))
				{
					return false;
				}
				if (!affectObject.checkAffectedObject(creature, c))
				{
					return false;
				}
			}
			if (!GeoEngine.getInstance().canSeeTarget(target, c))
			{
				return false;
			}
			
			affected.incrementAndGet();
			return true;
		};
		
		// Check and add targets.
		if (skill.getTargetType() == TargetType.GROUND)
		{
			if (creature.isPlayable())
			{
				final Location worldPosition = creature.getActingPlayer().getCurrentSkillWorldPosition();
				if (worldPosition != null)
				{
					World.getInstance().forEachVisibleObjectInRange(creature, Creature.class, (int) (affectRange + creature.calculateDistance2D(worldPosition)), c ->
					{
						if (!c.isInsideRadius3D(worldPosition, affectRange))
						{
							return;
						}
						if (filter.test(c))
						{
							action.accept(c);
						}
					});
				}
			}
		}
		else
		{
			World.getInstance().forEachVisibleObjectInRange(target, Creature.class, affectRange, c ->
			{
				if (filter.test(c))
				{
					action.accept(c);
				}
			});
		}
	}
	
	@Override
	public Enum<AffectScope> getAffectScopeType()
	{
		return AffectScope.POINT_BLANK;
	}
}
