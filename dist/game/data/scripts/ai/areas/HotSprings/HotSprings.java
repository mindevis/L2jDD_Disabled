
package ai.areas.HotSprings;

import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.BuffInfo;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.SkillCaster;

import ai.AbstractNpcAI;

/**
 * Hot Springs AI.
 * @author Pandragon
 */
public class HotSprings extends AbstractNpcAI
{
	// NPCs
	private static final int BANDERSNATCHLING = 21314;
	private static final int FLAVA = 21316;
	private static final int ATROXSPAWN = 21317;
	private static final int NEPENTHES = 21319;
	private static final int ATROX = 21321;
	private static final int BANDERSNATCH = 21322;
	// Skills
	private static final int RHEUMATISM = 4551;
	private static final int CHOLERA = 4552;
	private static final int FLU = 4553;
	private static final int MALARIA = 4554;
	// Misc
	private static final int DISEASE_CHANCE = 10;
	
	private HotSprings()
	{
		addAttackId(BANDERSNATCHLING, FLAVA, ATROXSPAWN, NEPENTHES, ATROX, BANDERSNATCH);
	}
	
	@Override
	public String onAttack(Npc npc, PlayerInstance attacker, int damage, boolean isSummon)
	{
		if (getRandom(100) < DISEASE_CHANCE)
		{
			tryToInfect(npc, attacker, MALARIA);
		}
		
		if (getRandom(100) < DISEASE_CHANCE)
		{
			switch (npc.getId())
			{
				case BANDERSNATCHLING:
				case ATROX:
				{
					tryToInfect(npc, attacker, RHEUMATISM);
					break;
				}
				case FLAVA:
				case NEPENTHES:
				{
					tryToInfect(npc, attacker, CHOLERA);
					break;
				}
				case ATROXSPAWN:
				case BANDERSNATCH:
				{
					tryToInfect(npc, attacker, FLU);
					break;
				}
			}
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	private void tryToInfect(Npc npc, Creature creature, int diseaseId)
	{
		final BuffInfo info = creature.getEffectList().getBuffInfoBySkillId(diseaseId);
		final int skillLevel = (info == null) ? 1 : (info.getSkill().getLevel() < 10) ? info.getSkill().getLevel() + 1 : 10;
		final Skill skill = SkillData.getInstance().getSkill(diseaseId, skillLevel);
		if ((skill != null) && SkillCaster.checkUseConditions(npc, skill))
		{
			npc.setTarget(creature);
			npc.doCast(skill);
		}
	}
	
	public static void main(String[] args)
	{
		new HotSprings();
	}
}