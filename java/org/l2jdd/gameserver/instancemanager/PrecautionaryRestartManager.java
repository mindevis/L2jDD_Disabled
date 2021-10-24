
package org.l2jdd.gameserver.instancemanager;

import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.l2jdd.Config;
import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.Shutdown;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.instance.GrandBossInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.actor.instance.RaidBossInstance;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.model.siege.Fort;
import org.l2jdd.gameserver.util.Broadcast;

/**
 * @author Mobius
 */
public class PrecautionaryRestartManager
{
	private static final Logger LOGGER = Logger.getLogger(PrecautionaryRestartManager.class.getName());
	
	private static final String SYSTEM_CPU_LOAD_VAR = "SystemCpuLoad";
	private static final String PROCESS_CPU_LOAD_VAR = "ProcessCpuLoad";
	
	private static boolean _restarting = false;
	
	protected PrecautionaryRestartManager()
	{
		ThreadPool.scheduleAtFixedRate(() ->
		{
			if (_restarting)
			{
				return;
			}
			
			if (Config.PRECAUTIONARY_RESTART_CPU && (getCpuLoad(SYSTEM_CPU_LOAD_VAR) > Config.PRECAUTIONARY_RESTART_PERCENTAGE))
			{
				if (serverBizzy())
				{
					return;
				}
				
				LOGGER.info("PrecautionaryRestartManager: CPU usage over " + Config.PRECAUTIONARY_RESTART_PERCENTAGE + "%.");
				LOGGER.info("PrecautionaryRestartManager: Server is using " + getCpuLoad(PROCESS_CPU_LOAD_VAR) + "%.");
				Broadcast.toAllOnlinePlayers("Server will restart in 10 minutes.", false);
				Shutdown.getInstance().startShutdown(null, 600, true);
			}
			
			if (Config.PRECAUTIONARY_RESTART_MEMORY && (getProcessRamLoad() > Config.PRECAUTIONARY_RESTART_PERCENTAGE))
			{
				if (serverBizzy())
				{
					return;
				}
				
				LOGGER.info("PrecautionaryRestartManager: Memory usage over " + Config.PRECAUTIONARY_RESTART_PERCENTAGE + "%.");
				Broadcast.toAllOnlinePlayers("Server will restart in 10 minutes.", false);
				Shutdown.getInstance().startShutdown(null, 600, true);
			}
		}, Config.PRECAUTIONARY_RESTART_DELAY, Config.PRECAUTIONARY_RESTART_DELAY);
	}
	
	private static double getCpuLoad(String var)
	{
		try
		{
			final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			final ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
			final AttributeList list = mbs.getAttributes(name, new String[]
			{
				var
			});
			
			if (list.isEmpty())
			{
				return 0;
			}
			
			final Attribute att = (Attribute) list.get(0);
			final Double value = (Double) att.getValue();
			if (value == -1)
			{
				return 0;
			}
			
			return (value * 1000) / 10d;
		}
		catch (Exception e)
		{
		}
		
		return 0;
	}
	
	private static double getProcessRamLoad()
	{
		final Runtime runTime = Runtime.getRuntime();
		final long totalMemory = runTime.maxMemory();
		final long usedMemory = totalMemory - ((totalMemory - runTime.totalMemory()) + runTime.freeMemory());
		return (usedMemory * 100) / totalMemory;
	}
	
	private boolean serverBizzy()
	{
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			if ((castle != null) && castle.getSiege().isInProgress())
			{
				return true;
			}
		}
		
		for (Fort fort : FortManager.getInstance().getForts())
		{
			if ((fort != null) && fort.getSiege().isInProgress())
			{
				return true;
			}
		}
		
		for (PlayerInstance player : World.getInstance().getPlayers())
		{
			if ((player == null) || player.isInOfflineMode())
			{
				continue;
			}
			
			if (player.isInOlympiadMode())
			{
				return true;
			}
			
			if (player.isOnEvent())
			{
				return true;
			}
			
			if (player.isInInstance())
			{
				return true;
			}
			
			final WorldObject target = player.getTarget();
			if ((target instanceof RaidBossInstance) || (target instanceof GrandBossInstance))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void restartEnabled()
	{
		_restarting = true;
	}
	
	public void restartAborted()
	{
		_restarting = false;
	}
	
	public static PrecautionaryRestartManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final PrecautionaryRestartManager INSTANCE = new PrecautionaryRestartManager();
	}
}