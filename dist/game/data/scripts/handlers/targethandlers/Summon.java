
package handlers.targethandlers;

import org.l2jdd.gameserver.handler.ITargetTypeHandler;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.TargetType;

/**
 * Target automatically one of my summons.
 * @author Nik
 */
public class Summon implements ITargetTypeHandler
{
	@Override
	public Enum<TargetType> getTargetType()
	{
		return TargetType.SUMMON;
	}
	
	@Override
	public WorldObject getTarget(Creature creature, WorldObject selectedTarget, Skill skill, boolean forceUse, boolean dontMove, boolean sendMessage)
	{
		if (creature.isPlayer() && creature.hasSummon())
		{
			return creature.getActingPlayer().getAnyServitor();
		}
		return creature.getPet();
	}
}
