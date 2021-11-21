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
package ai.others;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.instance.NpcInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.quest.Quest;
import org.l2jdd.gameserver.network.serverpackets.CreatureSay;

/**
 * @author Mobius
 * @note Based on python script
 */
public class KarulBugbear extends Quest
{
	// NPC
	private static final int KARUL_BUGBEAR = 20600;
	
	private KarulBugbear()
	{
		super(-1, "ai/others");
		
		addAttackId(KARUL_BUGBEAR);
	}
	
	@Override
	public String onAttack(NpcInstance npc, PlayerInstance attacker, int damage, boolean isPet)
	{
		if (npc.isScriptValue(1))
		{
			if (Rnd.get(100) < 4)
			{
				npc.broadcastPacket(new CreatureSay(npc.getObjectId(), ChatType.GENERAL, npc.getName(), "Your rear is practically unguarded!"));
			}
		}
		else
		{
			npc.setScriptValue(1);
			if (Rnd.get(100) < 4)
			{
				npc.broadcastPacket(new CreatureSay(npc.getObjectId(), ChatType.GENERAL, npc.getName(), "Watch your back!"));
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	public static void main(String[] args)
	{
		new KarulBugbear();
	}
}
