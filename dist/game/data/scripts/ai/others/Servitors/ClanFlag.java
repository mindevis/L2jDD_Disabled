
package ai.others.Servitors;

import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.holders.SkillHolder;

import ai.AbstractNpcAI;

/**
 * @author Mobius
 */
public class ClanFlag extends AbstractNpcAI
{
	// NPC
	private static final int CLAN_FLAG = 19269;
	// Skills
	private static final SkillHolder BUFF = new SkillHolder(15095, 1);
	private static final SkillHolder DEBUFF = new SkillHolder(15096, 1);
	
	private ClanFlag()
	{
		addSpawnId(CLAN_FLAG);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		getTimers().addTimer("END_OF_LIFE", 1800000, npc, null);
		getTimers().addTimer("SKILL_CAST", 1000, npc, null);
		return super.onSpawn(npc);
	}
	
	@Override
	public void onTimerEvent(String event, StatSet params, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "SKILL_CAST":
			{
				if (npc.getSummoner() != null)
				{
					final Clan summonerClan = npc.getSummoner().getClan();
					if (summonerClan != null)
					{
						World.getInstance().forEachVisibleObjectInRange(npc, PlayerInstance.class, 2000, target ->
						{
							if ((target != null) && !target.isDead() && GeoEngine.getInstance().canSeeTarget(npc, target))
							{
								final Clan targetClan = target.getClan();
								if (targetClan != null)
								{
									if (targetClan == summonerClan)
									{
										BUFF.getSkill().applyEffects(npc, target);
									}
									else if (targetClan.isAtWarWith(summonerClan))
									{
										DEBUFF.getSkill().applyEffects(npc, target);
									}
								}
							}
						});
						getTimers().addTimer("SKILL_CAST", 3000, npc, null);
						return;
					}
				}
				getTimers().addTimer("END_OF_LIFE", 100, npc, null);
				break;
			}
			case "END_OF_LIFE":
			{
				getTimers().cancelTimer("SKILL_CAST", npc, null);
				npc.deleteMe();
				break;
			}
		}
	}
	
	public static void main(String[] args)
	{
		new ClanFlag();
	}
}
