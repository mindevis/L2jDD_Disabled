/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jdd.gameserver;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.l2jdd.Config;
import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.commons.database.DatabaseFactory;
import org.l2jdd.commons.enums.ServerMode;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.commons.util.DeadlockDetector;
import org.l2jdd.commons.util.Util;
import org.l2jdd.gameserver.cache.HtmCache;
import org.l2jdd.gameserver.communitybbs.Manager.ForumsBBSManager;
import org.l2jdd.gameserver.data.HeroSkillTable;
import org.l2jdd.gameserver.data.ItemTable;
import org.l2jdd.gameserver.data.NobleSkillTable;
import org.l2jdd.gameserver.data.SchemeBufferTable;
import org.l2jdd.gameserver.data.SkillTable;
import org.l2jdd.gameserver.data.sql.AnnouncementsTable;
import org.l2jdd.gameserver.data.sql.CharNameTable;
import org.l2jdd.gameserver.data.sql.ClanTable;
import org.l2jdd.gameserver.data.sql.CrestTable;
import org.l2jdd.gameserver.data.sql.HelperBuffTable;
import org.l2jdd.gameserver.data.sql.NpcTable;
import org.l2jdd.gameserver.data.sql.OfflineTraderTable;
import org.l2jdd.gameserver.data.sql.PetDataTable;
import org.l2jdd.gameserver.data.sql.SkillSpellbookTable;
import org.l2jdd.gameserver.data.sql.SkillTreeTable;
import org.l2jdd.gameserver.data.sql.SpawnTable;
import org.l2jdd.gameserver.data.sql.TeleportLocationTable;
import org.l2jdd.gameserver.data.xml.AdminData;
import org.l2jdd.gameserver.data.xml.ArmorSetData;
import org.l2jdd.gameserver.data.xml.AugmentationData;
import org.l2jdd.gameserver.data.xml.BoatData;
import org.l2jdd.gameserver.data.xml.DoorData;
import org.l2jdd.gameserver.data.xml.ExperienceData;
import org.l2jdd.gameserver.data.xml.ExtractableItemData;
import org.l2jdd.gameserver.data.xml.FenceData;
import org.l2jdd.gameserver.data.xml.FishData;
import org.l2jdd.gameserver.data.xml.HennaData;
import org.l2jdd.gameserver.data.xml.ManorSeedData;
import org.l2jdd.gameserver.data.xml.MapRegionData;
import org.l2jdd.gameserver.data.xml.MultisellData;
import org.l2jdd.gameserver.data.xml.PlayerTemplateData;
import org.l2jdd.gameserver.data.xml.RecipeData;
import org.l2jdd.gameserver.data.xml.StaticObjectData;
import org.l2jdd.gameserver.data.xml.SummonItemData;
import org.l2jdd.gameserver.data.xml.WalkerRouteData;
import org.l2jdd.gameserver.data.xml.ZoneData;
import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.handler.AdminCommandHandler;
import org.l2jdd.gameserver.handler.AutoChatHandler;
import org.l2jdd.gameserver.handler.ItemHandler;
import org.l2jdd.gameserver.handler.SkillHandler;
import org.l2jdd.gameserver.handler.UserCommandHandler;
import org.l2jdd.gameserver.handler.VoicedCommandHandler;
import org.l2jdd.gameserver.instancemanager.AuctionManager;
import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.instancemanager.CastleManorManager;
import org.l2jdd.gameserver.instancemanager.ClanHallManager;
import org.l2jdd.gameserver.instancemanager.ClassDamageManager;
import org.l2jdd.gameserver.instancemanager.CoupleManager;
import org.l2jdd.gameserver.instancemanager.CrownManager;
import org.l2jdd.gameserver.instancemanager.CursedWeaponsManager;
import org.l2jdd.gameserver.instancemanager.CustomMailManager;
import org.l2jdd.gameserver.instancemanager.DayNightSpawnManager;
import org.l2jdd.gameserver.instancemanager.DimensionalRiftManager;
import org.l2jdd.gameserver.instancemanager.DuelManager;
import org.l2jdd.gameserver.instancemanager.FishingChampionshipManager;
import org.l2jdd.gameserver.instancemanager.FortManager;
import org.l2jdd.gameserver.instancemanager.FortSiegeManager;
import org.l2jdd.gameserver.instancemanager.FourSepulchersManager;
import org.l2jdd.gameserver.instancemanager.GlobalVariablesManager;
import org.l2jdd.gameserver.instancemanager.GrandBossManager;
import org.l2jdd.gameserver.instancemanager.IdManager;
import org.l2jdd.gameserver.instancemanager.ItemsOnGroundManager;
import org.l2jdd.gameserver.instancemanager.MercTicketManager;
import org.l2jdd.gameserver.instancemanager.PetitionManager;
import org.l2jdd.gameserver.instancemanager.PrecautionaryRestartManager;
import org.l2jdd.gameserver.instancemanager.QuestManager;
import org.l2jdd.gameserver.instancemanager.RaidBossPointsManager;
import org.l2jdd.gameserver.instancemanager.RaidBossSpawnManager;
import org.l2jdd.gameserver.instancemanager.ServerRestartManager;
import org.l2jdd.gameserver.instancemanager.SiegeManager;
import org.l2jdd.gameserver.instancemanager.events.EventManager;
import org.l2jdd.gameserver.instancemanager.events.PcPoint;
import org.l2jdd.gameserver.instancemanager.games.Lottery;
import org.l2jdd.gameserver.instancemanager.games.MonsterRace;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.olympiad.Hero;
import org.l2jdd.gameserver.model.olympiad.Olympiad;
import org.l2jdd.gameserver.model.partymatching.PartyMatchRoomList;
import org.l2jdd.gameserver.model.partymatching.PartyMatchWaitingList;
import org.l2jdd.gameserver.model.sevensigns.SevenSigns;
import org.l2jdd.gameserver.model.sevensigns.SevenSignsFestival;
import org.l2jdd.gameserver.model.siege.clanhalls.BanditStrongholdSiege;
import org.l2jdd.gameserver.model.siege.clanhalls.DevastatedCastle;
import org.l2jdd.gameserver.model.siege.clanhalls.FortressOfResistance;
import org.l2jdd.gameserver.model.spawn.AutoSpawn;
import org.l2jdd.gameserver.network.ClientNetworkManager;
import org.l2jdd.gameserver.network.loginserver.LoginServerNetworkManager;
import org.l2jdd.gameserver.script.EventDroplist;
import org.l2jdd.gameserver.script.faenor.FaenorScriptEngine;
import org.l2jdd.gameserver.scripting.ScriptEngineManager;
import org.l2jdd.gameserver.taskmanager.TaskManager;
import org.l2jdd.gameserver.ui.Gui;
import org.l2jdd.telnet.TelnetStatusThread;

