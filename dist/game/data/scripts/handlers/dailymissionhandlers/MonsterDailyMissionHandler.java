
package handlers.dailymissionhandlers;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.Config;
import org.l2jdd.gameserver.enums.DailyMissionStatus;
import org.l2jdd.gameserver.handler.AbstractDailyMissionHandler;
import org.l2jdd.gameserver.model.CommandChannel;
import org.l2jdd.gameserver.model.DailyMissionDataHolder;
import org.l2jdd.gameserver.model.DailyMissionPlayerEntry;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.Containers;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.creature.npc.OnAttackableKill;
import org.l2jdd.gameserver.model.events.listeners.ConsumerEventListener;

/**
 * @author Mobius
 */
public class MonsterDailyMissionHandler extends AbstractDailyMissionHandler
{
	private final int _amount;
	private final int _minLevel;
	private final int _maxLevel;
	private final List<Integer> _ids = new ArrayList<>();
	
	public MonsterDailyMissionHandler(DailyMissionDataHolder holder)
	{
		super(holder);
		_amount = holder.getRequiredCompletions();
		_minLevel = holder.getParams().getInt("minLevel", 0);
		_maxLevel = holder.getParams().getInt("maxLevel", Integer.MAX_VALUE);
		final String ids = holder.getParams().getString("ids", "");
		if (!ids.isEmpty())
		{
			for (String s : ids.split(","))
			{
				final int id = Integer.parseInt(s);
				if (!_ids.contains(id))
				{
					_ids.add(id);
				}
			}
		}
	}
	
	@Override
	public void init()
	{
		Containers.Monsters().addListener(new ConsumerEventListener(this, EventType.ON_ATTACKABLE_KILL, (OnAttackableKill event) -> onAttackableKill(event), this));
	}
	
	@Override
	public boolean isAvailable(PlayerInstance player)
	{
		final DailyMissionPlayerEntry entry = getPlayerEntry(player.getObjectId(), false);
		if (entry != null)
		{
			switch (entry.getStatus())
			{
				case NOT_AVAILABLE: // Initial state
				{
					if (entry.getProgress() >= _amount)
					{
						entry.setStatus(DailyMissionStatus.AVAILABLE);
						storePlayerEntry(entry);
					}
					break;
				}
				case AVAILABLE:
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private void onAttackableKill(OnAttackableKill event)
	{
		final Attackable monster = event.getTarget();
		if (!_ids.isEmpty() && !_ids.contains(monster.getId()))
		{
			return;
		}
		
		final PlayerInstance player = event.getAttacker();
		if (_minLevel > 0)
		{
			final int monsterLevel = monster.getLevel();
			if ((monsterLevel < _minLevel) || (monsterLevel > _maxLevel) || ((player.getLevel() - monsterLevel) > 5))
			{
				return;
			}
		}
		
		final Party party = player.getParty();
		if (party != null)
		{
			final CommandChannel channel = party.getCommandChannel();
			final List<PlayerInstance> members = channel != null ? channel.getMembers() : party.getMembers();
			members.stream().filter(member -> member.calculateDistance3D(monster) <= Config.ALT_PARTY_RANGE).forEach(this::processPlayerProgress);
		}
		else
		{
			processPlayerProgress(player);
		}
	}
	
	private void processPlayerProgress(PlayerInstance player)
	{
		final DailyMissionPlayerEntry entry = getPlayerEntry(player.getObjectId(), true);
		if (entry.getStatus() == DailyMissionStatus.NOT_AVAILABLE)
		{
			if (entry.increaseProgress() >= _amount)
			{
				entry.setStatus(DailyMissionStatus.AVAILABLE);
			}
			storePlayerEntry(entry);
		}
	}
}
