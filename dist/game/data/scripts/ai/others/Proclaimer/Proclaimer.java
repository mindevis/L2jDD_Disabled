
package ai.others.Proclaimer;

import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.holders.SkillHolder;
import org.l2jdd.gameserver.model.skills.SkillCaster;
import org.l2jdd.gameserver.network.NpcStringId;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jdd.gameserver.network.serverpackets.NpcSay;

import ai.AbstractNpcAI;

/**
 * Proclaimer AI.
 * @author St3eT
 */
public class Proclaimer extends AbstractNpcAI
{
	// NPCs
	private static final int[] PROCLAIMER =
	{
		36609, // Gludio
		36610, // Dion
		36611, // Giran
		36612, // Oren
		36613, // Aden
		36614, // Innadril
		36615, // Goddard
		36616, // Rune
		36617, // Schuttgart
	};
	// Skills
	private static final SkillHolder XP_BUFF = new SkillHolder(19036, 1); // Blessing of Light
	
	private Proclaimer()
	{
		addStartNpc(PROCLAIMER);
		addFirstTalkId(PROCLAIMER);
		addTalkId(PROCLAIMER);
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		if (!player.isOnDarkSide())
		{
			player.sendPacket(new NpcSay(npc.getObjectId(), ChatType.WHISPER, npc.getId(), NpcStringId.WHEN_THE_WORLD_PLUNGES_INTO_CHAOS_WE_WILL_NEED_YOUR_HELP_WE_HOPE_YOU_JOIN_US_WHEN_THE_TIME_COMES));
			
			final Clan ownerClan = npc.getCastle().getOwner();
			if (ownerClan != null)
			{
				final NpcHtmlMessage packet = new NpcHtmlMessage(npc.getObjectId());
				packet.setHtml(getHtm(player, "proclaimer.html"));
				packet.replace("%leaderName%", ownerClan.getLeaderName());
				packet.replace("%clanName%", ownerClan.getName());
				packet.replace("%castleName%", npc.getCastle().getName());
				player.sendPacket(packet);
			}
		}
		else
		{
			htmltext = "proclaimer-01.html";
		}
		return htmltext;
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		if (event.equals("giveBuff"))
		{
			if (!player.isOnDarkSide())
			{
				SkillCaster.triggerCast(npc, player, XP_BUFF.getSkill());
			}
			else
			{
				htmltext = "proclaimer-01.html";
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Proclaimer();
	}
}