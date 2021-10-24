
package handlers.targethandlers.affectscope;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.l2jdd.gameserver.handler.AffectObjectHandler;
import org.l2jdd.gameserver.handler.IAffectObjectHandler;
import org.l2jdd.gameserver.handler.IAffectScopeHandler;
import org.l2jdd.gameserver.model.CommandChannel;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Playable;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.AffectScope;

/**
 * Dead command channel/party affect scope implementation.
 * @author Nik
 */
public class DeadUnion implements IAffectScopeHandler
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
			final Party party = player.getParty();
			final CommandChannel commandChannel = party != null ? party.getCommandChannel() : null;
			
			// Create the target filter.
			final AtomicInteger affected = new AtomicInteger(0);
			final Predicate<Playable> filter = plbl ->
			{
				if ((affectLimit > 0) && (affected.get() >= affectLimit))
				{
					return false;
				}
				
				final PlayerInstance p = plbl.getActingPlayer();
				if ((p == null) || !p.isDead())
				{
					return false;
				}
				
				if (p != player)
				{
					final Party targetParty = p.getParty();
					if ((party == null) || (targetParty == null))
					{
						return false;
					}
					
					if (party.getLeaderObjectId() != targetParty.getLeaderObjectId())
					{
						final CommandChannel targetCommandChannel = targetParty.getCommandChannel();
						if ((commandChannel == null) || (targetCommandChannel == null) || (commandChannel.getLeaderObjectId() != targetCommandChannel.getLeaderObjectId()))
						{
							return false;
						}
					}
				}
				
				if ((affectObject != null) && !affectObject.checkAffectedObject(creature, p))
				{
					return false;
				}
				
				affected.incrementAndGet();
				return true;
			};
			
			// Affect object of origin since its skipped in the forEachVisibleObjectInRange method.
			if (filter.test((Playable) target))
			{
				action.accept(target);
			}
			
			// Check and add targets.
			World.getInstance().forEachVisibleObjectInRange(target, Playable.class, affectRange, c ->
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
		return AffectScope.DEAD_UNION;
	}
}
