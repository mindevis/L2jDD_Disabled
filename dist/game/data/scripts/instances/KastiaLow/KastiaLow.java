
package instances.KastiaLow;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.ItemHolder;
import org.l2jdd.gameserver.model.instancezone.Instance;
import org.l2jdd.gameserver.network.NpcStringId;
import org.l2jdd.gameserver.network.serverpackets.ExShowScreenMessage;

import instances.AbstractInstance;

/**
 * @author Mobius
 */
public class KastiaLow extends AbstractInstance
{
	// NPC
	private static final int KARINIA = 34541;
	// Monsters
	private static final int[] MONSTERS =
	{
		24535, // Kastia's Keeper
		24536, // Kastia's Overseer
		24537, // Kastia's Warder
		24538, // Kalix
	};
	// Item
	private static final ItemHolder KASTIAS_SMALL_PACK = new ItemHolder(81147, 1);
	// Misc
	private static final int TEMPLATE_ID = 298;
	
	public KastiaLow()
	{
		super(TEMPLATE_ID);
		addStartNpc(KARINIA);
		addTalkId(KARINIA);
		addKillId(MONSTERS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "enterInstance":
			{
				/*
				 * Cannot enter if player finished another Kastia instance. if ((Chronos.currentTimeMillis() > InstanceManager.getInstance().getInstanceTime(player, 299)) || (Chronos.currentTimeMillis() > InstanceManager.getInstance().getInstanceTime(player, 298))) { player.sendPacket(new
				 * SystemMessage(SystemMessageId.SINCE_C1_ENTERED_ANOTHER_INSTANCE_ZONE_THEREFORE_YOU_CANNOT_ENTER_THIS_DUNGEON).addString(player.getName())); return null; }
				 */
				enterInstance(player, npc, TEMPLATE_ID);
				if (player.getInstanceWorld() != null)
				{
					startQuestTimer("check_status", 10000, null, player);
				}
				return null;
			}
			case "check_status":
			{
				final Instance world = player.getInstanceWorld();
				if (!isInInstance(world))
				{
					return null;
				}
				switch (world.getStatus())
				{
					case 0:
					{
						showOnScreenMsg(world, NpcStringId.STAGE_1, ExShowScreenMessage.TOP_CENTER, 10000, true);
						world.setStatus(1);
						world.spawnGroup("wave_1");
						startQuestTimer("check_status", 10000, null, player);
						break;
					}
					case 1:
					{
						if (world.getAliveNpcs().isEmpty())
						{
							showOnScreenMsg(world, NpcStringId.STAGE_2, ExShowScreenMessage.TOP_CENTER, 10000, true);
							world.setStatus(2);
							world.spawnGroup("wave_2");
						}
						startQuestTimer("check_status", 10000, null, player);
						break;
					}
					case 2:
					{
						if (world.getAliveNpcs().isEmpty())
						{
							showOnScreenMsg(world, NpcStringId.STAGE_3, ExShowScreenMessage.TOP_CENTER, 10000, true);
							world.setStatus(3);
							world.spawnGroup("wave_3");
						}
						startQuestTimer("check_status", 10000, null, player);
						break;
					}
					case 3:
					{
						if (world.getAliveNpcs().isEmpty())
						{
							showOnScreenMsg(world, NpcStringId.STAGE_4, ExShowScreenMessage.TOP_CENTER, 10000, true);
							world.setStatus(4);
							world.spawnGroup("wave_4");
						}
						startQuestTimer("check_status", 10000, null, player);
						break;
					}
					case 4:
					{
						if (world.getAliveNpcs().isEmpty())
						{
							showOnScreenMsg(world, NpcStringId.STAGE_5, ExShowScreenMessage.TOP_CENTER, 10000, true);
							world.setStatus(5);
							world.spawnGroup("wave_5");
						}
						startQuestTimer("check_status", 10000, null, player);
						break;
					}
					case 5:
					{
						if (world.getAliveNpcs().isEmpty())
						{
							showOnScreenMsg(world, NpcStringId.STAGE_6, ExShowScreenMessage.TOP_CENTER, 10000, true);
							world.setStatus(6);
							world.spawnGroup("wave_6");
						}
						startQuestTimer("check_status", 10000, null, player);
						break;
					}
					case 6:
					{
						if (world.getAliveNpcs().isEmpty())
						{
							showOnScreenMsg(world, NpcStringId.STAGE_7, ExShowScreenMessage.TOP_CENTER, 10000, true);
							world.setStatus(7);
							world.spawnGroup("wave_7");
						}
						startQuestTimer("check_status", 10000, null, player);
						break;
					}
					case 7:
					{
						if (world.getAliveNpcs().isEmpty())
						{
							giveItems(player, KASTIAS_SMALL_PACK);
							world.finishInstance();
						}
						else
						{
							startQuestTimer("check_status", 10000, null, player);
						}
						break;
					}
				}
				return null;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	public static void main(String[] args)
	{
		new KastiaLow();
	}
}
