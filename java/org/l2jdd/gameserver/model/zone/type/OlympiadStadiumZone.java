
package org.l2jdd.gameserver.model.zone.type;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.enums.TeleportWhereType;
import org.l2jdd.gameserver.instancemanager.ZoneManager;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.PlayerCondOverride;
import org.l2jdd.gameserver.model.Spawn;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Summon;
import org.l2jdd.gameserver.model.actor.instance.DoorInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.olympiad.OlympiadGameTask;
import org.l2jdd.gameserver.model.zone.AbstractZoneSettings;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.model.zone.ZoneRespawn;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExOlympiadMatchEnd;

/**
 * An olympiad stadium
 * @author durgus, DS
 */
public class OlympiadStadiumZone extends ZoneRespawn
{
	private final List<DoorInstance> _doors = new ArrayList<>(2);
	private final List<Spawn> _buffers = new ArrayList<>(2);
	private final List<Location> _spectatorLocations = new ArrayList<>(1);
	
	public OlympiadStadiumZone(int id)
	{
		super(id);
		AbstractZoneSettings settings = ZoneManager.getSettings(getName());
		if (settings == null)
		{
			settings = new Settings();
		}
		setSettings(settings);
	}
	
	public class Settings extends AbstractZoneSettings
	{
		private OlympiadGameTask _task = null;
		
		protected Settings()
		{
		}
		
		public OlympiadGameTask getOlympiadTask()
		{
			return _task;
		}
		
		protected void setTask(OlympiadGameTask task)
		{
			_task = task;
		}
		
		@Override
		public void clear()
		{
			_task = null;
		}
	}
	
	@Override
	public Settings getSettings()
	{
		return (Settings) super.getSettings();
	}
	
	@Override
	public void parseLoc(int x, int y, int z, String type)
	{
		if ((type != null) && type.equals("spectatorSpawn"))
		{
			_spectatorLocations.add(new Location(x, y, z));
		}
		else
		{
			super.parseLoc(x, y, z, type);
		}
	}
	
	public void registerTask(OlympiadGameTask task)
	{
		getSettings().setTask(task);
	}
	
	@Override
	protected final void onEnter(Creature creature)
	{
		if ((getSettings().getOlympiadTask() != null) && getSettings().getOlympiadTask().isBattleStarted())
		{
			creature.setInsideZone(ZoneId.PVP, true);
			if (creature.isPlayer())
			{
				creature.sendPacket(SystemMessageId.YOU_HAVE_ENTERED_A_COMBAT_ZONE);
				getSettings().getOlympiadTask().getGame().sendOlympiadInfo(creature);
			}
		}
		
		if (creature.isPlayable())
		{
			final PlayerInstance player = creature.getActingPlayer();
			if (player != null)
			{
				// only participants, observers and GMs allowed
				if (!player.canOverrideCond(PlayerCondOverride.ZONE_CONDITIONS) && !player.isInOlympiadMode() && !player.inObserverMode())
				{
					ThreadPool.execute(new KickPlayer(player));
				}
				else
				{
					// check for pet
					final Summon pet = player.getPet();
					if (pet != null)
					{
						pet.unSummon(player);
					}
				}
			}
		}
	}
	
	@Override
	protected final void onExit(Creature creature)
	{
		if ((getSettings().getOlympiadTask() != null) && getSettings().getOlympiadTask().isBattleStarted())
		{
			creature.setInsideZone(ZoneId.PVP, false);
			if (creature.isPlayer())
			{
				creature.sendPacket(SystemMessageId.YOU_HAVE_LEFT_A_COMBAT_ZONE);
				creature.sendPacket(ExOlympiadMatchEnd.STATIC_PACKET);
			}
		}
	}
	
	private static final class KickPlayer implements Runnable
	{
		private PlayerInstance _player;
		
		protected KickPlayer(PlayerInstance player)
		{
			_player = player;
		}
		
		@Override
		public void run()
		{
			if (_player != null)
			{
				_player.getServitors().values().forEach(s -> s.unSummon(_player));
				_player.teleToLocation(TeleportWhereType.TOWN, null);
				_player = null;
			}
		}
	}
	
	public List<DoorInstance> getDoors()
	{
		return _doors;
	}
	
	public List<Spawn> getBuffers()
	{
		return _buffers;
	}
	
	public List<Location> getSpectatorSpawns()
	{
		return _spectatorLocations;
	}
}
