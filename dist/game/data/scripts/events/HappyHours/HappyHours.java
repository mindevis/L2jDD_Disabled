
package events.HappyHours;

import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.quest.LongTimeEvent;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 */
public class HappyHours extends LongTimeEvent
{
	// NPC
	private static final int SIBI = 34262;
	// Items
	private static final int SUPPLY_BOX = 47399;
	private static final int SIBIS_COIN = 49783;
	// Skill
	private static final int TRANSFORMATION_SKILL = 39171;
	// Other
	private static final int MIN_LEVEL = 20;
	private static final int REWARD_INTERVAL = 60 * 60 * 1000; // 1 hour
	private static long _lastRewardTime = Chronos.currentTimeMillis();
	
	private HappyHours()
	{
		addStartNpc(SIBI);
		addFirstTalkId(SIBI);
		addTalkId(SIBI);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "34262-1.htm":
			{
				htmltext = event;
				break;
			}
			case "giveSupplyBox":
			{
				if (player.getLevel() < MIN_LEVEL)
				{
					return "34262-2.htm";
				}
				if (ownsAtLeastOneItem(player, SUPPLY_BOX))
				{
					return "34262-3.htm";
				}
				giveItems(player, SUPPLY_BOX, 1);
				break;
			}
			case "REWARD_SIBI_COINS":
			{
				if (isEventPeriod())
				{
					if ((Chronos.currentTimeMillis() - (_lastRewardTime + REWARD_INTERVAL)) > 0) // Exploit check - Just in case.
					{
						_lastRewardTime = Chronos.currentTimeMillis();
						final ExShowScreenMessage screenMsg = new ExShowScreenMessage("You obtained 20 Sibi's coins.", ExShowScreenMessage.TOP_CENTER, 7000, 0, true, true);
						final SystemMessage systemMsg = new SystemMessage(SystemMessageId.YOU_VE_OBTAINED_S1_LUCKY_COINS);
						systemMsg.addInt(20);
						for (PlayerInstance plr : World.getInstance().getPlayers())
						{
							if ((plr != null) && (plr.isOnlineInt() == 1) && plr.isAffectedBySkill(TRANSFORMATION_SKILL))
							{
								plr.addItem("HappyHours", SIBIS_COIN, 20, player, false);
								plr.sendPacket(screenMsg);
								plr.sendPacket(systemMsg);
								// TODO: Random reward.
							}
						}
					}
				}
				else
				{
					cancelQuestTimers("REWARD_SIBI_COINS");
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "34262.htm";
	}
	
	@Override
	protected void startEvent()
	{
		super.startEvent();
		cancelQuestTimers("REWARD_SIBI_COINS");
		startQuestTimer("REWARD_SIBI_COINS", REWARD_INTERVAL + 1000, null, null, true);
	}
	
	public static void main(String[] args)
	{
		new HappyHours();
	}
}
