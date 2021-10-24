
package ai.others.Spawns;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jdd.gameserver.GameTimeController;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.ListenerRegisterType;
import org.l2jdd.gameserver.model.events.annotations.RegisterEvent;
import org.l2jdd.gameserver.model.events.annotations.RegisterType;
import org.l2jdd.gameserver.model.events.impl.OnDayNightChange;
import org.l2jdd.gameserver.model.spawns.SpawnTemplate;

import ai.AbstractNpcAI;

/**
 * @author UnAfraid
 */
public class DayNightSpawns extends AbstractNpcAI
{
	private static final String NIGHT_GROUP_NAME = "nightTime";
	private static final String DAY_GROUP_NAME = "dayTime";
	private final Set<SpawnTemplate> _templates = ConcurrentHashMap.newKeySet();
	
	private DayNightSpawns()
	{
	}
	
	@Override
	public void onSpawnActivate(SpawnTemplate template)
	{
		if (_templates.add(template))
		{
			manageSpawns(template, GameTimeController.getInstance().isNight());
		}
	}
	
	@Override
	public void onSpawnDeactivate(SpawnTemplate template)
	{
		_templates.remove(template);
	}
	
	@RegisterEvent(EventType.ON_DAY_NIGHT_CHANGE)
	@RegisterType(ListenerRegisterType.GLOBAL)
	public void onDayNightChange(OnDayNightChange event)
	{
		_templates.forEach(template -> manageSpawns(template, event.isNight()));
	}
	
	private void manageSpawns(SpawnTemplate template, boolean isNight)
	{
		template.getGroups().forEach(group ->
		{
			if (NIGHT_GROUP_NAME.equalsIgnoreCase(group.getName()))
			{
				if (!isNight)
				{
					group.despawnAll();
				}
				else
				{
					group.spawnAll();
				}
			}
			else if (DAY_GROUP_NAME.equalsIgnoreCase(group.getName()))
			{
				if (isNight)
				{
					group.despawnAll();
				}
				else
				{
					group.spawnAll();
				}
			}
		});
	}
	
	public static void main(String[] args)
	{
		new DayNightSpawns();
	}
}
