
package handlers.effecthandlers;

import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Formulas;
import org.l2jdd.gameserver.network.serverpackets.FlyToLocation;
import org.l2jdd.gameserver.network.serverpackets.FlyToLocation.FlyType;
import org.l2jdd.gameserver.network.serverpackets.ValidateLocation;

/**
 * An effect that pulls effected target back to the effector.
 * @author Nik
 */
public class PullBack extends AbstractEffect
{
	private final int _speed;
	private final int _delay;
	private final int _animationSpeed;
	private final FlyType _type;
	
	public PullBack(StatSet params)
	{
		_speed = params.getInt("speed", 0);
		_delay = params.getInt("delay", _speed);
		_animationSpeed = params.getInt("animationSpeed", 0);
		_type = params.getEnum("type", FlyType.class, FlyType.WARP_FORWARD); // type 9
	}
	
	@Override
	public boolean calcSuccess(Creature effector, Creature effected, Skill skill)
	{
		return Formulas.calcProbability(100, effector, effected, skill);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		// Prevent pulling raids and town NPCs.
		if ((effected == null) || effected.isRaid() || (effected.isNpc() && !effected.isAttackable()))
		{
			return;
		}
		// Prevent pulling debuff blocked characters.
		if ((effected.isDebuffBlocked()))
		{
			return;
		}
		
		// In retail, you get debuff, but you are not even moved if there is obstacle. You are still disabled from using skills and moving though.
		if (GeoEngine.getInstance().canMoveToTarget(effected.getX(), effected.getY(), effected.getZ(), effector.getX(), effector.getY(), effector.getZ(), effector.getInstanceWorld()))
		{
			effected.broadcastPacket(new FlyToLocation(effected, effector, _type, _speed, _delay, _animationSpeed));
			effected.setXYZ(effector.getX(), effector.getY(), GeoEngine.getInstance().getHeight(effector.getX(), effector.getY(), effector.getZ()) + 10);
			effected.broadcastPacket(new ValidateLocation(effected), false);
			effected.revalidateZone(true);
		}
	}
}
