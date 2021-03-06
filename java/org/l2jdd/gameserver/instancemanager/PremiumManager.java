
package org.l2jdd.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.commons.database.DatabaseFactory;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.Containers;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.ListenersContainer;
import org.l2jdd.gameserver.model.events.impl.creature.player.OnPlayerLogin;
import org.l2jdd.gameserver.model.events.impl.creature.player.OnPlayerLogout;
import org.l2jdd.gameserver.model.events.listeners.ConsumerEventListener;

/**
 * @author Mobius
 */
public class PremiumManager
{
	private static final Logger LOGGER = Logger.getLogger(PremiumManager.class.getName());
	
	private static final String LOAD_SQL = "SELECT account_name,enddate FROM account_premium WHERE account_name = ?";
	private static final String UPDATE_SQL = "REPLACE INTO account_premium (account_name,enddate) VALUE (?,?)";
	private static final String DELETE_SQL = "DELETE FROM account_premium WHERE account_name = ?";
	
	class PremiumExpireTask implements Runnable
	{
		final PlayerInstance _player;
		
		PremiumExpireTask(PlayerInstance player)
		{
			_player = player;
		}
		
		@Override
		public void run()
		{
			_player.setPremiumStatus(false);
		}
	}
	
	// Data Cache
	private final Map<String, Long> _premiumData = new ConcurrentHashMap<>();
	
	// expireTasks
	private final Map<String, ScheduledFuture<?>> _expiretasks = new ConcurrentHashMap<>();
	
	// Listeners
	private final ListenersContainer _listenerContainer = Containers.Players();
	
	private final Consumer<OnPlayerLogin> _playerLoginEvent = event ->
	{
		final PlayerInstance player = event.getPlayer();
		final String accountName = player.getAccountName();
		loadPremiumData(accountName);
		final long now = Chronos.currentTimeMillis();
		final long premiumExpiration = getPremiumExpiration(accountName);
		player.setPremiumStatus(premiumExpiration > now);
		if (player.hasPremiumStatus())
		{
			startExpireTask(player, premiumExpiration - now);
		}
		else if (premiumExpiration > 0)
		{
			removePremiumStatus(accountName, false);
		}
	};
	
	private final Consumer<OnPlayerLogout> _playerLogoutEvent = event ->
	{
		stopExpireTask(event.getPlayer());
	};
	
	protected PremiumManager()
	{
		_listenerContainer.addListener(new ConsumerEventListener(_listenerContainer, EventType.ON_PLAYER_LOGIN, _playerLoginEvent, this));
		_listenerContainer.addListener(new ConsumerEventListener(_listenerContainer, EventType.ON_PLAYER_LOGOUT, _playerLogoutEvent, this));
	}
	
	/**
	 * @param player
	 * @param delay
	 */
	private void startExpireTask(PlayerInstance player, long delay)
	{
		_expiretasks.put(player.getAccountName(), ThreadPool.schedule(new PremiumExpireTask(player), delay));
	}
	
	/**
	 * @param player
	 */
	private void stopExpireTask(PlayerInstance player)
	{
		ScheduledFuture<?> task = _expiretasks.remove(player.getAccountName());
		if (task != null)
		{
			task.cancel(false);
			task = null;
		}
	}
	
	public void loadPremiumData(String accountName)
	{
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(LOAD_SQL))
		{
			stmt.setString(1, accountName);
			try (ResultSet rset = stmt.executeQuery())
			{
				while (rset.next())
				{
					_premiumData.put(rset.getString(1), rset.getLong(2));
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.warning("Problem with PremiumManager: " + e.getMessage());
		}
	}
	
	public long getPremiumExpiration(String accountName)
	{
		return _premiumData.getOrDefault(accountName, 0L);
	}
	
	public void addPremiumTime(String accountName, int timeValue, TimeUnit timeUnit)
	{
		final long addTime = timeUnit.toMillis(timeValue);
		final long now = Chronos.currentTimeMillis();
		// new premium task at least from now
		final long oldPremiumExpiration = Math.max(now, getPremiumExpiration(accountName));
		final long newPremiumExpiration = oldPremiumExpiration + addTime;
		
		// UPDATE DATABASE
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_SQL))
		{
			stmt.setString(1, accountName);
			stmt.setLong(2, newPremiumExpiration);
			stmt.execute();
		}
		catch (SQLException e)
		{
			LOGGER.warning("Problem with PremiumManager: " + e.getMessage());
		}
		
		// UPDATE CACHE
		_premiumData.put(accountName, newPremiumExpiration);
		
		// UPDATE PlAYER PREMIUMSTATUS
		for (PlayerInstance player : World.getInstance().getPlayers())
		{
			if (accountName.equals(player.getAccountName()))
			{
				stopExpireTask(player);
				startExpireTask(player, newPremiumExpiration - now);
				if (!player.hasPremiumStatus())
				{
					player.setPremiumStatus(true);
				}
				break;
			}
		}
	}
	
	public void removePremiumStatus(String accountName, boolean checkOnline)
	{
		if (checkOnline)
		{
			for (PlayerInstance player : World.getInstance().getPlayers())
			{
				if (accountName.equals(player.getAccountName()) && player.hasPremiumStatus())
				{
					player.setPremiumStatus(false);
					stopExpireTask(player);
					break;
				}
			}
		}
		
		// UPDATE CACHE
		_premiumData.remove(accountName);
		
		// UPDATE DATABASE
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_SQL))
		{
			stmt.setString(1, accountName);
			stmt.execute();
		}
		catch (SQLException e)
		{
			LOGGER.warning("Problem with PremiumManager: " + e.getMessage());
		}
	}
	
	public static PremiumManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final PremiumManager INSTANCE = new PremiumManager();
	}
}