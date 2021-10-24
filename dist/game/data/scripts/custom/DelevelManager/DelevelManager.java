
package custom.DelevelManager;

import org.l2jdd.Config;
import org.l2jdd.gameserver.data.xml.ExperienceData;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * @author Mobius
 */
public class DelevelManager extends AbstractNpcAI
{
	private DelevelManager()
	{
		addStartNpc(Config.DELEVEL_MANAGER_NPCID);
		addTalkId(Config.DELEVEL_MANAGER_NPCID);
		addFirstTalkId(Config.DELEVEL_MANAGER_NPCID);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (!Config.DELEVEL_MANAGER_ENABLED)
		{
			return null;
		}
		
		switch (event)
		{
			case "delevel":
			{
				if (player.getLevel() <= Config.DELEVEL_MANAGER_MINIMUM_DELEVEL)
				{
					return "1002000-2.htm";
				}
				if (getQuestItemsCount(player, Config.DELEVEL_MANAGER_ITEMID) >= Config.DELEVEL_MANAGER_ITEMCOUNT)
				{
					takeItems(player, Config.DELEVEL_MANAGER_ITEMID, Config.DELEVEL_MANAGER_ITEMCOUNT);
					player.getStat().removeExpAndSp((player.getExp() - ExperienceData.getInstance().getExpForLevel(player.getLevel() - 1)), 0);
					player.broadcastUserInfo();
					return "1002000.htm";
				}
				return "1002000-1.htm";
			}
		}
		
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "1002000.htm";
	}
	
	public static void main(String[] args)
	{
		new DelevelManager();
	}
}
