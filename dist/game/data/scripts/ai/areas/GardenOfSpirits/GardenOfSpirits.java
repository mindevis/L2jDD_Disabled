
package ai.areas.GardenOfSpirits;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.zone.ZoneType;

import ai.AbstractNpcAI;

/**
 * Garden of Spirits teleport zones.
 * @author Mobius
 */
public class GardenOfSpirits extends AbstractNpcAI
{
	// Zones
	private static final int ZONE_ID_1 = 200213;
	private static final int ZONE_ID_2 = 200214;
	private static final int ZONE_ID_3 = 200215;
	private static final int ZONE_ID_4 = 200216;
	private static final int ZONE_ID_5 = 200217;
	private static final int ZONE_ID_6 = 200218;
	private static final int ZONE_ID_7 = 200219;
	private static final int ZONE_ID_8 = 200220;
	// Teleport Locations
	private static final Location TELEPORT_LOC_1 = new Location(-55613, 84681, -4560);
	private static final Location TELEPORT_LOC_2 = new Location(-53634, 88711, -3944);
	private static final Location TELEPORT_LOC_3 = new Location(-42123, 79254, -4056);
	private static final Location TELEPORT_LOC_4 = new Location(-45887, 77906, -3656);
	private static final Location TELEPORT_LOC_5 = new Location(-34900, 83422, -3512);
	private static final Location TELEPORT_LOC_6 = new Location(-42123, 79254, -4056);
	private static final Location TELEPORT_LOC_7 = new Location(-42123, 91991, -3793);
	private static final Location TELEPORT_LOC_8 = new Location(-39725, 80900, -3931);
	
	private GardenOfSpirits()
	{
		addEnterZoneId(ZONE_ID_1, ZONE_ID_2, ZONE_ID_3, ZONE_ID_4, ZONE_ID_5, ZONE_ID_6, ZONE_ID_7, ZONE_ID_8);
	}
	
	@Override
	public String onEnterZone(Creature creature, ZoneType zone)
	{
		if (creature.isPlayer())
		{
			switch (zone.getId())
			{
				case ZONE_ID_1:
				{
					creature.teleToLocation(TELEPORT_LOC_1);
					break;
				}
				case ZONE_ID_2:
				{
					creature.teleToLocation(TELEPORT_LOC_2);
					break;
				}
				case ZONE_ID_3:
				{
					creature.teleToLocation(TELEPORT_LOC_3);
					break;
				}
				case ZONE_ID_4:
				{
					creature.teleToLocation(TELEPORT_LOC_4);
					break;
				}
				case ZONE_ID_5:
				{
					creature.teleToLocation(TELEPORT_LOC_5);
					break;
				}
				case ZONE_ID_6:
				{
					creature.teleToLocation(TELEPORT_LOC_6);
					break;
				}
				case ZONE_ID_7:
				{
					creature.teleToLocation(TELEPORT_LOC_7);
					break;
				}
				case ZONE_ID_8:
				{
					creature.teleToLocation(TELEPORT_LOC_8);
					break;
				}
			}
		}
		return super.onEnterZone(creature, zone);
	}
	
	public static void main(String[] args)
	{
		new GardenOfSpirits();
	}
}