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
import java.util.logging.Logger;

import org.l2jdd.gameserver.handler.ISkillHandler;
import org.l2jdd.gameserver.model.Skill;
import org.l2jdd.gameserver.model.Skill.SkillType;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

/**
 * @author _drunk_ TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class DrainSoul implements ISkillHandler
{
	private static final Logger LOGGER = Logger.getLogger(DrainSoul.class.getName());
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.DRAIN_SOUL
	};
	
	@Override
	public void useSkill(Creature creature, Skill skill, List<Creature> targets)
	{
		if (!(creature instanceof PlayerInstance))
		{
			return;
		}
		
		final List<Creature> targetList = skill.getTargetList(creature);
		if (targetList == null)
		{
			return;
		}
		
		LOGGER.info("Soul Crystal casting succeded.");
		
		// This is just a dummy skill handler for the soul crystal skill, since the Soul Crystal item handler already does everything.
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}
