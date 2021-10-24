
package ai.bosses.Camille;

import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.DoorInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.instancezone.Instance;

import instances.AbstractInstance;

/**
 * Camille instance zone.
 * @author Sero
 * @URL https://www.youtube.com/watch?v=jpv9S_xQVrA
 */
public class Camille extends AbstractInstance
{
	// NPCs
	private static final int CAMILLE = 26236;
	private static final int MAMUT = 26243;
	private static final int ISBURG = 26244;
	private static final int TRANSMISSION_UNIT = 34324;
	private static final int ERDA = 34319;
	// Locations
	private static final Location ENTER_LOCATION = new Location(-245768, 147832, 4662);
	private static final Location CAMILLE_LOCATION = new Location(-245752, 150392, 11845);
	// Misc
	private static final int TEMPLATE_ID = 266;
	
	public Camille()
	{
		super(TEMPLATE_ID);
		addStartNpc(ERDA);
		addTalkId(ERDA, TRANSMISSION_UNIT);
		addFirstTalkId(TRANSMISSION_UNIT);
		addKillId(CAMILLE, MAMUT, ISBURG);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "enterInstance":
			{
				enterInstance(player, npc, TEMPLATE_ID);
				final Instance world = player.getInstanceWorld();
				if (isInInstance(world))
				{
					world.getPlayers().forEach(p -> p.teleToLocation(ENTER_LOCATION));
					world.getDoors().forEach(DoorInstance::closeMe);
				}
				break;
			}
			case "teleup":
			{
				final Instance world = npc.getInstanceWorld();
				if (isInInstance(world) && (npc.getId() == TRANSMISSION_UNIT))
				{
					world.getPlayers().forEach(p -> p.teleToLocation(CAMILLE_LOCATION));
					world.getDoors().forEach(DoorInstance::closeMe);
				}
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance player, boolean isSummon)
	{
		final Instance world = npc.getInstanceWorld();
		if (isInInstance(world))
		{
			switch (npc.getId())
			{
				case MAMUT:
				{
					world.openCloseDoor(world.getTemplateParameters().getInt("firstDoorId"), true);
					world.openCloseDoor(world.getTemplateParameters().getInt("secondDoorId"), true);
					world.setReenterTime();
					break;
				}
				case ISBURG:
				{
					world.spawnGroup("teleport");
					world.setReenterTime();
					break;
				}
				case CAMILLE:
				{
					world.finishInstance();
					break;
				}
			}
		}
		return super.onKill(npc, player, isSummon);
	}
	
	public static void main(String[] args)
	{
		new Camille();
	}
}