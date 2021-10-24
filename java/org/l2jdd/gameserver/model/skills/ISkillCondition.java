
package org.l2jdd.gameserver.model.skills;

import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;

/**
 * @author NosBit
 */
public interface ISkillCondition
{
	boolean canUse(Creature caster, Skill skill, WorldObject target);
}
