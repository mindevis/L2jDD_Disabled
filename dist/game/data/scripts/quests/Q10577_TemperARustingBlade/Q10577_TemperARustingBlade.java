
package quests.Q10577_TemperARustingBlade;

import org.l2jdd.gameserver.enums.QuestType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.Containers;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.creature.player.OnPlayerAugment;
import org.l2jdd.gameserver.model.events.listeners.ConsumerEventListener;
import org.l2jdd.gameserver.model.quest.Quest;
import org.l2jdd.gameserver.model.quest.QuestState;
import org.l2jdd.gameserver.model.quest.State;
import org.l2jdd.gameserver.network.serverpackets.ExTutorialShowId;

import quests.Q10566_BestChoice.Q10566_BestChoice;

/**
 * Temper a Rusting Blade (10577)
 * @URL https://l2wiki.com/Temper_a_Rusting_Blade
 * @author NightBR
 * @html by Werum
 */
public class Q10577_TemperARustingBlade extends Quest
{
	// NPCs
	private static final int FLUTTER = 30677;
	// Item
	private static final int AUGMENTATION_PRACTICE_STORMBRINGER = 36717;
	private static final int AUGMENTATION_PRACTICE_SPIRIT_STONE = 36718;
	private static final int AUGMENTATION_PRACTICE_GEMSTONE = 36719;
	// Rewards
	private static final long XP = 597699960;
	private static final int SP = 597690;
	private static final int CERTIFICATE_FROM_FLUTTER = 48175;
	// Misc
	private static final int MIN_LEVEL = 95;
	
	public Q10577_TemperARustingBlade()
	{
		super(10577);
		addStartNpc(FLUTTER);
		addTalkId(FLUTTER);
		addCondMinLevel(MIN_LEVEL, "noLevel.html");
		registerQuestItems(AUGMENTATION_PRACTICE_STORMBRINGER, AUGMENTATION_PRACTICE_SPIRIT_STONE, AUGMENTATION_PRACTICE_GEMSTONE);
		addCondStartedQuest(Q10566_BestChoice.class.getSimpleName(), "30677-99.html");
		Containers.Global().addListener(new ConsumerEventListener(Containers.Global(), EventType.ON_PLAYER_AUGMENT, (OnPlayerAugment event) -> OnPlayerAugment(event), this));
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		final QuestState qs = getQuestState(player, false);
		if (qs == null)
		{
			return getNoQuestMsg(player);
		}
		String htmltext = null;
		switch (event)
		{
			case "30677-02.htm":
			case "30677-05.html":
			case "30677-07.html":
			{
				htmltext = event;
				break;
			}
			case "30677-03.htm":
			{
				qs.startQuest();
				htmltext = event;
				break;
			}
			case "30677-04.html":
			{
				// show Service/Help/Private Store page
				player.sendPacket(new ExTutorialShowId(57));
				htmltext = event;
				break;
			}
			case "30677-06.html":
			{
				// show Service/Help/Augmentation page
				player.sendPacket(new ExTutorialShowId(39));
				giveItems(player, AUGMENTATION_PRACTICE_STORMBRINGER, 1);
				giveItems(player, AUGMENTATION_PRACTICE_SPIRIT_STONE, 1);
				giveItems(player, AUGMENTATION_PRACTICE_GEMSTONE, 20);
				qs.setCond(2, true);
				htmltext = event;
				break;
			}
			case "30677-08.html":
			{
				addExpAndSp(player, XP, SP);
				giveItems(player, CERTIFICATE_FROM_FLUTTER, 1);
				qs.exitQuest(QuestType.ONE_TIME, true);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player)
	{
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (qs.getState())
		{
			case State.CREATED:
			{
				htmltext = "30677-01.htm";
				break;
			}
			case State.STARTED:
			{
				if (qs.isCond(1))
				{
					htmltext = "30677-03.html";
				}
				else if (qs.isCond(2))
				{
					htmltext = "30677-09.html";
				}
				else
				{
					htmltext = "30677-07.html";
				}
				break;
			}
			case State.COMPLETED:
			{
				htmltext = getAlreadyCompletedMsg(player);
				break;
			}
		}
		return htmltext;
	}
	
	public void OnPlayerAugment(OnPlayerAugment event)
	{
		final PlayerInstance player = event.getPlayer();
		if ((player == null) || (event.getItem().getId() != AUGMENTATION_PRACTICE_STORMBRINGER))
		{
			return;
		}
		
		final QuestState qs = getQuestState(player, false);
		// Check if weapon has been augmented to complete the quest
		if ((qs != null) && qs.isCond(2) && (player.getInventory().getItemByItemId(AUGMENTATION_PRACTICE_STORMBRINGER).isAugmented()))
		{
			qs.setCond(3, true);
		}
	}
}
