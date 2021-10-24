
package org.l2jdd.gameserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.l2jdd.Config;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.itemcontainer.Inventory;
import org.l2jdd.gameserver.model.siege.Fort;

/**
 * Class managing periodical events with castle
 * @author Vice - 2008
 */
public class FortUpdater implements Runnable
{
	private static final Logger LOGGER = Logger.getLogger(FortUpdater.class.getName());
	private final Clan _clan;
	private final Fort _fort;
	private int _runCount;
	private final UpdaterType _updaterType;
	
	public enum UpdaterType
	{
		MAX_OWN_TIME, // gives fort back to NPC clan
		PERIODIC_UPDATE // raise blood oath/supply level
	}
	
	public FortUpdater(Fort fort, Clan clan, int runCount, UpdaterType ut)
	{
		_fort = fort;
		_clan = clan;
		_runCount = runCount;
		_updaterType = ut;
	}
	
	@Override
	public void run()
	{
		try
		{
			switch (_updaterType)
			{
				case PERIODIC_UPDATE:
				{
					_runCount++;
					if ((_fort.getOwnerClan() == null) || (_fort.getOwnerClan() != _clan))
					{
						return;
					}
					
					_fort.getOwnerClan().increaseBloodOathCount();
					
					if (_fort.getFortState() == 2)
					{
						if (_clan.getWarehouse().getAdena() >= Config.FS_FEE_FOR_CASTLE)
						{
							_clan.getWarehouse().destroyItemByItemId("FS_fee_for_Castle", Inventory.ADENA_ID, Config.FS_FEE_FOR_CASTLE, null, null);
							_fort.getContractedCastle().addToTreasuryNoTax(Config.FS_FEE_FOR_CASTLE);
							_fort.raiseSupplyLvL();
						}
						else
						{
							_fort.setFortState(1, 0);
						}
					}
					_fort.saveFortVariables();
					break;
				}
				case MAX_OWN_TIME:
				{
					if ((_fort.getOwnerClan() == null) || (_fort.getOwnerClan() != _clan))
					{
						return;
					}
					if (_fort.getOwnedTime() > (Config.FS_MAX_OWN_TIME * 3600))
					{
						_fort.removeOwner(true);
						_fort.setFortState(0, 0);
					}
					break;
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "", e);
		}
	}
	
	public int getRunCount()
	{
		return _runCount;
	}
}