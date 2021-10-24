
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.stat.PlayerStat;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.BuffInfo;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.zone.ZoneId;

/**
 * Recover Vitality in Peace Zone effect implementation.
 * @author Mobius
 */
public class RecoverVitalityInPeaceZone extends AbstractEffect
{
	private final double _amount;
	private final int _ticks;
	
	public RecoverVitalityInPeaceZone(StatSet params)
	{
		_amount = params.getDouble("amount", 0);
		_ticks = params.getInt("ticks", 10);
	}
	
	@Override
	public int getTicks()
	{
		return _ticks;
	}
	
	@Override
	public boolean onActionTime(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if ((effected == null) //
			|| effected.isDead() //
			|| !effected.isPlayer() //
			|| !effected.isInsideZone(ZoneId.PEACE))
		{
			return false;
		}
		
		long vitality = effected.getActingPlayer().getVitalityPoints();
		vitality += _amount;
		if (vitality >= PlayerStat.MAX_VITALITY_POINTS)
		{
			vitality = PlayerStat.MAX_VITALITY_POINTS;
		}
		effected.getActingPlayer().setVitalityPoints((int) vitality, true);
		
		return skill.isToggle();
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		if ((effected != null) //
			&& effected.isPlayer())
		{
			final BuffInfo info = effected.getEffectList().getBuffInfoBySkillId(skill.getId());
			if ((info != null) && !info.isRemoved())
			{
				long vitality = effected.getActingPlayer().getVitalityPoints();
				vitality += _amount * 100;
				if (vitality >= PlayerStat.MAX_VITALITY_POINTS)
				{
					vitality = PlayerStat.MAX_VITALITY_POINTS;
				}
				effected.getActingPlayer().setVitalityPoints((int) vitality, true);
			}
		}
	}
}
