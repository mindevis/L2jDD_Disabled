
package custom.NoblessMaster;

import org.l2jdd.Config;
import org.l2jdd.gameserver.enums.QuestSound;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * @author Mobius
 */
public class NoblessMaster extends AbstractNpcAI
{
	// Item
	private static final int NOBLESS_TIARA = 7694;
	
	private NoblessMaster()
	{
		addStartNpc(Config.NOBLESS_MASTER_NPCID);
		addTalkId(Config.NOBLESS_MASTER_NPCID);
		addFirstTalkId(Config.NOBLESS_MASTER_NPCID);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (!Config.NOBLESS_MASTER_ENABLED)
		{
			return null;
		}
		
		switch (event)
		{
			case "noblesse":
			{
				if (player.getNobleLevel() > 0)
				{
					return "1003000-3.htm";
				}
				if (player.getLevel() >= Config.NOBLESS_MASTER_LEVEL_REQUIREMENT)
				{
					if (Config.NOBLESS_MASTER_REWARD_TIARA)
					{
						giveItems(player, NOBLESS_TIARA, 1);
					}
					player.setNobleLevel(Config.NOBLESS_MASTER_LEVEL_REWARDED);
					player.sendPacket(QuestSound.ITEMSOUND_QUEST_FINISH.getPacket());
					return "1003000-1.htm";
				}
				return "1003000-2.htm";
			}
		}
		
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "1003000.htm";
	}
	
	public static void main(String[] args)
	{
		new NoblessMaster();
	}
}
