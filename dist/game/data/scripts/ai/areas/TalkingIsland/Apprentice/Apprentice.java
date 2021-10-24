
package ai.areas.TalkingIsland.Apprentice;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Apprentice AI.
 * @author St3eT
 */
public class Apprentice extends AbstractNpcAI
{
	// NPCs
	private static final int APPRENTICE = 33124;
	// Skill
	private static final SkillHolder KUKURU = new SkillHolder(9204, 1); // Kukuru
	
	private Apprentice()
	{
		addSpawnId(APPRENTICE);
		addStartNpc(APPRENTICE);
		addTalkId(APPRENTICE);
		addFirstTalkId(APPRENTICE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("rideKukuru"))
		{
			if (!player.isTransformed())
			{
				KUKURU.getSkill().applyEffects(npc, player);
			}
			else
			{
				npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.YOU_CAN_T_RIDE_A_KUKURI_NOW);
			}
		}
		else if (event.equals("SPAM_TEXT") && (npc != null))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.TRY_RIDING_A_KUKURI, 1000);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		startQuestTimer("SPAM_TEXT", 12000, npc, null, true);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Apprentice();
	}
}
