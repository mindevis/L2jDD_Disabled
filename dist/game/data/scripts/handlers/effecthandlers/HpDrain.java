
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.ShotType;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Formulas;

/**
 * HP Drain effect implementation.
 * @author Adry_85
 */
public class HpDrain extends AbstractEffect
{
	private final double _power;
	private final double _percentage;
	
	public HpDrain(StatSet params)
	{
		_power = params.getDouble("power", 0);
		_percentage = params.getDouble("percentage", 0);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.HP_DRAIN;
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
		
		final boolean sps = skill.useSpiritShot() && effector.isChargedShot(ShotType.SPIRITSHOTS);
		final boolean bss = skill.useSpiritShot() && effector.isChargedShot(ShotType.BLESSED_SPIRITSHOTS);
		final boolean mcrit = Formulas.calcCrit(skill.getMagicCriticalRate(), effector, effected, skill);
		final double damage = Formulas.calcMagicDam(effector, effected, skill, effector.getMAtk(), _power, effected.getMDef(), sps, bss, mcrit);
		
		double drain = 0;
		final int cp = (int) effected.getCurrentCp();
		final int hp = (int) effected.getCurrentHp();
		
		if (cp > 0)
		{
			drain = (damage < cp) ? 0 : (damage - cp);
		}
		else if (damage > hp)
		{
			drain = hp;
		}
		else
		{
			drain = damage;
		}
		
		final double hpAdd = ((_percentage / 100) * drain);
		final double hpFinal = ((effector.getCurrentHp() + hpAdd) > effector.getMaxHp() ? effector.getMaxHp() : (effector.getCurrentHp() + hpAdd));
		effector.setCurrentHp(hpFinal);
		
		effector.doAttack(damage, effected, skill, false, false, mcrit, false);
	}
}