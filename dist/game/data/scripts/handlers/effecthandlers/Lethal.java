
package handlers.effecthandlers;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Formulas;
import org.l2jdd.gameserver.model.stats.Stat;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Lethal effect implementation.
 * @author Adry_85
 */
public class Lethal extends AbstractEffect
{
	private final double _fullLethal;
	private final double _halfLethal;
	
	public Lethal(StatSet params)
	{
		_fullLethal = params.getDouble("fullLethal", 0);
		_halfLethal = params.getDouble("halfLethal", 0);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.LETHAL_ATTACK;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effector.isPlayer() && !effector.getAccessLevel().canGiveDamage())
		{
			return;
		}
		
		if (skill.getMagicLevel() < (effected.getLevel() - 6))
		{
			return;
		}
		
		if (!effected.isLethalable() || effected.isHpBlocked())
		{
			return;
		}
		
		if (effector.isPlayer() && effected.isPlayer() && effected.isAffected(EffectFlag.DUELIST_FURY) && !effector.isAffected(EffectFlag.DUELIST_FURY))
		{
			return;
		}
		
		final double chanceMultiplier = Formulas.calcAttributeBonus(effector, effected, skill) * Formulas.calcGeneralTraitBonus(effector, effected, skill.getTraitType(), false);
		
		// Calculate instant kill resistance first.
		if (Rnd.get(100) < effected.getStat().getValue(Stat.INSTANT_KILL_RESIST, 0))
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.C1_HAS_EVADED_C2_S_ATTACK);
			sm.addString(effected.getName());
			sm.addString(effector.getName());
			effected.sendPacket(sm);
			final SystemMessage sm2 = new SystemMessage(SystemMessageId.C1_S_ATTACK_WENT_ASTRAY);
			sm2.addString(effector.getName());
			effector.sendPacket(sm2);
		}
		// Lethal Strike
		else if (Rnd.get(100) < (_fullLethal * chanceMultiplier))
		{
			// for Players CP and HP is set to 1.
			if (effected.isPlayer())
			{
				effected.setCurrentCp(1);
				effected.setCurrentHp(1);
				effected.sendPacket(SystemMessageId.LETHAL_STRIKE);
			}
			// for Monsters HP is set to 1.
			else if (effected.isMonster() || effected.isSummon())
			{
				effected.setCurrentHp(1);
			}
			effector.sendPacket(SystemMessageId.HIT_WITH_LETHAL_STRIKE);
		}
		// Half-Kill
		else if (Rnd.get(100) < (_halfLethal * chanceMultiplier))
		{
			// for Players CP is set to 1.
			if (effected.isPlayer())
			{
				effected.setCurrentCp(1);
				effected.sendPacket(SystemMessageId.HALF_KILL);
				effected.sendPacket(SystemMessageId.YOUR_CP_WAS_DRAINED_BECAUSE_YOU_WERE_HIT_WITH_A_HALF_KILL_SKILL);
			}
			// for Monsters HP is set to 50%.
			else if (effected.isMonster() || effected.isSummon())
			{
				effected.setCurrentHp(effected.getCurrentHp() * 0.5);
			}
			effector.sendPacket(SystemMessageId.HALF_KILL);
		}
		
		// No matter if lethal succeeded or not, its reflected.
		Formulas.calcCounterAttack(effector, effected, skill, false);
	}
}
