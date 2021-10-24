
package ai.areas.TalkingIsland.AwakeningMaster;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.data.xml.SkillTreeData;
import org.l2jdd.gameserver.enums.CategoryType;
import org.l2jdd.gameserver.enums.ClassId;
import org.l2jdd.gameserver.enums.Race;
import org.l2jdd.gameserver.enums.UserInfoType;
import org.l2jdd.gameserver.model.SkillLearn;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.ListenerRegisterType;
import org.l2jdd.gameserver.model.events.annotations.RegisterEvent;
import org.l2jdd.gameserver.model.events.annotations.RegisterType;
import org.l2jdd.gameserver.model.events.impl.creature.player.OnPlayerChangeToAwakenedClass;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.olympiad.Hero;
import org.l2jdd.gameserver.model.quest.QuestState;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExChangeToAwakenedClass;
import org.l2jdd.gameserver.network.serverpackets.ExShowUsm;
import org.l2jdd.gameserver.network.serverpackets.SocialAction;

import ai.AbstractNpcAI;

/**
 * AwakeningMaster AI.
 * @author Sdw
 */
public class AwakeningMaster extends AbstractNpcAI
{
	// NPCs
	private static final int SIGEL_MASTER = 33397;
	private static final int TYRR_MASTER = 33398;
	private static final int OTHELL_MASTER = 33399;
	private static final int YUL_MASTER = 33400;
	private static final int FEOH_MASTER = 33401;
	private static final int ISS_MASTER = 33402;
	private static final int WYNN_MASTER = 33403;
	private static final int AEORE_MASTER = 33404;
	// Items
	private static final int SCROLL_OF_AFTERLIFE = 17600;
	private static final int CHAOS_POMANDER = 37374;
	private static final int CHAOS_POMANDER_DUAL_CLASS = 37375;
	private static final Map<CategoryType, Integer> AWAKE_POWER = new EnumMap<>(CategoryType.class);
	static
	{
		AWAKE_POWER.put(CategoryType.SIXTH_SIGEL_GROUP, 32264);
		AWAKE_POWER.put(CategoryType.SIXTH_TIR_GROUP, 32265);
		AWAKE_POWER.put(CategoryType.SIXTH_OTHEL_GROUP, 32266);
		AWAKE_POWER.put(CategoryType.SIXTH_YR_GROUP, 32267);
		AWAKE_POWER.put(CategoryType.SIXTH_FEOH_GROUP, 32268);
		AWAKE_POWER.put(CategoryType.SIXTH_WYNN_GROUP, 32269);
		AWAKE_POWER.put(CategoryType.SIXTH_IS_GROUP, 32270);
		AWAKE_POWER.put(CategoryType.SIXTH_EOLH_GROUP, 32271);
	}
	
	// Skills
	// private static final SkillHolder WYNN_POWER = new SkillHolder(16390, 1);
	// private static final SkillHolder FEOH_POWER = new SkillHolder(16391, 1);
	// private static final SkillHolder TYRR_POWER = new SkillHolder(16392, 1);
	// private static final SkillHolder OTHELL_POWER = new SkillHolder(16393, 1);
	// private static final SkillHolder ISS_POWER = new SkillHolder(16394, 1);
	// private static final SkillHolder YUL_POWER = new SkillHolder(16395, 1);
	// private static final SkillHolder SIGEL_POWER = new SkillHolder(16396, 1);
	// private static final SkillHolder AEORE_POWER = new SkillHolder(16397, 1);
	
