
package vehicles.SoDController;

import org.l2jdd.gameserver.enums.Movie;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.VehiclePathPoint;

import vehicles.AirShipController;

public class SoDController extends AirShipController
{
	private static final int DOCK_ZONE = 50601;
	private static final int LOCATION = 102;
	private static final int CONTROLLER_ID = 32605;
	
	private static final VehiclePathPoint[] ARRIVAL =
	{
		new VehiclePathPoint(-246445, 252331, 4359, 280, 2000),
	};
	
	private static final VehiclePathPoint[] DEPART =
	{
		new VehiclePathPoint(-245245, 251040, 4359, 280, 2000)
	};
	
	private static final VehiclePathPoint[][] TELEPORTS =
	{
		{
			new VehiclePathPoint(-245245, 251040, 4359, 280, 2000),
			new VehiclePathPoint(-235693, 248843, 5100, 0, 0)
		},
		{
			new VehiclePathPoint(-245245, 251040, 4359, 280, 2000),
			new VehiclePathPoint(-195357, 233430, 2500, 0, 0)
		}
	};
	
	private static final int[] FUEL =
	{
		0,
		100
	};
	
	public SoDController()
	{
		addStartNpc(CONTROLLER_ID);
		addFirstTalkId(CONTROLLER_ID);
		addTalkId(CONTROLLER_ID);
		
		_dockZone = DOCK_ZONE;
		addEnterZoneId(DOCK_ZONE);
		addExitZoneId(DOCK_ZONE);
		
		_shipSpawnX = -247702;
		_shipSpawnY = 253631;
		_shipSpawnZ = 4359;
		_oustLoc = new Location(-247746, 251079, 4328);
		_locationId = LOCATION;
		_arrivalPath = ARRIVAL;
		_departPath = DEPART;
		_teleportsTable = TELEPORTS;
		_fuelTable = FUEL;
		_movie = Movie.LAND_DISTRUCTION_A;
		validityCheck();
	}
	
	public static void main(String[] args)
	{
		new SoDController();
	}
}