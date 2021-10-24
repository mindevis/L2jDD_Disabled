
package ai.bosses.Octavis.Lydia;

import org.l2jdd.gameserver.instancemanager.InstanceManager;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.instancezone.Instance;

import ai.AbstractNpcAI;

/**
 * Lydia AI.
 * @author St3eT
 */
public class Lydia extends AbstractNpcAI
{
	// NPCs
	private static final int LYDIA = 32892;
	// Misc
	private static final int OCTAVIS_TEMPLATE_ID = 180;
	private static final int OCTAVIS_EXTREME_TEMPLATE_ID = 181;
	
	private Lydia()
	{
		addStartNpc(LYDIA);
		addFirstTalkId(LYDIA);
		addTalkId(LYDIA);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		final Instance instance = InstanceManager.getInstance().getPlayerInstance(player, false);
		if ((instance != null) && ((instance.getTemplateId() == OCTAVIS_TEMPLATE_ID) || (instance.getTemplateId() == OCTAVIS_EXTREME_TEMPLATE_ID)))
		{
			htmltext = "Lydia-02.html";
		}
		else
		{
			htmltext = "Lydia-01.html";
		}
		return htmltext;
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "Lydia-03.html":
			case "Lydia-04.html":
			case "Lydia-05.html":
			{
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Lydia();
	}
}