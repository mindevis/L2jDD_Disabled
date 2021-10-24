
package org.l2jdd.gameserver.model.zone.type;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.instancemanager.ZoneManager;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.model.stats.Stat;
import org.l2jdd.gameserver.model.zone.AbstractZoneSettings;
import org.l2jdd.gameserver.model.zone.TaskZoneSettings;
import org.l2jdd.gameserver.model.zone.ZoneType;

/**
 * A damage zone
 * @author durgus
 */
public class DamageZone extends ZoneType
{
	private int _damageHPPerSec;
	private int _damageMPPerSec;
	
	private int _castleId;
	private Castle _castle;
	
	private int _startTask;
	private int _reuseTask;
	
	public DamageZone(int id)
	{
		super(id);
		
		// Setup default damage
		_damageHPPerSec = 200;
		_damageMPPerSec = 0;
		
		// Setup default start / reuse time
		_startTask = 10;
		_reuseTask = 5000;
		
		// no castle by default
		_castleId = 0;
		_castle = null;
		
		setTargetType(InstanceType.Playable); // default only playabale
		AbstractZoneSettings settings = ZoneManager.getSettings(getName());
		if (settings == null)
		{
			settings = new TaskZoneSettings();
		}
		setSettings(settings);
	}
	
	@Override
	public TaskZoneSettings getSettings()
	{
		return (TaskZoneSettings) super.getSettings();
	}
	
	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("dmgHPSec"))
		{
			_damageHPPerSec = Integer.parseInt(value);
		}
		else if (name.equals("dmgMPSec"))
		{
			_damageMPPerSec = Integer.parseInt(value);
		}
		else if (name.equals("castleId"))
		{
			_castleId = Integer.parseInt(value);
		}
		else if (name.equalsIgnoreCase("initialDelay"))
		{
			_startTask = Integer.parseInt(value);
		}
		else if (name.equalsIgnoreCase("reuse"))
		{
			_reuseTask = Integer.parseInt(value);
		}
		else
		{
			super.setParameter(name, value);
		}
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
		if ((getSettings().getTask() == null) && ((_damageHPPerSec != 0) || (_damageMPPerSec != 0)))
		{
			final PlayerInstance player = creature.getActingPlayer();
			if (getCastle() != null) // Castle zone
			{
				if (!(getCastle().getSiege().isInProgress() && (player != null) && (player.getSiegeState() != 2))) // Siege and no defender
				{
					return;
				}
			}
			
			synchronized (this)
			{
				if (getSettings().getTask() == null)
				{
					getSettings().setTask(ThreadPool.scheduleAtFixedRate(new ApplyDamage(this), _startTask, _reuseTask));
				}
			}
		}
	}
	
	@Override
	protected void onExit(Creature creature)
	{
		if (getCharactersInside().isEmpty() && (getSettings().getTask() != null))
		{
			getSettings().clear();
		}
	}
	
	protected int getHPDamagePerSecond()
	{
		return _damageHPPerSec;
	}
	
	protected int getMPDamagePerSecond()
	{
		return _damageMPPerSec;
	}
	
	protected Castle getCastle()
	{
		if ((_castleId > 0) && (_castle == null))
		{
			_castle = CastleManager.getInstance().getCastleById(_castleId);
		}
		
		return _castle;
	}
	
	private final class ApplyDamage implements Runnable
	{
		private final DamageZone _dmgZone;
		private final Castle _castle;
		
		protected ApplyDamage(DamageZone zone)
		{
			_dmgZone = zone;
			_castle = zone.getCastle();
		}
		
		@Override
		public void run()
		{
			if (!isEnabled())
			{
				return;
			}
			
			boolean siege = false;
			
			if (_castle != null)
			{
				siege = _castle.getSiege().isInProgress();
				// castle zones active only during siege
				if (!siege)
				{
					_dmgZone.getSettings().clear();
					return;
				}
			}
			
			for (Creature temp : _dmgZone.getCharactersInside())
			{
				if ((temp != null) && !temp.isDead())
				{
					if (siege)
					{
						// during siege defenders not affected
						final PlayerInstance player = temp.getActingPlayer();
						if ((player != null) && player.isInSiege() && (player.getSiegeState() == 2))
						{
							continue;
						}
					}
					
					final double multiplier = 1 + (temp.getStat().getValue(Stat.DAMAGE_ZONE_VULN, 0) / 100);
					
					if (getHPDamagePerSecond() != 0)
					{
						temp.reduceCurrentHp(_dmgZone.getHPDamagePerSecond() * multiplier, temp, null);
					}
					if (getMPDamagePerSecond() != 0)
					{
						temp.reduceCurrentMp(_dmgZone.getMPDamagePerSecond() * multiplier);
					}
				}
			}
		}
	}
}