	private AwakeningMaster()
	{
		addStartNpc(SIGEL_MASTER, TYRR_MASTER, OTHELL_MASTER, YUL_MASTER, FEOH_MASTER, ISS_MASTER, WYNN_MASTER, AEORE_MASTER);
		addTalkId(SIGEL_MASTER, TYRR_MASTER, OTHELL_MASTER, YUL_MASTER, FEOH_MASTER, ISS_MASTER, WYNN_MASTER, AEORE_MASTER);
		addFirstTalkId(SIGEL_MASTER, TYRR_MASTER, OTHELL_MASTER, YUL_MASTER, FEOH_MASTER, ISS_MASTER, WYNN_MASTER, AEORE_MASTER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		final QuestState qs = getQuestState(player, true);
		if (qs == null)
		{
			return null;
		}
		final String htmltext = null;
		switch (event)
		{
			case "awakening":
			{
				if (hasQuestItems(player, SCROLL_OF_AFTERLIFE) && (player.getLevel() > 84) && player.isInCategory(CategoryType.FOURTH_CLASS_GROUP))
				{
					switch (npc.getId())
					{
						case SIGEL_MASTER:
						{
							if (!player.isInCategory(CategoryType.TANKER_CATEGORY))
							{
								return SIGEL_MASTER + "-no_class.htm";
							}
							break;
						}
						case TYRR_MASTER:
						{
							if (!player.isInCategory(CategoryType.WARRIOR_CATEGORY))
							{
								return TYRR_MASTER + "-no_class.htm";
							}
							break;
						}
						case OTHELL_MASTER:
						{
							if (!player.isInCategory(CategoryType.ROGUE_CATEGORY))
							{
								return OTHELL_MASTER + "-no_class.htm";
							}
							break;
						}
						case YUL_MASTER:
						{
							if (!player.isInCategory(CategoryType.ARCHER_CATEGORY))
							{
								return YUL_MASTER + "-no_class.htm";
							}
							break;
						}
						case FEOH_MASTER:
						{
							if (!player.isInCategory(CategoryType.WIZARD_CATEGORY))
							{
								return FEOH_MASTER + "-no_class.htm";
							}
							break;
						}
						case ISS_MASTER:
						{
							if (!player.isInCategory(CategoryType.ENCHANTER_CATEGORY))
							{
								return ISS_MASTER + "-no_class.htm";
							}
							break;
						}
						case WYNN_MASTER:
						{
							if (!player.isInCategory(CategoryType.SUMMONER_CATEGORY))
							{
								return WYNN_MASTER + "-no_class.htm";
							}
							break;
						}
						case AEORE_MASTER:
						{
							if (!player.isInCategory(CategoryType.HEALER_CATEGORY))
							{
								return AEORE_MASTER + "-no_class.htm";
							}
							break;
						}
					}
					
					// Fix for Female Soulhounds
					if (player.getClassId() == ClassId.FEMALE_SOUL_HOUND)
					{
						player.sendPacket(new ExChangeToAwakenedClass(ClassId.FEOH_SOUL_HOUND.getId()));
					}
					else
					{
						for (ClassId newClass : player.getClassId().getNextClassIds())
						{
							player.sendPacket(new ExChangeToAwakenedClass(newClass.getId()));
						}
					}
				}
				else
				{
					return npc.getId() + "-no.htm";
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		if (player.getRace() == Race.ERTHEIA)
		{
			// Ertheia dual class quest
			/*
			 * final QuestState qs = player.getQuestState(Q10472_WindsOfFateEncroachingShadows.class.getSimpleName()); if (qs != null) { if ((npc.getId() == WYNN_MASTER) && qs.isCond(8)) { return setNextErtheiaQuestState(npc, qs, WYNN_MASTER, 9, WYNN_POWER); } else if ((npc.getId() == FEOH_MASTER)
			 * && qs.isCond(9)) { return setNextErtheiaQuestState(npc, qs, FEOH_MASTER, 10, FEOH_POWER); } else if ((npc.getId() == TYRR_MASTER) && qs.isCond(10)) { return setNextErtheiaQuestState(npc, qs, TYRR_MASTER, 11, TYRR_POWER); } else if ((npc.getId() == OTHELL_MASTER) && qs.isCond(11)) {
			 * return setNextErtheiaQuestState(npc, qs, OTHELL_MASTER, 12, OTHELL_POWER); } else if ((npc.getId() == ISS_MASTER) && qs.isCond(12)) { return setNextErtheiaQuestState(npc, qs, ISS_MASTER, 13, ISS_POWER); } else if ((npc.getId() == YUL_MASTER) && qs.isCond(13)) { return
			 * setNextErtheiaQuestState(npc, qs, YUL_MASTER, 14, YUL_POWER); } else if ((npc.getId() == SIGEL_MASTER) && qs.isCond(14)) { return setNextErtheiaQuestState(npc, qs, SIGEL_MASTER, 15, SIGEL_POWER); } else if ((npc.getId() == AEORE_MASTER) && qs.isCond(15)) { return
			 * setNextErtheiaQuestState(npc, qs, AEORE_MASTER, 16, AEORE_POWER); } }
			 */
			return "ertheia.html";
		}
		return npc.getId() + ".html";
	}
	
	/*
	 * private String setNextErtheiaQuestState(Npc npc, QuestState qs, int npcId, int cond, SkillHolder skill) { npc.setTarget(qs.getPlayer()); npc.doCast(skill.getSkill()); qs.setCond(cond, true); return npcId + "-01.html"; }
	 */
	
	@RegisterEvent(EventType.ON_PLAYER_CHANGE_TO_AWAKENED_CLASS)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void OnPlayerChangeToAwakenedClass(OnPlayerChangeToAwakenedClass event)
	{
		final PlayerInstance player = event.getPlayer();
		if (player.isSubClassActive() && !player.isDualClassActive())
		{
			return;
		}
		
		if ((player.getLevel() < 85) || !player.isInCategory(CategoryType.FOURTH_CLASS_GROUP))
		{
			return;
		}
		
		if (player.isHero() || Hero.getInstance().isUnclaimedHero(player.getObjectId()))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AWAKEN_WHEN_YOU_ARE_A_HERO_OR_ON_THE_WAIT_LIST_FOR_HERO_STATUS);
			return;
		}
		
		if (!player.isInventoryUnder80(false))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AWAKEN_DUE_TO_YOUR_CURRENT_INVENTORY_WEIGHT_PLEASE_ORGANIZE_YOUR_INVENTORY_AND_TRY_AGAIN_DWARVEN_CHARACTERS_MUST_BE_AT_20_OR_BELOW_THE_INVENTORY_MAX_TO_AWAKEN);
			return;
		}
		
		if (player.isMounted() || player.isTransformed())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_AWAKEN_WHILE_YOU_RE_TRANSFORMED_OR_RIDING);
			return;
		}
		
		final ItemInstance item = player.getInventory().getItemByItemId(SCROLL_OF_AFTERLIFE);
		if (item == null)
		{
			return;
		}
		
		if (!player.destroyItem("Awakening", item, player, true))
		{
			return;
		}
		
		// Fix for Female Soulhounds
		int newClassId = -1;
		if (player.getClassId() == ClassId.FEMALE_SOUL_HOUND)
		{
			newClassId = ClassId.FEOH_SOUL_HOUND.getId();
		}
		else
		{
			for (ClassId newClass : player.getClassId().getNextClassIds())
			{
				newClassId = newClass.getId();
			}
		}
		
		player.setClassId(newClassId);
		if (player.isDualClassActive())
		{
			player.getSubClasses().get(player.getClassIndex()).setClassId(player.getActiveClass());
		}
		else
		{
			player.setBaseClass(player.getActiveClass());
		}
		player.sendPacket(SystemMessageId.CONGRATULATIONS_YOU_VE_COMPLETED_A_CLASS_TRANSFER);
		player.broadcastUserInfo(UserInfoType.BASIC_INFO, UserInfoType.MAX_HPCPMP);
		player.broadcastInfo();
		
		player.broadcastPacket(new SocialAction(player.getObjectId(), 20));
		for (Entry<CategoryType, Integer> ent : AWAKE_POWER.entrySet())
		{
			if (player.isInCategory(ent.getKey()))
			{
				giveItems(player, ent.getValue().intValue(), 1);
				break;
			}
		}
		giveItems(player, player.isDualClassActive() ? CHAOS_POMANDER_DUAL_CLASS : CHAOS_POMANDER, 2);
		SkillTreeData.getInstance().cleanSkillUponChangeClass(player);
		for (SkillLearn skill : SkillTreeData.getInstance().getRaceSkillTree(player.getRace()))
		{
			player.addSkill(SkillData.getInstance().getSkill(skill.getSkillId(), skill.getSkillLevel()), true);
		}
		player.sendSkillList();
		
		ThreadPool.schedule(() -> player.sendPacket(ExShowUsm.AWAKENING_END), 10000);
	}
	
	public static void main(String[] args)
	{
		new AwakeningMaster();
	}
}
