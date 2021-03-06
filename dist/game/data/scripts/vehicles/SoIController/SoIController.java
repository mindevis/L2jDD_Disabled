
package vehicles.SoIController;

import org.l2jdd.gameserver.enums.Movie;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.VehiclePathPoint;

import vehicles.AirShipController;

public class SoIController extends AirShipController
{
	private static final int DOCK_ZONE = 50600;
	private static final int LOCATION = 101;
	private static final int CONTROLLER_ID = 32604;
	
	private static final VehiclePathPoint[] ARRIVAL =
	{
		new VehiclePathPoint(-214422, 211396, 5000, 280, 2000),
		new VehiclePathPoint(-214422, 211396, 4422, 280, 2000)
	};
	
	private static final VehiclePathPoint[] DEPART =
	{
		new VehiclePathPoint(-214422, 211396, 5000, 280, 2000),
		new VehiclePathPoint(-215877, 209709, 5000, 280, 2000)
	};
	
	private static final VehiclePathPoint[][] TELEPORTS =
	{
		{
			new VehiclePathPoint(-214422, 211396, 5000, 280, 2000),
			new VehiclePathPoint(-215877, 209709, 5000, 280, 2000),
			new VehiclePathPoint(-206692, 220997, 3000, 0, 0)
		},
		{
			new VehiclePathPoint(-214422, 211396, 5000, 280, 2000),
			new VehiclePathPoint(-215877, 209709, 5000, 280, 2000),
			new VehiclePathPoint(-195357, 233430, 2500, 0, 0)
		}
	};
	
	private static final int[] FUEL =
	{
		0,
		50
	};
	
	public SoIController()
	{
		addStartNpc(CONTROLLER_ID);
		addFirstTalkId(CONTROLLER_ID);
		addTalkId(CONTROLLER_ID);
		
		_dockZone = DOCK_ZONE;
		addEnterZoneId(DOCK_ZONE);
		addExitZoneId(DOCK_ZONE);
		
		_shipSpawnX = -212719;
		_shipSpawnY = 213348;
		_shipSpawnZ = 5000;
		_oustLoc = new Location(-213401, 210401, 4408);
		_locationId = LOCATION;
		_arrivalPath = ARRIVAL;
		_departPath = DEPART;
		_teleportsTable = TELEPORTS;
		_fuelTable = FUEL;
		_movie = Movie.LAND_UNDEAD_A;
		validityCheck();
	}
	
	public static void main(String[] args)
	{
		new SoIController();
	}
}