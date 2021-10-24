
package handlers.effecthandlers;

import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.serverpackets.FlyToLocation;
import org.l2jdd.gameserver.network.serverpackets.FlyToLocation.FlyType;
import org.l2jdd.gameserver.network.serverpackets.ValidateLocation;

/**
 * Throw Up effect implementation.
 */
public class FlyAway extends AbstractEffect
{
	private final int _radius;
	
	public FlyAway(StatSet params)
	{
		_radius = params.getInt("radius");
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final int dx = effector.getX() - effected.getX();
		final int dy = effector.getY() - effected.getY();
		final double distance = Math.sqrt((dx * dx) + (dy * dy));
		final double nRadius = effector.getCollisionRadius() + effected.getCollisionRadius() + _radius;
		
		final int x = (int) (effector.getX() - (nRadius * (dx / distance)));
		final int y = (int) (effector.getY() - (nRadius * (dy / distance)));
		final int z = effector.getZ();
		
		final Location destination = GeoEngine.getInstance().getValidLocation(effected.getX(), effected.getY(), effected.getZ(), x, y, z, effected.getInstanceWorld());
		
		effected.broadcastPacket(new FlyToLocation(effected, destination, FlyType.THROW_UP));
		effected.setXYZ(destination);
		effected.broadcastPacket(new ValidateLocation(effected));
		effected.revalidateZone(true);
	}
}
