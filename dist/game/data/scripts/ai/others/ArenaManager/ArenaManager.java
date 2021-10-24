
package ai.others.ArenaManager;

import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.itemcontainer.Inventory;
import org.l2jdd.gameserver.model.skills.SkillCaster;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.network.SystemMessageId;

import ai.AbstractNpcAI;

/**
 * Arena Manager AI.
 * @author St3eT
 */
public class ArenaManager extends AbstractNpcAI
{
	// NPCs
	private static final int[] ARENA_MANAGER =
	{
		31225, // Arena Director (MDT)
		31226, // Arena Manager (Coliseum)
	};
	// Skills
	private static final SkillHolder[] BUFFS =
	{
		new SkillHolder(6805, 1), // Arena Empower
		new SkillHolder(6806, 1), // Arena Acumen
		new SkillHolder(6807, 1), // Arena Concentration
		new SkillHolder(6808, 1), // Arena Might
		new SkillHolder(6804, 1), // Arena Wind Walk
		new SkillHolder(6812, 1), // Arena Berserker Spirit
	};
	private static final SkillHolder CP_RECOVERY = new SkillHolder(4380, 1); // Arena: CP Recovery
	private static final SkillHolder HP_RECOVERY = new SkillHolder(6817, 1); // Arena HP Recovery
	// Misc
	private static final int CP_COST = 1000;
	private static final int HP_COST = 1000;
	private static final int BUFF_COST = 2000;
	
	private ArenaManager()
	{
		addStartNpc(ARENA_MANAGER);
		addTalkId(ARENA_MANAGER);
		addFirstTalkId(ARENA_MANAGER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		switch (event)
		{
			case "CPrecovery":
			{
				if (player.getAdena() >= CP_COST)
				{
					takeItems(player, Inventory.ADENA_ID, CP_COST);
					startQuestTimer("CPrecovery_delay", 2000, npc, player);
				}
				else
				{
					player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				}
				break;
			}
			case "HPrecovery":
			{
				if (player.getAdena() >= HP_COST)
				{
					takeItems(player, Inventory.ADENA_ID, HP_COST);
					startQuestTimer("HPrecovery_delay", 2000, npc, player);
				}
				else
				{
					player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				}
				break;
			}
			case "Buff":
			{
				if (player.getAdena() >= BUFF_COST)
				{
					takeItems(player, Inventory.ADENA_ID, BUFF_COST);
					for (SkillHolder skill : BUFFS)
					{
						SkillCaster.triggerCast(npc, player, skill.getSkill());
					}
				}
				else
				{
					player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				}
				break;
			}
			case "CPrecovery_delay":
			{
				if ((player != null) && !player.isInsideZone(ZoneId.PVP))
				{
					npc.setTarget(player);
					npc.doCast(CP_RECOVERY.getSkill());
				}
				break;
			}
			case "HPrecovery_delay":
			{
				if ((player != null) && !player.isInsideZone(ZoneId.PVP))
				{
					npc.setTarget(player);
					npc.doCast(HP_RECOVERY.getSkill());
				}
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	public static void main(String[] args)
	{
		new ArenaManager();
	}
}