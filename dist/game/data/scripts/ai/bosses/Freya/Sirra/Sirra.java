
package ai.bosses.Freya.Sirra;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.instancezone.Instance;

import ai.AbstractNpcAI;

/**
 * Sirra AI.
 * @author St3eT
 */
public class Sirra extends AbstractNpcAI
{
	// NPC
	private static final int SIRRA = 32762;
	// Misc
	private static final int FREYA_INSTID = 139;
	private static final int FREYA_HARD_INSTID = 144;
	
	private Sirra()
	{
		addFirstTalkId(SIRRA);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		final Instance world = npc.getInstanceWorld();
		if (world != null)
		{
			if ((world.getTemplateId() == FREYA_INSTID))
			{
				return (world.isStatus(0)) ? "32762-easy.html" : "32762-easyfight.html";
			}
			else if ((world.getTemplateId() == FREYA_HARD_INSTID))
			{
				return (world.isStatus(0)) ? "32762-hard.html" : "32762-hardfight.html";
			}
		}
		return "32762.html";
	}
	
	public static void main(String[] args)
	{
		new Sirra();
	}
}