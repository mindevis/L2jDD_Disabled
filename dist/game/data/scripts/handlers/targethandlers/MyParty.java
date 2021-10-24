
package handlers.targethandlers;

import org.l2jdd.gameserver.handler.ITargetTypeHandler;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.TargetType;

/**
 * Something like target self, but party. Used in aura skills.
 * @author Nik
 */
public class MyParty implements ITargetTypeHandler
{
	@Override
	public Enum<TargetType> getTargetType()
	{
		return TargetType.MY_PARTY;
	}
	
	@Override
	public WorldObject getTarget(Creature creature, WorldObject selectedTarget, Skill skill, boolean forceUse, boolean dontMove, boolean sendMessage)
	{
		if ((selectedTarget != null) && selectedTarget.isPlayer() && (selectedTarget != creature))
		{
			final Party party = creature.getParty();
			final Party targetParty = selectedTarget.getActingPlayer().getParty();
			if ((party != null) && (targetParty != null) && (party.getLeaderObjectId() == targetParty.getLeaderObjectId()))
			{
				return selectedTarget;
			}
		}
		return creature;
	}
}
