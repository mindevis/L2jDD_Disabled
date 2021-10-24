
package ai.areas.GainakUnderground.Lailly;

import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.instancemanager.InstanceManager;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.instancezone.Instance;
import org.l2jdd.gameserver.network.NpcStringId;
import org.l2jdd.gameserver.network.serverpackets.NpcSay;

import ai.AbstractNpcAI;

/**
 * Lailly AI.
 * @author Stayway
 */
public class Lailly extends AbstractNpcAI
{
	// NPCs
	private static final int LAILLY = 34181;
	// Instances
	private static final int INSTANCE_TAUTI = 261;
	private static final int INSTANCE_KELBIM = 262;
	private static final int INSTANCE_FREYA = 263;
	
	private Lailly()
	{
		addSpawnId(LAILLY);
		addFirstTalkId(LAILLY);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "34181.html":
			{
				htmltext = event;
				break;
			}
			case "spam_text":
			{
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), ChatType.NPC_GENERAL, npc.getId(), NpcStringId.READY_TO_LISTEN_TO_A_STORY_COME_NOW));
				break;
			}
			case "okay":
			{
				final Instance instance = InstanceManager.getInstance().getPlayerInstance(player, false);
				if ((instance != null) && (instance.getEndTime() > Chronos.currentTimeMillis()))
				{
					switch (instance.getTemplateId())
					{
						case INSTANCE_TAUTI:
						case INSTANCE_KELBIM:
						case INSTANCE_FREYA:
						{
							player.teleToLocation(instance.getEnterLocation(), instance);
							break;
						}
					}
					break;
				}
				htmltext = "34181-1.html";
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		startQuestTimer("spam_text", 180000, npc, null, true);
		return super.onSpawn(npc);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "34181.html";
	}
	
	public static void main(String[] args)
	{
		new Lailly();
	}
}