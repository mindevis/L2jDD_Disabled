
package events;

import java.util.HashMap;
import java.util.Map;

import org.l2jdd.gameserver.instancemanager.EventShrineManager;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.quest.Quest;
import org.l2jdd.gameserver.model.zone.ZoneType;
import org.l2jdd.gameserver.network.serverpackets.OnEventTrigger;

/**
 * @author hlwrave, Mobius
 * @Add in event config.xml enableShrines="true" after event name to enable them.
 */
public class EventShrines extends Quest
{
	private static final Map<Integer, Integer> ZONE_TRIGGERS = new HashMap<>();
	static
	{
		ZONE_TRIGGERS.put(11030, 23206292); // Hunter
		ZONE_TRIGGERS.put(11031, 24186292); // Aden
		ZONE_TRIGGERS.put(11032, 24166292); // Goddard
		ZONE_TRIGGERS.put(11035, 22136292); // Shuttgard
		ZONE_TRIGGERS.put(11028, 20226292); // Dion
		ZONE_TRIGGERS.put(11029, 22196292); // Oren
		ZONE_TRIGGERS.put(11020, 22226292); // Giran
		ZONE_TRIGGERS.put(11027, 19216292); // Gludio
		ZONE_TRIGGERS.put(11034, 23246292); // Heine
		ZONE_TRIGGERS.put(11025, 17226292); // Gluddin
		ZONE_TRIGGERS.put(11033, 21166292); // Rune
		ZONE_TRIGGERS.put(11042, 17256292); // Faeron
		ZONE_TRIGGERS.put(11043, 26206292); // Arcan
		ZONE_TRIGGERS.put(11022, 16256292); // Talking Island
	}
	
	public EventShrines()
	{
		super(-1);
		addEnterZoneId(ZONE_TRIGGERS.keySet());
	}
	
	@Override
	public String onEnterZone(Creature creature, ZoneType zone)
	{
		if (creature.isPlayer())
		{
			if (EventShrineManager.getInstance().areShrinesEnabled())
			{
				creature.sendPacket(new OnEventTrigger(ZONE_TRIGGERS.get(zone.getId()), true));
			}
			else // Deactivate shrine.
			{
				creature.sendPacket(new OnEventTrigger(ZONE_TRIGGERS.get(zone.getId()) + 2, true));
			}
		}
		return super.onEnterZone(creature, zone);
	}
	
	public static void main(String[] args)
	{
		new EventShrines();
	}
}