/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.ShotType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Formulas;

/**
 * Soul Blow effect implementation.
 * @author Adry_85
 */
public class SoulBlow extends AbstractEffect
{
	private final double _power;
	private final double _chanceBoost;
	private final boolean _overHit;
	
	public SoulBlow(StatSet params)
	{
		_power = params.getDouble("power");
		_chanceBoost = params.getDouble("chanceBoost");
		_overHit = params.getBoolean("overHit", false);
	}
	
	/**
	 * If is not evaded and blow lands.
	 * @param effector
	 * @param effected
	 * @param skill
	 */
	@Override
	public boolean calcSuccess(Creature effector, Creature effected, Skill skill)
	{
		return !Formulas.calcPhysicalSkillEvasion(effector, effected, skill) && Formulas.calcBlowSuccess(effector, effected, skill, _chanceBoost);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.PHYSICAL_ATTACK;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effector.isAlikeDead())
		{
			return;
		}
		
		if (_overHit && effected.isAttackable())
		{
			((Attackable) effected).overhitEnabled(true);
		}
		
		final boolean ss = skill.useSoulShot() && (effector.isChargedShot(ShotType.SOULSHOTS) || effector.isChargedShot(ShotType.BLESSED_SOULSHOTS));
		final byte shld = Formulas.calcShldUse(effector, effected);
		double damage = Formulas.calcBlowDamage(effector, effected, skill, false, _power, shld, ss);
		if ((skill.getMaxSoulConsumeCount() > 0) && effector.isPlayer())
		{
			// Souls Formula (each soul increase +4%)
			final int chargedSouls = (effector.getActingPlayer().getChargedSouls() <= skill.getMaxSoulConsumeCount()) ? effector.getActingPlayer().getChargedSouls() : skill.getMaxSoulConsumeCount();
			damage *= 1 + (chargedSouls * 0.04);
		}
		
		effector.doAttack(damage, effected, skill, false, false, true, false);
	}
}