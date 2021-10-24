
package custom.ShadowWeapons;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Shadow Weapons AI.<br>
 * @author Nyaran, jurchiks
 */
public class ShadowWeapons extends AbstractNpcAI
{
	// @formatter:off
	private static final int[] NPCS =
	{
		30037, 30066, 30070, 30109, 30115, 30120, 30174, 30175, 30176, 30187,
		30191, 30195, 30288, 30289, 30290, 30297, 30373, 30462, 30474, 30498,
		30499, 30500, 30503, 30504, 30505, 30511, 30512, 30513, 30595, 30676,
		30677, 30681, 30685, 30687, 30689, 30694, 30699, 30704, 30845, 30847,
		30849, 30854, 30857, 30862, 30865, 30894, 30897, 30900, 30905, 30910,
		30913, 31269, 31272, 31276, 31285, 31288, 31314, 31317, 31321, 31324,
		31326, 31328, 31331, 31334, 31336, 31958, 31961, 31965, 31968, 31974,
		31977, 31996, 32092, 32093, 32094, 32095, 32096, 32097, 32098, 32193,
		32196, 32199, 32202, 32205, 32206, 32213, 32214, 32221, 32222, 32229,
		32230, 32233, 32234
	};
	// @formatter:on
	private ShadowWeapons()
	{
		addStartNpc(NPCS);
		addTalkId(NPCS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "ShadowCoupon.html":
			{
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player)
	{
		String htmltext;
		final boolean hasD = hasQuestItems(player, 8869); // Shadow Item Exchange Coupon (D-Grade)
		final boolean hasC = hasQuestItems(player, 8870); // Shadow Item Exchange Coupon (C-Grade)
		
		if (hasD || hasC)
		{
			if (!hasD)
			{
				htmltext = "exchange_c.html";
			}
			else if (!hasC)
			{
				htmltext = "exchange_d.html";
			}
			else
			{
				htmltext = "exchange_both.html";
			}
		}
		else
		{
			htmltext = "exchange_no.html";
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new ShadowWeapons();
	}
}
