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
package org.l2jdd.gameserver.handler.skillhandlers;

import java.util.List;

import org.l2jdd.gameserver.handler.ISkillHandler;
import org.l2jdd.gameserver.model.Skill;
import org.l2jdd.gameserver.model.Skill.SkillType;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;

/**
 * @author Forsaiken
 */
public class GiveSp implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.GIVE_SP
	};
	
	@Override
	public void useSkill(Creature creature, Skill skill, List<Creature> targets)
	{
		for (WorldObject obj : targets)
		{
			final Creature target = (Creature) obj;
			if (target != null)
			{
				final int spToAdd = (int) skill.getPower();
				target.addExpAndSp(0, spToAdd);
			}
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}
