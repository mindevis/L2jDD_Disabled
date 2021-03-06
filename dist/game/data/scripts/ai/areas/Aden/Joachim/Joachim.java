
package ai.areas.Aden.Joachim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.l2jdd.gameserver.data.xml.CategoryData;
import org.l2jdd.gameserver.data.xml.ClassListData;
import org.l2jdd.gameserver.data.xml.SkillTreeData;
import org.l2jdd.gameserver.enums.CategoryType;
import org.l2jdd.gameserver.enums.ClassId;
import org.l2jdd.gameserver.enums.SubclassInfoType;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.ListenerRegisterType;
import org.l2jdd.gameserver.model.events.annotations.Id;
import org.l2jdd.gameserver.model.events.annotations.RegisterEvent;
import org.l2jdd.gameserver.model.events.annotations.RegisterType;
import org.l2jdd.gameserver.model.events.impl.creature.npc.OnNpcMenuSelect;
import org.l2jdd.gameserver.model.quest.QuestState;
import org.l2jdd.gameserver.network.serverpackets.AcquireSkillList;
import org.l2jdd.gameserver.network.serverpackets.ExSubjobInfo;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;

import ai.AbstractNpcAI;
import quests.Q10590_ReawakenedFate.Q10590_ReawakenedFate;

/**
 * @author Mobius
 */
public class Joachim extends AbstractNpcAI
{
	// NPC
	private static final int JOACHIM = 34513;
	// Items
	private static final int CHAOS_POMANDER_DUAL_CLASS = 37375;
	private static final int PAULINAS_RGRADE_EQUIPMENT_SET = 46919;
	// Misc
	private static final List<ClassId> DUAL_CLASS_LIST = new ArrayList<>();
	static
	{
		DUAL_CLASS_LIST.addAll(Arrays.asList(ClassId.SIGEL_PHOENIX_KNIGHT, ClassId.SIGEL_HELL_KNIGHT, ClassId.SIGEL_EVA_TEMPLAR, ClassId.SIGEL_SHILLIEN_TEMPLAR));
		DUAL_CLASS_LIST.addAll(Arrays.asList(ClassId.TYRR_DUELIST, ClassId.TYRR_DREADNOUGHT, ClassId.TYRR_TITAN, ClassId.TYRR_GRAND_KHAVATARI, ClassId.TYRR_DOOMBRINGER));
		DUAL_CLASS_LIST.addAll(Arrays.asList(ClassId.OTHELL_ADVENTURER, ClassId.OTHELL_WIND_RIDER, ClassId.OTHELL_GHOST_HUNTER, ClassId.OTHELL_FORTUNE_SEEKER));
		DUAL_CLASS_LIST.addAll(Arrays.asList(ClassId.YUL_SAGITTARIUS, ClassId.YUL_MOONLIGHT_SENTINEL, ClassId.YUL_GHOST_SENTINEL, ClassId.YUL_TRICKSTER));
		DUAL_CLASS_LIST.addAll(Arrays.asList(ClassId.FEOH_ARCHMAGE, ClassId.FEOH_SOULTAKER, ClassId.FEOH_MYSTIC_MUSE, ClassId.FEOH_STORM_SCREAMER, ClassId.FEOH_SOUL_HOUND));
		DUAL_CLASS_LIST.addAll(Arrays.asList(ClassId.ISS_HIEROPHANT, ClassId.ISS_SWORD_MUSE, ClassId.ISS_SPECTRAL_DANCER, ClassId.ISS_DOOMCRYER));
		DUAL_CLASS_LIST.addAll(Arrays.asList(ClassId.WYNN_ARCANA_LORD, ClassId.WYNN_ELEMENTAL_MASTER, ClassId.WYNN_SPECTRAL_MASTER));
		DUAL_CLASS_LIST.addAll(Arrays.asList(ClassId.AEORE_CARDINAL, ClassId.AEORE_EVA_SAINT, ClassId.AEORE_SHILLIEN_SAINT));
	}
	
