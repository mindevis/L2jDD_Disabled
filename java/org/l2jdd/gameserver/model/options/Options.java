
package org.l2jdd.gameserver.model.options;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.skills.BuffInfo;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.serverpackets.SkillCoolTime;

/**
 * @author UnAfraid
 */
public class Options
{
	private final int _id;
	private List<AbstractEffect> _effects = null;
	private List<SkillHolder> _activeSkill = null;
	private List<SkillHolder> _passiveSkill = null;
	private List<OptionsSkillHolder> _activationSkills = null;
	
	/**
	 * @param id
	 */
	public Options(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public void addEffect(AbstractEffect effect)
	{
		if (_effects == null)
		{
			_effects = new ArrayList<>();
		}
		_effects.add(effect);
	}
	
	public List<AbstractEffect> getEffects()
	{
		return _effects;
	}
	
	public boolean hasEffects()
	{
		return _effects != null;
	}
	
	public boolean hasActiveSkills()
	{
		return _activeSkill != null;
	}
	
	public List<SkillHolder> getActiveSkills()
	{
		return _activeSkill;
	}
	
	public void addActiveSkill(SkillHolder holder)
	{
		if (_activeSkill == null)
		{
			_activeSkill = new ArrayList<>();
		}
		_activeSkill.add(holder);
	}
	
	public boolean hasPassiveSkills()
	{
		return _passiveSkill != null;
	}
	
	public List<SkillHolder> getPassiveSkills()
	{
		return _passiveSkill;
	}
	
	public void addPassiveSkill(SkillHolder holder)
	{
		if (_passiveSkill == null)
		{
			_passiveSkill = new ArrayList<>();
		}
		_passiveSkill.add(holder);
	}
	
	public boolean hasActivationSkills()
	{
		return _activationSkills != null;
	}
	
	public boolean hasActivationSkills(OptionsSkillType type)
	{
		if (_activationSkills != null)
		{
			for (OptionsSkillHolder holder : _activationSkills)
			{
				if (holder.getSkillType() == type)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public List<OptionsSkillHolder> getActivationsSkills()
	{
		return _activationSkills;
	}
	
	public List<OptionsSkillHolder> getActivationsSkills(OptionsSkillType type)
	{
		final List<OptionsSkillHolder> temp = new ArrayList<>();
		if (_activationSkills != null)
		{
			for (OptionsSkillHolder holder : _activationSkills)
			{
				if (holder.getSkillType() == type)
				{
					temp.add(holder);
				}
			}
		}
		return temp;
	}
	
	public void addActivationSkill(OptionsSkillHolder holder)
	{
		if (_activationSkills == null)
		{
			_activationSkills = new ArrayList<>();
		}
		_activationSkills.add(holder);
	}
	
	public void apply(PlayerInstance player)
	{
		if (hasEffects())
		{
			final BuffInfo info = new BuffInfo(player, player, null, true, null, this);
			for (AbstractEffect effect : _effects)
			{
				if (effect.isInstant())
				{
					if (effect.calcSuccess(info.getEffector(), info.getEffected(), info.getSkill()))
					{
						effect.instant(info.getEffector(), info.getEffected(), info.getSkill(), info.getItem());
					}
				}
				else
				{
					effect.continuousInstant(info.getEffector(), info.getEffected(), info.getSkill(), info.getItem());
					effect.pump(player, info.getSkill());
					if (effect.canStart(info.getEffector(), info.getEffected(), info.getSkill()))
					{
						info.addEffect(effect);
					}
				}
			}
			if (!info.getEffects().isEmpty())
			{
				player.getEffectList().add(info);
			}
		}
		if (hasActiveSkills())
		{
			for (SkillHolder holder : _activeSkill)
			{
				addSkill(player, holder.getSkill());
			}
		}
		if (hasPassiveSkills())
		{
			for (SkillHolder holder : _passiveSkill)
			{
				addSkill(player, holder.getSkill());
			}
		}
		if (hasActivationSkills())
		{
			for (OptionsSkillHolder holder : _activationSkills)
			{
				player.addTriggerSkill(holder);
			}
		}
		
		player.getStat().recalculateStats(true);
		player.sendSkillList();
	}
	
	public void remove(PlayerInstance player)
	{
		if (hasEffects())
		{
			for (BuffInfo info : player.getEffectList().getOptions())
			{
				if (info.getOption() == this)
				{
					player.getEffectList().remove(info, false, true, true);
				}
			}
		}
		if (hasActiveSkills())
		{
			for (SkillHolder holder : _activeSkill)
			{
				player.removeSkill(holder.getSkill(), false, false);
			}
		}
		if (hasPassiveSkills())
		{
			for (SkillHolder holder : _passiveSkill)
			{
				player.removeSkill(holder.getSkill(), false, true);
			}
		}
		if (hasActivationSkills())
		{
			for (OptionsSkillHolder holder : _activationSkills)
			{
				player.removeTriggerSkill(holder);
			}
		}
		
		player.getStat().recalculateStats(true);
		player.sendSkillList();
	}
	
	private void addSkill(PlayerInstance player, Skill skill)
	{
		boolean updateTimeStamp = false;
		player.addSkill(skill, false);
		if (skill.isActive())
		{
			final long remainingTime = player.getSkillRemainingReuseTime(skill.getReuseHashCode());
			if (remainingTime > 0)
			{
				player.addTimeStamp(skill, remainingTime);
				player.disableSkill(skill, remainingTime);
			}
			updateTimeStamp = true;
		}
		if (updateTimeStamp)
		{
			player.sendPacket(new SkillCoolTime(player));
		}
	}
}
