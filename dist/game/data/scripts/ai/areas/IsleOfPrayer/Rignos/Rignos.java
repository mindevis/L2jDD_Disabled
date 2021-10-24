
package ai.areas.IsleOfPrayer.Rignos;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.SkillHolder;

import ai.AbstractNpcAI;

/**
 * Rignos AI.
 * @author St3eT
 */
public class Rignos extends AbstractNpcAI
{
	// NPC
	private static final int RIGNOS = 32349; // Rignos
	// Item
	private static final int STAMP = 10013; // Race Stamp
	private static final int KEY = 9694; // Secret Key
	// Skill
	private static final SkillHolder TIMER = new SkillHolder(5239, 5); // Event Timer
	// Misc
	private static final int MIN_LV = 78;
	
	private Rignos()
	{
		addStartNpc(RIGNOS);
		addTalkId(RIGNOS);
		addFirstTalkId(RIGNOS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "32349-03.html":
			{
				return event;
			}
			case "startRace":
			{
				if (npc.isScriptValue(0))
				{
					npc.setScriptValue(1);
					startQuestTimer("TIME_OUT", 1800000, npc, null);
					TIMER.getSkill().applyEffects(player, player);
					final Summon pet = player.getPet();
					if (pet != null)
					{
						TIMER.getSkill().applyEffects(pet, pet);
					}
					player.getServitors().values().forEach(s -> TIMER.getSkill().applyEffects(s, s));
					if (hasQuestItems(player, STAMP))
					{
						takeItems(player, STAMP, -1);
					}
				}
				break;
			}
			case "exchange":
			{
				if (getQuestItemsCount(player, STAMP) >= 4)
				{
					giveItems(player, KEY, 3);
					takeItems(player, STAMP, -1);
				}
				break;
			}
			case "TIME_OUT":
			{
				npc.setScriptValue(0);
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		String htmltext = (npc.isScriptValue(0) && (player.getLevel() >= MIN_LV)) ? "32349.html" : "32349-02.html";
		if (getQuestItemsCount(player, STAMP) >= 4)
		{
			htmltext = "32349-01.html";
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Rignos();
	}
}