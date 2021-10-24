
package org.l2jdd.gameserver.handler;

import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.TargetType;

/**
 * @author Nik
 */
public interface ITargetTypeHandler
{
	WorldObject getTarget(Creature creature, WorldObject selectedTarget, Skill skill, boolean forceUse, boolean dontMove, boolean sendMessage);
	
	Enum<TargetType> getTargetType();
}
