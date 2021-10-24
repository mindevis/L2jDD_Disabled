
package ai.others.CastleSiegeManager;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Castle Siege Manager AI.
 * @author St3eT
 */
public class CastleSiegeManager extends AbstractNpcAI
{
	// NPCs
	private static final int[] SIEGE_MANAGER =
	{
		35104, // Gludio Castle
		35146, // Dion Castle
		35188, // Giran Castle
		35232, // Oren Castle
		35278, // Aden Castle
		35320, // Innadril Castle
		35367, // Goddard Castle
		35513, // Rune Castle
		35559, // Schuttgart Castle
	};
	
	private CastleSiegeManager()
	{
		addFirstTalkId(SIEGE_MANAGER);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		if (player.isClanLeader() && (player.getClanId() == npc.getCastle().getOwnerId()))
		{
			if (isInSiege(npc))
			{
				htmltext = "CastleSiegeManager.html";
			}
			else
			{
				htmltext = "CastleSiegeManager-01.html";
			}
		}
		else if (isInSiege(npc))
		{
			htmltext = "CastleSiegeManager-02.html";
		}
		else
		{
			npc.getCastle().getSiege().listRegisterClan(player);
		}
		return htmltext;
	}
	
	private boolean isInSiege(Npc npc)
	{
		return npc.getCastle().getSiege().isInProgress();
	}
	
	public static void main(String[] args)
	{
		new CastleSiegeManager();
	}
}