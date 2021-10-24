
package org.l2jdd.gameserver.model.interfaces;

import java.util.List;

import org.l2jdd.gameserver.model.zone.type.BannedSpawnTerritory;
import org.l2jdd.gameserver.model.zone.type.SpawnTerritory;

/**
 * @author UnAfraid
 */
public interface ITerritorized
{
	void addTerritory(SpawnTerritory territory);
	
	List<SpawnTerritory> getTerritories();
	
	void addBannedTerritory(BannedSpawnTerritory territory);
	
	List<BannedSpawnTerritory> getBannedTerritories();
}
