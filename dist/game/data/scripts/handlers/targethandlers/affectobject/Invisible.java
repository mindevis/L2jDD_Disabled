/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.targethandlers.affectobject;

import org.l2jdd.gameserver.handler.IAffectObjectHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.skills.targets.AffectObject;

/**
 * Invisible affect object implementation.
 * @author Nik
 */
public class Invisible implements IAffectObjectHandler
{
	@Override
	public boolean checkAffectedObject(Creature creature, Creature target)
	{
		return target.isInvisible();
	}
	
	@Override
	public Enum<AffectObject> getAffectObjectType()
	{
		return AffectObject.INVISIBLE;
	}
}
