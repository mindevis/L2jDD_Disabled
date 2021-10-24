
package ai.others.SymbolMaker;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.HennaEquipList;
import org.l2jdd.gameserver.network.serverpackets.HennaRemoveList;

import ai.AbstractNpcAI;

/**
 * Symbol Maker AI.
 * @author Adry_85
 */
public class SymbolMaker extends AbstractNpcAI
{
	// NPCs
	private static final int[] NPCS =
	{
		31046, // Marsden
		31047, // Kell
		31048, // McDermott
		31049, // Pepper
		31050, // Thora
		31051, // Keach
		31052, // Heid
		31053, // Kidder
		31264, // Olsun
		31308, // Achim
		31953, // Rankar
	};
	
	private SymbolMaker()
	{
		addFirstTalkId(NPCS);
		addStartNpc(NPCS);
		addTalkId(NPCS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "symbol_maker.htm":
			case "symbol_maker-1.htm":
			case "symbol_maker-2.htm":
			case "symbol_maker-3.htm":
			{
				htmltext = event;
				break;
			}
			case "Draw":
			{
				player.sendPacket(new HennaEquipList(player));
				break;
			}
			case "Remove":
			{
				player.sendPacket(new HennaRemoveList(player));
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "symbol_maker.htm";
	}
	
	public static void main(String[] args)
	{
		new SymbolMaker();
	}
}