public class GameServer
{
	private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());
	
	private static TelnetStatusThread _statusServer;
	private static GameServer INSTANCE;
	
	public static final Calendar dateTimeServerStarted = Calendar.getInstance();
	
	public long getUsedMemoryMB()
	{
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
	}
	
	public GameServer() throws Exception
	{
		final long serverLoadStart = Chronos.currentTimeMillis();
		
		// GUI
		if (!GraphicsEnvironment.isHeadless())
		{
			System.out.println("GameServer: Running in GUI mode.");
			new Gui();
		}
		
		// Create log folder
		final File logFolder = new File(".", "log");
		logFolder.mkdir();
		
		// Create input stream for log file -- or store file data into memory
		try (InputStream is = new FileInputStream(new File("./log.cfg")))
		{
			LogManager.getLogManager().readConfiguration(is);
		}
		
		// Initialize config
		Config.load(ServerMode.GAME);
		
		Util.printSection("Database");
		DatabaseFactory.init();
		
		Util.printSection("ThreadPool");
		ThreadPool.init();
		if (Config.DEADLOCKCHECK_INTIAL_TIME > 0)
		{
			ThreadPool.scheduleAtFixedRate(DeadlockDetector.getInstance(), Config.DEADLOCKCHECK_INTIAL_TIME, Config.DEADLOCKCHECK_DELAY_TIME);
		}
		
		Util.printSection("IdManager");
		IdManager.getInstance();
		if (!IdManager.hasInitialized())
		{
			LOGGER.severe("IdFactory: Could not read object IDs from database. Please check your configuration.");
			throw new Exception("Could not initialize the ID factory!");
		}
		
		new File(Config.DATAPACK_ROOT, "data/geodata").mkdirs();
		
		HtmCache.getInstance();
		CrestTable.getInstance();
		ScriptEngineManager.getInstance();
		
		Util.printSection("World");
		World.getInstance();
		MapRegionData.getInstance();
		AnnouncementsTable.getInstance();
		GlobalVariablesManager.getInstance();
		StaticObjectData.getInstance();
		TeleportLocationTable.getInstance();
		PartyMatchWaitingList.getInstance();
		PartyMatchRoomList.getInstance();
		GameTimeController.getInstance();
		CharNameTable.getInstance();
		ExperienceData.getInstance();
		DuelManager.getInstance();
		
		Util.printSection("Players");
		PlayerTemplateData.getInstance();
		if (Config.ENABLE_CLASS_DAMAGE_SETTINGS)
		{
			ClassDamageManager.loadConfig();
		}
		ClanTable.getInstance();
		if (Config.ENABLE_COMMUNITY_BOARD)
		{
			ForumsBBSManager.getInstance().initRoot();
		}
		
		Util.printSection("Skills");
		if (!SkillTable.getInstance().isInitialized())
		{
			LOGGER.info("Could not find the extraced files. Please Check Your Data.");
			throw new Exception("Could not initialize the skill table");
		}
		SkillTreeTable.getInstance();
		SkillSpellbookTable.getInstance();
		NobleSkillTable.getInstance();
		HeroSkillTable.getInstance();
		if (!HelperBuffTable.getInstance().isInitialized())
		{
			throw new Exception("Could not initialize the Helper Buff Table.");
		}
		LOGGER.info("Skills: All skills loaded.");
		
		Util.printSection("Items");
		ItemTable.getInstance();
		ArmorSetData.getInstance();
		ExtractableItemData.getInstance();
		SummonItemData.getInstance();
		HennaData.getInstance();
		if (Config.ALLOWFISHING)
		{
			FishData.getInstance();
		}
		
		Util.printSection("Npc");
		SchemeBufferTable.getInstance();
		WalkerRouteData.getInstance();
		if (!NpcTable.getInstance().isInitialized())
		{
			LOGGER.info("Could not find the extracted files. Please Check Your Data.");
			throw new Exception("Could not initialize the npc table");
		}
		
		Util.printSection("Geodata");
		GeoEngine.getInstance();
		
		Util.printSection("Economy");
		TradeController.getInstance();
		MultisellData.getInstance();
		
		Util.printSection("Clan Halls");
		ClanHallManager.getInstance();
		FortressOfResistance.getInstance();
		DevastatedCastle.getInstance();
		BanditStrongholdSiege.getInstance();
		AuctionManager.getInstance();
		
		Util.printSection("Zone");
		ZoneData.getInstance();
		
		Util.printSection("Spawnlist");
		if (!Config.ALT_DEV_NO_SPAWNS)
		{
			SpawnTable.getInstance();
		}
		else
		{
			LOGGER.info("Spawn: disable load.");
		}
		if (!Config.ALT_DEV_NO_RB)
		{
			RaidBossSpawnManager.getInstance();
			GrandBossManager.getInstance();
			RaidBossPointsManager.init();
		}
		else
		{
			LOGGER.info("RaidBoss: disable load.");
		}
		DayNightSpawnManager.getInstance().notifyChangeMode();
		
		Util.printSection("Dimensional Rift");
		DimensionalRiftManager.getInstance();
		
		Util.printSection("Misc");
		RecipeData.getInstance();
		RecipeController.getInstance();
		EventDroplist.getInstance();
		AugmentationData.getInstance();
		MonsterRace.getInstance();
		Lottery.getInstance();
		MercTicketManager.getInstance();
		PetitionManager.getInstance();
		CursedWeaponsManager.getInstance();
		TaskManager.getInstance();
		PetDataTable.getInstance();
		if (Config.SAVE_DROPPED_ITEM)
		{
			ItemsOnGroundManager.getInstance();
		}
		if ((Config.AUTODESTROY_ITEM_AFTER > 0) || (Config.HERB_AUTO_DESTROY_TIME > 0))
		{
			ItemsAutoDestroy.getInstance();
		}
		
		Util.printSection("Manor");
		ManorSeedData.getInstance();
		CastleManorManager.getInstance();
		
		Util.printSection("Castles");
		CastleManager.getInstance();
		SiegeManager.getInstance();
		FortManager.getInstance();
		FortSiegeManager.getInstance();
		CrownManager.getInstance();
		
		Util.printSection("Boat");
		BoatData.getInstance();
		
		Util.printSection("Doors");
		DoorData.getInstance().load();
		FenceData.getInstance();
		
		Util.printSection("Four Sepulchers");
		FourSepulchersManager.getInstance();
		
		Util.printSection("Seven Signs");
		SevenSigns.getInstance();
		SevenSignsFestival.getInstance();
		AutoSpawn.getInstance();
		AutoChatHandler.getInstance();
		
		Util.printSection("Olympiad System");
		Olympiad.getInstance();
		Hero.getInstance();
		
		Util.printSection("Access Levels");
		AdminData.getInstance();
		
		Util.printSection("Handlers");
		ItemHandler.getInstance();
		SkillHandler.getInstance();
		AdminCommandHandler.getInstance();
		UserCommandHandler.getInstance();
		VoicedCommandHandler.getInstance();
		
		LOGGER.info("AutoChatHandler: Loaded " + AutoChatHandler.getInstance().size() + " handlers in total.");
		LOGGER.info("AutoSpawnHandler: Loaded " + AutoSpawn.getInstance().size() + " handlers in total.");
		
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		
		// Schedule auto opening/closing doors.
		DoorData.getInstance().checkAutoOpen();
		
		if (Config.CUSTOM_MAIL_MANAGER_ENABLED)
		{
			CustomMailManager.getInstance();
		}
		
		Util.printSection("Scripts");
		if (!Config.ALT_DEV_NO_SCRIPT)
		{
			LOGGER.info("ScriptEngineManager: Loading server scripts:");
			ScriptEngineManager.getInstance().executeScriptList();
			FaenorScriptEngine.getInstance();
		}
		else
		{
			LOGGER.info("Script: disable load.");
		}
		
		if (Config.ALT_FISH_CHAMPIONSHIP_ENABLED)
		{
			FishingChampionshipManager.getInstance();
		}
		
		/* QUESTS */
		Util.printSection("Quests");
		if (!Config.ALT_DEV_NO_QUESTS)
		{
			if (QuestManager.getInstance().getQuests().size() == 0)
			{
				QuestManager.getInstance().reloadAllQuests();
			}
			else
			{
				QuestManager.getInstance().report();
			}
		}
		else
		{
			QuestManager.getInstance().unloadAllQuests();
		}
		
		Util.printSection("Game Server");
		
		LOGGER.info("IdFactory: Free ObjectID's remaining: " + IdManager.size());
		
		if (Config.ALLOW_WEDDING)
		{
			CoupleManager.getInstance();
		}
		
		if (Config.PCB_ENABLE)
		{
			ThreadPool.scheduleAtFixedRate(PcPoint.getInstance(), Config.PCB_INTERVAL * 1000, Config.PCB_INTERVAL * 1000);
		}
		
		Util.printSection("EventManager");
		EventManager.getInstance().startEventRegistration();
		
		if (EventManager.TVT_EVENT_ENABLED || EventManager.CTF_EVENT_ENABLED || EventManager.DM_EVENT_ENABLED)
		{
			if (EventManager.TVT_EVENT_ENABLED)
			{
				LOGGER.info("TVT Event is Enabled.");
			}
			if (EventManager.CTF_EVENT_ENABLED)
			{
				LOGGER.info("CTF Event is Enabled.");
			}
			if (EventManager.DM_EVENT_ENABLED)
			{
				LOGGER.info("DM Event is Enabled.");
			}
		}
		else
		{
			LOGGER.info("All events are Disabled.");
		}
		
		if ((Config.OFFLINE_TRADE_ENABLE || Config.OFFLINE_CRAFT_ENABLE) && Config.RESTORE_OFFLINERS)
		{
			OfflineTraderTable.restoreOfflineTraders();
		}
		
		Util.printSection("Protection");
		
		if (Config.CHECK_SKILLS_ON_ENTER)
		{
			LOGGER.info("Check skills on enter actived.");
		}
		
		if (Config.CHECK_NAME_ON_LOGIN)
		{
			LOGGER.info("Check bad name on enter actived.");
		}
		
		if (Config.PROTECTED_ENCHANT)
		{
			LOGGER.info("Check OverEnchant items on enter actived.");
		}
		
		if (Config.BYPASS_VALIDATION)
		{
			LOGGER.info("Bypass Validation actived.");
		}
		
		if (Config.L2WALKER_PROTECTION)
		{
			LOGGER.info("L2Walker protection actived.");
		}
		
		if (Config.SERVER_RESTART_SCHEDULE_ENABLED)
		{
			ServerRestartManager.getInstance();
		}
		
		if (Config.PRECAUTIONARY_RESTART_ENABLED)
		{
			PrecautionaryRestartManager.getInstance();
		}
		
		Util.printSection("Status");
		
		if (Config.IS_TELNET_ENABLED)
		{
			_statusServer = new TelnetStatusThread();
			_statusServer.start();
		}
		
		System.gc();
		final long totalMem = Runtime.getRuntime().maxMemory() / 1048576;
		LOGGER.info(getClass().getSimpleName() + ": Started, using " + getUsedMemoryMB() + " of " + totalMem + " MB total memory.");
		LOGGER.info(getClass().getSimpleName() + ": Maximum number of connected players is " + Config.MAXIMUM_ONLINE_USERS + ".");
		LOGGER.info(getClass().getSimpleName() + ": Server loaded in " + ((Chronos.currentTimeMillis() - serverLoadStart) / 1000) + " seconds.");
		
		ClientNetworkManager.getInstance().start();
		
		if (Boolean.getBoolean("newLoginServer"))
		{
			LoginServerNetworkManager.getInstance().connect();
		}
		else
		{
			LoginServerThread.getInstance().start();
		}
		
		Toolkit.getDefaultToolkit().beep();
	}
	
	public static void main(String[] args) throws Exception
	{
		INSTANCE = new GameServer();
	}
	
	public static GameServer getInstance()
	{
		return INSTANCE;
	}
}