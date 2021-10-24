
package instances.EvasHiddenSpace;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.instancezone.Instance;
import org.l2jdd.gameserver.model.quest.QuestState;

import instances.AbstractInstance;
import quests.Q10591_NobleMaterial.Q10591_NobleMaterial;

/**
 * Eva's Hidden Space instance zone.
 * @author Gladicek, St3eT
 */
public class EvasHiddenSpace extends AbstractInstance
{
	// NPCs
	private static final int EVAS_AVATAR = 33686;
	// Misc
	private static final int TEMPLATE_ID = 217;
	
	public EvasHiddenSpace()
	{
		super(TEMPLATE_ID);
		addStartNpc(EVAS_AVATAR);
		addTalkId(EVAS_AVATAR);
		addFirstTalkId(EVAS_AVATAR);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		if (event.equals("enterInstance"))
		{
			enterInstance(player, npc, TEMPLATE_ID);
		}
		else
		{
			final Instance world = npc.getInstanceWorld();
			if (isInInstance(world))
			{
				switch (event)
				{
					case "33686-01.html":
					case "33686-04.html":
					{
						htmltext = event;
						break;
					}
					case "inter_quest_10591_NPC33686":
					{
						final QuestState qs = player.getQuestState(Q10591_NobleMaterial.class.getSimpleName());
						if ((qs != null) && qs.isCond(6))
						{
							qs.setCond(7, true);
							htmltext = "33686-02.html";
						}
						break;
					}
					case "exitInstance":
					{
						world.finishInstance(0);
						break;
					}
					case "endCinematic":
					{
						startQuestTimer("exitInstance", 250, npc, player);
						break;
					}
				}
			}
			else if (event.equals("exitInstance"))
			{
				teleportPlayerOut(player, world);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		final QuestState qs = player.getQuestState(Q10591_NobleMaterial.class.getSimpleName());
		if (qs != null)
		{
			if (qs.isCond(6))
			{
				htmltext = "33686.html";
			}
			else if (qs.isCond(7))
			{
				htmltext = "33686-03.html";
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new EvasHiddenSpace();
	}
}
