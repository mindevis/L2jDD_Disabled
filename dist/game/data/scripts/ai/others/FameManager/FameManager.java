
package ai.others.FameManager;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.UserInfo;

import ai.AbstractNpcAI;

/**
 * Fame Manager AI.
 * @author St3eT
 */
public class FameManager extends AbstractNpcAI
{
	// Npc
	private static final int[] FAME_MANAGER =
	{
		36479, // Rapidus
		36480, // Scipio
	};
	// Misc
	private static final int MIN_LEVEL = 40;
	private static final int DECREASE_COST = 5000;
	private static final int REPUTATION_COST = 1000;
	private static final int MIN_CLAN_LEVEL = 5;
	private static final int CLASS_LEVEL = 2;
	
	private FameManager()
	{
		addStartNpc(FAME_MANAGER);
		addTalkId(FAME_MANAGER);
		addFirstTalkId(FAME_MANAGER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "36479.html":
			case "36479-02.html":
			case "36479-07.html":
			case "36480.html":
			case "36480-02.html":
			case "36480-07.html":
			{
				htmltext = event;
				break;
			}
			case "decreasePk":
			{
				if (player.getPkKills() > 0)
				{
					if ((player.getFame() >= DECREASE_COST) && (player.getLevel() >= MIN_LEVEL) && (player.getClassId().level() >= CLASS_LEVEL))
					{
						player.setFame(player.getFame() - DECREASE_COST);
						player.setPkKills(player.getPkKills() - 1);
						player.sendPacket(new UserInfo(player));
						htmltext = npc.getId() + "-06.html";
					}
					else
					{
						htmltext = npc.getId() + "-01.html";
					}
				}
				else
				{
					htmltext = npc.getId() + "-05.html";
				}
				break;
			}
			case "clanRep":
			{
				if ((player.getClan() != null) && (player.getClan().getLevel() >= MIN_CLAN_LEVEL))
				{
					if ((player.getFame() >= REPUTATION_COST) && (player.getLevel() >= MIN_LEVEL) && (player.getClassId().level() >= CLASS_LEVEL))
					{
						player.setFame(player.getFame() - REPUTATION_COST);
						player.getClan().addReputationScore(50, true);
						player.sendPacket(new UserInfo(player));
						player.sendPacket(SystemMessageId.YOU_HAVE_ACQUIRED_50_CLAN_REPUTATION);
						htmltext = npc.getId() + "-04.html";
					}
					else
					{
						htmltext = npc.getId() + "-01.html";
					}
				}
				else
				{
					htmltext = npc.getId() + "-03.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return ((player.getFame() > 0) && (player.getLevel() >= MIN_LEVEL) && (player.getClassId().level() >= CLASS_LEVEL)) ? npc.getId() + ".html" : npc.getId() + "-01.html";
	}
	
	public static void main(String[] args)
	{
		new FameManager();
	}
}
