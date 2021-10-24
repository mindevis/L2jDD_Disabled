
package handlers.effecthandlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PetInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectFlag;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.instancezone.Instance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Resurrection Special effect implementation.
 * @author Zealar
 */
public class ResurrectionSpecial extends AbstractEffect
{
	private final int _power;
	private final int _hpPercent;
	private final int _mpPercent;
	private final int _cpPercent;
	private final Set<Integer> _instanceId;
	
	public ResurrectionSpecial(StatSet params)
	{
		_power = params.getInt("power", 0);
		_hpPercent = params.getInt("hpPercent", 0);
		_mpPercent = params.getInt("mpPercent", 0);
		_cpPercent = params.getInt("cpPercent", 0);
		
		final String instanceIds = params.getString("instanceId", null);
		if ((instanceIds != null) && !instanceIds.isEmpty())
		{
			_instanceId = new HashSet<>();
			for (String id : instanceIds.split(";"))
			{
				_instanceId.add(Integer.parseInt(id));
			}
		}
		else
		{
			_instanceId = Collections.<Integer> emptySet();
		}
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.RESURRECTION_SPECIAL;
	}
	
	@Override
	public long getEffectFlags()
	{
		return EffectFlag.RESURRECTION_SPECIAL.getMask();
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		if (!effected.isPlayer() && !effected.isPet())
		{
			return;
		}
		
		final PlayerInstance caster = effector.getActingPlayer();
		final Instance instance = caster.getInstanceWorld();
		if (!_instanceId.isEmpty() && ((instance == null) || !_instanceId.contains(instance.getTemplateId())))
		{
			return;
		}
		
		if (effected.isPlayer())
		{
			effected.getActingPlayer().reviveRequest(caster, false, _power, _hpPercent, _mpPercent, _cpPercent);
		}
		else if (effected.isPet())
		{
			final PetInstance pet = (PetInstance) effected;
			effected.getActingPlayer().reviveRequest(pet.getActingPlayer(), true, _power, _hpPercent, _mpPercent, _cpPercent);
		}
	}
}