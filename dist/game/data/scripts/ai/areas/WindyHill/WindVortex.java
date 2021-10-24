
package ai.areas.WindyHill;

import org.l2jdd.gameserver.enums.Race;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.NpcStringId;
import org.l2jdd.gameserver.network.serverpackets.ExShowScreenMessage;

import ai.AbstractNpcAI;

/**
 * Wind Vortex AI (Windy Hill)
 * @author malyelfik
 */
public class WindVortex extends AbstractNpcAI
{
	// NPC
	private static final int VORTEX = 23417;
	private static final int GIANT_WINDIMA = 23419;
	private static final int IMMENSE_WINDIMA = 23420;
	
	public WindVortex()
	{
		addAttackId(VORTEX);
	}
	
	@Override
	public String onAttack(Npc npc, PlayerInstance attacker, int damage, boolean isSummon)
	{
		if (npc.isScriptValue(0) && !npc.isDead())
		{
			npc.setScriptValue(1);
			if (attacker.getRace() == Race.ERTHEIA)
			{
				final int npcId = (attacker.isMageClass()) ? IMMENSE_WINDIMA : GIANT_WINDIMA;
				showOnScreenMsg(attacker, NpcStringId.A_POWERFUL_MONSTER_HAS_COME_TO_FACE_YOU, ExShowScreenMessage.TOP_CENTER, 5000);
				addSpawn(npcId, npc, false, 120000);
			}
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	public static void main(String[] args)
	{
		new WindVortex();
	}
}