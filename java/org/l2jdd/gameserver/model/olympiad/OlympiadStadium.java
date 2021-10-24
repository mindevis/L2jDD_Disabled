
package org.l2jdd.gameserver.model.olympiad;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.instancemanager.InstanceManager;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.Spawn;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.DoorInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.instancezone.Instance;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.model.zone.type.OlympiadStadiumZone;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExOlympiadMatchEnd;
import org.l2jdd.gameserver.network.serverpackets.ExOlympiadUserInfo;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @author JIV
 */
public class OlympiadStadium
{
	private static final Logger LOGGER = Logger.getLogger(OlympiadStadium.class.getName());
	private final OlympiadStadiumZone _zone;
	private final Instance _instance;
	private final List<Spawn> _buffers;
	private OlympiadGameTask _task = null;
	
	protected OlympiadStadium(OlympiadStadiumZone olyzone, int stadium)
	{
		_zone = olyzone;
		_instance = InstanceManager.getInstance().createInstance(olyzone.getInstanceTemplateId(), null);
		_buffers = _instance.getNpcs().stream().map(Npc::getSpawn).collect(Collectors.toList());
		_buffers.stream().map(Spawn::getLastSpawn).forEach(Npc::deleteMe);
	}
	
	public OlympiadStadiumZone getZone()
	{
		return _zone;
	}
	
	public void registerTask(OlympiadGameTask task)
	{
		_task = task;
	}
	
	public OlympiadGameTask getTask()
	{
		return _task;
	}
	
	public Instance getInstance()
	{
		return _instance;
	}
	
	public void openDoors()
	{
		_instance.getDoors().forEach(DoorInstance::openMe);
	}
	
	public void closeDoors()
	{
		_instance.getDoors().forEach(DoorInstance::closeMe);
	}
	
	public void spawnBuffers()
	{
		_buffers.forEach(spawn -> spawn.doSpawn(false));
	}
	
	public void deleteBuffers()
	{
		_buffers.stream().map(Spawn::getLastSpawn).filter(Objects::nonNull).forEach(Npc::deleteMe);
	}
	
	public void broadcastStatusUpdate(PlayerInstance player)
	{
		final ExOlympiadUserInfo packet = new ExOlympiadUserInfo(player);
		for (PlayerInstance target : _instance.getPlayers())
		{
			if (target.inObserverMode() || (target.getOlympiadSide() != player.getOlympiadSide()))
			{
				target.sendPacket(packet);
			}
		}
	}
	
	public void broadcastPacket(IClientOutgoingPacket packet)
	{
		_instance.broadcastPacket(packet);
	}
	
	public void broadcastPacketToObservers(IClientOutgoingPacket packet)
	{
		for (PlayerInstance target : _instance.getPlayers())
		{
			if (target.inObserverMode())
			{
				target.sendPacket(packet);
			}
		}
	}
	
	public void updateZoneStatusForCharactersInside()
	{
		if (_task == null)
		{
			return;
		}
		
		final boolean battleStarted = _task.isBattleStarted();
		final SystemMessage sm;
		if (battleStarted)
		{
			sm = new SystemMessage(SystemMessageId.YOU_HAVE_ENTERED_A_COMBAT_ZONE);
		}
		else
		{
			sm = new SystemMessage(SystemMessageId.YOU_HAVE_LEFT_A_COMBAT_ZONE);
		}
		
		for (PlayerInstance player : _instance.getPlayers())
		{
			if (player.inObserverMode())
			{
				return;
			}
			
			if (battleStarted)
			{
				player.setInsideZone(ZoneId.PVP, true);
				player.sendPacket(sm);
			}
			else
			{
				player.setInsideZone(ZoneId.PVP, false);
				player.sendPacket(sm);
				player.sendPacket(ExOlympiadMatchEnd.STATIC_PACKET);
			}
		}
	}
	
	public void updateZoneInfoForObservers()
	{
		if (_task == null)
		{
			return;
		}
		
		for (PlayerInstance player : _instance.getPlayers())
		{
			if (!player.inObserverMode())
			{
				return;
			}
			
			final OlympiadGameTask nextArena = OlympiadGameManager.getInstance().getOlympiadTask(player.getOlympiadGameId());
			final List<Location> spectatorSpawns = nextArena.getStadium().getZone().getSpectatorSpawns();
			if (spectatorSpawns.isEmpty())
			{
				LOGGER.warning(getClass().getSimpleName() + ": Zone: " + nextArena.getStadium().getZone() + " doesn't have specatator spawns defined!");
				return;
			}
			final Location loc = spectatorSpawns.get(Rnd.get(spectatorSpawns.size()));
			player.enterOlympiadObserverMode(loc, player.getOlympiadGameId());
		}
	}
}