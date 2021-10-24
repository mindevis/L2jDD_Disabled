
package org.l2jdd.gameserver.handler;

import java.util.function.Consumer;

import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.AffectScope;

/**
 * @author Nik
 */
public interface IAffectScopeHandler
{
	void forEachAffected(Creature creature, WorldObject target, Skill skill, Consumer<? super WorldObject> action);
	
	Enum<AffectScope> getAffectScopeType();
}
