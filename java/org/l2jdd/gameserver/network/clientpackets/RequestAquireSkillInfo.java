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
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.SkillTable;
import org.l2jdd.gameserver.data.sql.SkillSpellbookTable;
import org.l2jdd.gameserver.data.sql.SkillTreeTable;
import org.l2jdd.gameserver.model.PledgeSkillLearn;
import org.l2jdd.gameserver.model.Skill;
import org.l2jdd.gameserver.model.SkillLearn;
import org.l2jdd.gameserver.model.actor.instance.FolkInstance;
import org.l2jdd.gameserver.model.actor.instance.NpcInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.serverpackets.AquireSkillInfo;

public class RequestAquireSkillInfo implements IClientIncomingPacket
{
	private int _id;
	private int _level;
	private int _skillType;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_id = packet.readD();
		_level = packet.readD();
		_skillType = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final FolkInstance trainer = player.getLastFolkNPC();
		if (trainer == null)
		{
			return;
		}
		
		if (!player.isGM() && !player.isInsideRadius2D(trainer, NpcInstance.INTERACTION_DISTANCE))
		{
			return;
		}
		
		boolean canteach = false;
		final Skill skill = SkillTable.getInstance().getSkill(_id, _level);
		if (skill == null)
		{
			return;
		}
		
		if (_skillType == 0)
		{
			if (!trainer.getTemplate().canTeach(player.getSkillLearningClassId()))
			{
				return; // cheater
			}
			
			final SkillLearn[] skills = SkillTreeTable.getInstance().getAvailableSkills(player, player.getSkillLearningClassId());
			for (SkillLearn s : skills)
			{
				if ((s.getId() == _id) && (s.getLevel() == _level))
				{
					canteach = true;
					break;
				}
			}
			
			if (!canteach)
			{
				return; // cheater
			}
			
			final int requiredSp = SkillTreeTable.getInstance().getSkillCost(player, skill);
			final AquireSkillInfo asi = new AquireSkillInfo(skill.getId(), skill.getLevel(), requiredSp, 0);
			int spbId = -1;
			if (Config.DIVINE_SP_BOOK_NEEDED && (skill.getId() == Skill.SKILL_DIVINE_INSPIRATION))
			{
				spbId = SkillSpellbookTable.getInstance().getBookForSkill(skill, _level);
			}
			else if (Config.SP_BOOK_NEEDED && (skill.getLevel() == 1))
			{
				spbId = SkillSpellbookTable.getInstance().getBookForSkill(skill);
			}
			
			if (spbId > -1)
			{
				asi.addRequirement(99, spbId, 1, 50);
			}
			
			player.sendPacket(asi);
		}
		else if (_skillType == 2)
		{
			int requiredRep = 0;
			int itemId = 0;
			final PledgeSkillLearn[] skills = SkillTreeTable.getInstance().getAvailablePledgeSkills(player);
			for (PledgeSkillLearn s : skills)
			{
				if ((s.getId() == _id) && (s.getLevel() == _level))
				{
					canteach = true;
					requiredRep = s.getRepCost();
					itemId = s.getItemId();
					break;
				}
			}
			
			if (!canteach)
			{
				return; // cheater
			}
			
			final AquireSkillInfo asi = new AquireSkillInfo(skill.getId(), skill.getLevel(), requiredRep, 2);
			if (Config.LIFE_CRYSTAL_NEEDED)
			{
				asi.addRequirement(1, itemId, 1, 0);
			}
			player.sendPacket(asi);
		}
		else
		// Common Skills
		{
			int costid = 0;
			int costcount = 0;
			int spcost = 0;
			
			final SkillLearn[] skillsc = SkillTreeTable.getInstance().getAvailableSkills(player);
			for (SkillLearn s : skillsc)
			{
				final Skill sk = SkillTable.getInstance().getSkill(s.getId(), s.getLevel());
				if ((sk == null) || (sk != skill))
				{
					continue;
				}
				
				canteach = true;
				costid = s.getIdCost();
				costcount = s.getCostCount();
				spcost = s.getSpCost();
			}
			
			final AquireSkillInfo asi = new AquireSkillInfo(skill.getId(), skill.getLevel(), spcost, 1);
			asi.addRequirement(4, costid, costcount, 0);
			player.sendPacket(asi);
		}
	}
}
