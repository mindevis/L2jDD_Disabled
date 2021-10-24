
package handlers.targethandlers.affectscope;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.handler.AffectObjectHandler;
import org.l2jdd.gameserver.handler.IAffectObjectHandler;
import org.l2jdd.gameserver.handler.IAffectScopeHandler;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.AffectScope;
import org.l2jdd.gameserver.model.skills.targets.TargetType;
import org.l2jdd.gameserver.util.Util;

/**
 * Square affect scope implementation (actually more like a rectangle).
 * @author Nik
 */
public class Square implements IAffectScopeHandler
{
	@Override
	public void forEachAffected(Creature creature, WorldObject target, Skill skill, Consumer<? super WorldObject> action)
	{
		final IAffectObjectHandler affectObject = AffectObjectHandler.getInstance().getHandler(skill.getAffectObject());
		final int squareStartAngle = skill.getFanRange()[1];
		final int squareLength = skill.getFanRange()[2];
		final int squareWidth = skill.getFanRange()[3];
		final int radius = (int) Math.sqrt((squareLength * squareLength) + (squareWidth * squareWidth));
		final int affectLimit = skill.getAffectLimit();
		
		final int rectX = creature.getX();
		final int rectY = creature.getY() - (squareWidth / 2);
		final double heading = Math.toRadians(squareStartAngle + Util.convertHeadingToDegree(creature.getHeading()));
		final double cos = Math.cos(-heading);
		final double sin = Math.sin(-heading);
		
		// Target checks.
		final TargetType targetType = skill.getTargetType();
		final AtomicInteger affected = new AtomicInteger(0);
		final Predicate<Creature> filter = c ->
		{
			if ((affectLimit > 0) && (affected.get() >= affectLimit))
			{
				return false;
			}
			if (c.isDead() && (targetType != TargetType.NPC_BODY) && (targetType != TargetType.PC_BODY))
			{
				return false;
			}
			
			// Check if inside square.
			final int xp = c.getX() - creature.getX();
			final int yp = c.getY() - creature.getY();
			final int xr = (int) ((creature.getX() + (xp * cos)) - (yp * sin));
			final int yr = (int) (creature.getY() + (xp * sin) + (yp * cos));
			if ((xr > rectX) && (xr < (rectX + squareLength)) && (yr > rectY) && (yr < (rectY + squareWidth)))
			{
				if ((c != target) && (affectObject != null) && !affectObject.checkAffectedObject(creature, c))
				{
					return false;
				}
				if (!GeoEngine.getInstance().canSeeTarget(creature, c))
				{
					return false;
				}
				
				affected.incrementAndGet();
				return true;
			}
			
			return false;
		};
		
		// Add object of origin since its skipped in the forEachVisibleObjectInRange method.
		if (filter.test(creature))
		{
			action.accept(creature);
		}
		
		// Check and add targets.
		World.getInstance().forEachVisibleObjectInRange(creature, Creature.class, radius, c ->
		{
			if (filter.test(c))
			{
				action.accept(c);
			}
		});
	}
	
	@Override
	public Enum<AffectScope> getAffectScopeType()
	{
		return AffectScope.SQUARE;
	}
}
