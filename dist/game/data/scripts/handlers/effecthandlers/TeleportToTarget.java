
package handlers.effecthandlers;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.serverpackets.FlyToLocation;
import org.l2jdd.gameserver.network.serverpackets.FlyToLocation.FlyType;
import org.l2jdd.gameserver.network.serverpackets.ValidateLocation;
import org.l2jdd.gameserver.util.Util;

/**
 * Teleport To Target effect implementation.
 * @author Didldak, Adry_85
 */
public class TeleportToTarget extends AbstractEffect
{
	public TeleportToTarget(StatSet params)
	{
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.TELEPORT_TO_TARGET;
	}
	
	@Override
	public boolean canStart(Creature effector, Creature effected, Skill skill)
	{
		return (effected != null) && GeoEngine.getInstance().canSeeTarget(effected, effector);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final int px = effected.getX();
		final int py = effected.getY();
		double ph = Util.convertHeadingToDegree(effected.getHeading());
		
		ph += 180;
		if (ph > 360)
		{
			ph -= 360;
		}
		
		ph = (Math.PI * ph) / 180;
		final int x = (int) (px + (25 * Math.cos(ph)));
		final int y = (int) (py + (25 * Math.sin(ph)));
		final int z = effected.getZ();
		
		final Location loc = GeoEngine.getInstance().getValidLocation(effector.getX(), effector.getY(), effector.getZ(), x, y, z,effector.getInstanceWorld());
		
		effector.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		effector.broadcastPacket(new FlyToLocation(effector, loc.getX(), loc.getY(), loc.getZ(), FlyType.DUMMY));
		effector.abortAttack();
		effector.abortCast();
		effector.setXYZ(loc);
		effector.broadcastPacket(new ValidateLocation(effector));
		effected.revalidateZone(true);
	}
}