	private Joachim()
	{
		addStartNpc(JOACHIM);
		addTalkId(JOACHIM);
		addFirstTalkId(JOACHIM);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		String htmltext = null;
		switch (event)
		{
			case "34513-01.html":
			{
				final QuestState qs = player.getQuestState(Q10590_ReawakenedFate.class.getSimpleName());
				if ((qs == null) || !qs.isCompleted())
				{
					htmltext = "34513-02.html";
				}
				else if (player.hasDualClass())
				{
					htmltext = "34513-03.html";
				}
				else if (player.getLevel() < 105)
				{
					htmltext = "34513-04.html";
				}
				else
				{
					htmltext = "34513-05.html";
				}
				break;
			}
			case "addDualClass_SIXTH_SIGEL_GROUP":
			case "addDualClass_SIXTH_TIR_GROUP":
			case "addDualClass_SIXTH_OTHEL_GROUP":
			case "addDualClass_SIXTH_YR_GROUP":
			case "addDualClass_SIXTH_FEOH_GROUP":
			case "addDualClass_SIXTH_IS_GROUP":
			case "addDualClass_SIXTH_WYNN_GROUP":
			case "addDualClass_SIXTH_EOLH_GROUP":
			{
				final CategoryType cType = CategoryType.valueOf(event.replace("addDualClass_", ""));
				if (cType == null)
				{
					LOGGER.warning(getClass().getSimpleName() + ": Cannot parse CategoryType, event: " + event);
				}
				
				final StringBuilder sb = new StringBuilder();
				final NpcHtmlMessage html = getNpcHtmlMessage(player, npc, "34513-06.html");
				for (ClassId dualClasses : getDualClasses(player, cType))
				{
					if (dualClasses != null)
					{
						sb.append("<button value=\"" + ClassListData.getInstance().getClass(dualClasses.getId()).getClassName() + "\" action=\"bypass -h menu_select?ask=1&reply=" + dualClasses.getId() + "\" width=\"200\" height=\"31\" back=\"L2UI_CT1.HtmlWnd_DF_Awake_Down\" fore=\"L2UI_CT1.HtmlWnd_DF_Awake\"><br>");
					}
				}
				html.replace("%dualclassList%", sb.toString());
				player.sendPacket(html);
				break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		return "34513.html";
	}
	
	private NpcHtmlMessage getNpcHtmlMessage(PlayerInstance player, Npc npc, String fileName)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
		final String text = getHtm(player, fileName);
		if (text == null)
		{
			LOGGER.info("Cannot find HTML file for " + Joachim.class.getSimpleName() + " AI: " + fileName);
			return null;
		}
		html.setHtml(text);
		return html;
	}
	
	private List<ClassId> getDualClasses(PlayerInstance player, CategoryType cType)
	{
		final List<ClassId> tempList = new ArrayList<>();
		final int baseClassId = player.getBaseClass();
		final int dualClassId = player.getClassId().getId();
		for (ClassId temp : DUAL_CLASS_LIST)
		{
			if ((temp.getId() != baseClassId) && (temp.getId() != dualClassId) && ((cType == null) || CategoryData.getInstance().isInCategory(cType, temp.getId())))
			{
				tempList.add(temp);
			}
		}
		return tempList;
	}
	
	@RegisterEvent(EventType.ON_NPC_MENU_SELECT)
	@RegisterType(ListenerRegisterType.NPC)
	@Id(JOACHIM)
	public void OnNpcMenuSelect(OnNpcMenuSelect event)
	{
		final PlayerInstance player = event.getTalker();
		final Npc npc = event.getNpc();
		final int ask = event.getAsk();
		
		switch (ask)
		{
			case 1: // Reawaken (change dual class)
			{
				final int classId = event.getReply();
				if (player.isTransformed() || player.hasSummon() || player.hasDualClass() || !player.isAwakenedClass())
				{
					break;
				}
				
				// Validating classId
				if (!getDualClasses(player, null).contains(ClassId.getClassId(classId)))
				{
					break;
				}
				
				if (player.addSubClass(classId, 1, true))
				{
					player.abortCast();
					player.stopAllEffectsExceptThoseThatLastThroughDeath();
					player.stopAllEffects();
					player.stopCubics();
					player.setActiveClass(1);
					player.sendPacket(new ExSubjobInfo(player, SubclassInfoType.CLASS_CHANGED));
					player.sendPacket(getNpcHtmlMessage(player, npc, "34513-07.html"));
					SkillTreeData.getInstance().cleanSkillUponChangeClass(player);
					player.sendPacket(new AcquireSkillList(player));
					player.sendSkillList();
					player.broadcastUserInfo();
					
					// Item rewards
					player.addItem("subclass", CHAOS_POMANDER_DUAL_CLASS, 2, player, true);
					player.addItem("subclass", PAULINAS_RGRADE_EQUIPMENT_SET, 1, player, true);
				}
				break;
			}
		}
	}
	
	public static void main(String[] args)
	{
		new Joachim();
	}
}
