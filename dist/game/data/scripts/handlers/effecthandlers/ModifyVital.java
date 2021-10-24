
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Modify vital effect implementation.
 * @author malyelfik
 */
public class ModifyVital extends AbstractEffect
{
	// Modify types
	private enum ModifyType
	{
		DIFF,
		SET,
		PER;
	}
	
	// Effect parameters
	private final ModifyType _type;
	private final int _hp;
	private final int _mp;
	private final int _cp;
	
	public ModifyVital(StatSet params)
	{
		_type = params.getEnum("type", ModifyType.class);
		if (_type != ModifyType.SET)
		{
			_hp = params.getInt("hp", 0);
			_mp = params.getInt("mp", 0);
			_cp = params.getInt("cp", 0);
		}
		else
		{
			_hp = params.getInt("hp", -1);
			_mp = params.getInt("mp", -1);
			_cp = params.getInt("cp", -1);
		}
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effected.isDead())
		{
			return;
		}
		
		if (effector.isPlayer() && effected.isPlayer() && effected.isAffected(EffectFlag.DUELIST_FURY) && !effector.isAffected(EffectFlag.DUELIST_FURY))
		{
			return;
		}
		
		switch (_type)
		{
			case DIFF:
			{
				effected.setCurrentCp(effected.getCurrentCp() + _cp);
				effected.setCurrentHp(effected.getCurrentHp() + _hp);
				effected.setCurrentMp(effected.getCurrentMp() + _mp);
				break;
			}
			case SET:
			{
				if (_cp >= 0)
				{
					effected.setCurrentCp(_cp);
				}
				if (_hp >= 0)
				{
					effected.setCurrentHp(_hp);
				}
				if (_mp >= 0)
				{
					effected.setCurrentMp(_mp);
				}
				break;
			}
			case PER:
			{
				effected.setCurrentCp(effected.getCurrentCp() + (effected.getMaxCp() * (_cp / 100)));
				effected.setCurrentHp(effected.getCurrentHp() + (effected.getMaxHp() * (_hp / 100)));
				effected.setCurrentMp(effected.getCurrentMp() + (effected.getMaxMp() * (_mp / 100)));
				break;
			}
		}
	}
}
