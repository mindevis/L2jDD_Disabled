
package org.l2jdd.gameserver;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.l2jdd.Config;
import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.commons.database.DatabaseFactory;
import org.l2jdd.commons.enums.ServerMode;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.commons.util.DeadLockDetector;
import org.l2jdd.gameserver.cache.HtmCache;
import org.l2jdd.gameserver.data.BotReportTable;
import org.l2jdd.gameserver.data.EventDroplist;
import org.l2jdd.gameserver.data.ItemTable;
import org.l2jdd.gameserver.data.sql.AnnouncementsTable;
import org.l2jdd.gameserver.data.sql.CharNameTable;
import org.l2jdd.gameserver.data.sql.CharSummonTable;
import org.l2jdd.gameserver.data.sql.ClanTable;
import org.l2jdd.gameserver.data.sql.CrestTable;
import org.l2jdd.gameserver.data.sql.OfflineTraderTable;
import org.l2jdd.gameserver.data.xml.ActionData;
import org.l2jdd.gameserver.data.xml.AdminData;
import org.l2jdd.gameserver.data.xml.AgathionData;
import org.l2jdd.gameserver.data.xml.AlchemyData;
import org.l2jdd.gameserver.data.xml.AppearanceItemData;
import org.l2jdd.gameserver.data.xml.ArmorSetData;
import org.l2jdd.gameserver.data.xml.AttendanceRewardData;
import org.l2jdd.gameserver.data.xml.BeautyShopData;
import org.l2jdd.gameserver.data.xml.BuyListData;
import org.l2jdd.gameserver.data.xml.CategoryData;
import org.l2jdd.gameserver.data.xml.ClanHallData;
import org.l2jdd.gameserver.data.xml.ClanMasteryData;
import org.l2jdd.gameserver.data.xml.ClanShopData;
import org.l2jdd.gameserver.data.xml.ClassListData;
import org.l2jdd.gameserver.data.xml.CombinationItemsData;
import org.l2jdd.gameserver.data.xml.CubicData;
import org.l2jdd.gameserver.data.xml.DailyMissionData;
import org.l2jdd.gameserver.data.xml.DoorData;
import org.l2jdd.gameserver.data.xml.EnchantItemData;
import org.l2jdd.gameserver.data.xml.EnchantItemGroupsData;
import org.l2jdd.gameserver.data.xml.EnchantItemHPBonusData;
import org.l2jdd.gameserver.data.xml.EnchantItemOptionsData;
import org.l2jdd.gameserver.data.xml.EnchantSkillGroupsData;
import org.l2jdd.gameserver.data.xml.EnsoulData;
import org.l2jdd.gameserver.data.xml.EquipmentUpgradeData;
import org.l2jdd.gameserver.data.xml.EventEngineData;
import org.l2jdd.gameserver.data.xml.ExperienceData;
import org.l2jdd.gameserver.data.xml.FakePlayerData;
import org.l2jdd.gameserver.data.xml.FenceData;
import org.l2jdd.gameserver.data.xml.FishingData;
import org.l2jdd.gameserver.data.xml.HennaData;
import org.l2jdd.gameserver.data.xml.HitConditionBonusData;
import org.l2jdd.gameserver.data.xml.InitialEquipmentData;
import org.l2jdd.gameserver.data.xml.InitialShortcutData;
import org.l2jdd.gameserver.data.xml.ItemCrystallizationData;
import org.l2jdd.gameserver.data.xml.KarmaData;
import org.l2jdd.gameserver.data.xml.LuckyGameData;
import org.l2jdd.gameserver.data.xml.MultisellData;
import org.l2jdd.gameserver.data.xml.NpcData;
import org.l2jdd.gameserver.data.xml.NpcNameLocalisationData;
import org.l2jdd.gameserver.data.xml.OptionData;
import org.l2jdd.gameserver.data.xml.PetDataTable;
import org.l2jdd.gameserver.data.xml.PetSkillData;
import org.l2jdd.gameserver.data.xml.PlayerTemplateData;
import org.l2jdd.gameserver.data.xml.PlayerXpPercentLostData;
import org.l2jdd.gameserver.data.xml.PrimeShopData;
import org.l2jdd.gameserver.data.xml.RecipeData;
import org.l2jdd.gameserver.data.xml.ResidenceFunctionsData;
import org.l2jdd.gameserver.data.xml.SayuneData;
import org.l2jdd.gameserver.data.xml.SecondaryAuthData;
import org.l2jdd.gameserver.data.xml.SendMessageLocalisationData;
import org.l2jdd.gameserver.data.xml.ShuttleData;
import org.l2jdd.gameserver.data.xml.SiegeScheduleData;
import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.data.xml.SkillTreeData;
import org.l2jdd.gameserver.data.xml.SpawnData;
import org.l2jdd.gameserver.data.xml.StaticObjectData;
import org.l2jdd.gameserver.data.xml.TeleportListData;
import org.l2jdd.gameserver.data.xml.TeleporterData;
import org.l2jdd.gameserver.data.xml.TimedHuntingZoneData;
import org.l2jdd.gameserver.data.xml.TransformData;
import org.l2jdd.gameserver.data.xml.VariationData;
import org.l2jdd.gameserver.geoengine.GeoEngine;
import org.l2jdd.gameserver.handler.ConditionHandler;
import org.l2jdd.gameserver.handler.DailyMissionHandler;
import org.l2jdd.gameserver.handler.EffectHandler;
import org.l2jdd.gameserver.handler.SkillConditionHandler;
import org.l2jdd.gameserver.instancemanager.AirShipManager;
import org.l2jdd.gameserver.instancemanager.AntiFeedManager;
import org.l2jdd.gameserver.instancemanager.BoatManager;
import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.instancemanager.CastleManorManager;
import org.l2jdd.gameserver.instancemanager.ClanEntryManager;
import org.l2jdd.gameserver.instancemanager.ClanHallAuctionManager;
import org.l2jdd.gameserver.instancemanager.CommissionManager;
import org.l2jdd.gameserver.instancemanager.CursedWeaponsManager;
import org.l2jdd.gameserver.instancemanager.CustomMailManager;
import org.l2jdd.gameserver.instancemanager.DBSpawnManager;
import org.l2jdd.gameserver.instancemanager.FactionManager;
import org.l2jdd.gameserver.instancemanager.FakePlayerChatManager;
import org.l2jdd.gameserver.instancemanager.FortManager;
import org.l2jdd.gameserver.instancemanager.FortSiegeManager;
import org.l2jdd.gameserver.instancemanager.GlobalVariablesManager;
import org.l2jdd.gameserver.instancemanager.GraciaSeedsManager;
import org.l2jdd.gameserver.instancemanager.GrandBossManager;
import org.l2jdd.gameserver.instancemanager.IdManager;
import org.l2jdd.gameserver.instancemanager.InstanceManager;
import org.l2jdd.gameserver.instancemanager.ItemAuctionManager;
import org.l2jdd.gameserver.instancemanager.ItemsOnGroundManager;
import org.l2jdd.gameserver.instancemanager.MailManager;
import org.l2jdd.gameserver.instancemanager.MapRegionManager;
import org.l2jdd.gameserver.instancemanager.MatchingRoomManager;
import org.l2jdd.gameserver.instancemanager.MentorManager;
import org.l2jdd.gameserver.instancemanager.PcCafePointsManager;
import org.l2jdd.gameserver.instancemanager.PetitionManager;
import org.l2jdd.gameserver.instancemanager.PrecautionaryRestartManager;
import org.l2jdd.gameserver.instancemanager.PremiumManager;
import org.l2jdd.gameserver.instancemanager.PunishmentManager;
import org.l2jdd.gameserver.instancemanager.QuestManager;
import org.l2jdd.gameserver.instancemanager.RankManager;
import org.l2jdd.gameserver.instancemanager.SellBuffsManager;
import org.l2jdd.gameserver.instancemanager.ServerRestartManager;
import org.l2jdd.gameserver.instancemanager.SiegeGuardManager;
import org.l2jdd.gameserver.instancemanager.SiegeManager;
import org.l2jdd.gameserver.instancemanager.WalkingManager;
import org.l2jdd.gameserver.instancemanager.ZoneManager;
import org.l2jdd.gameserver.instancemanager.games.MonsterRace;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.events.EventDispatcher;
import org.l2jdd.gameserver.model.olympiad.Hero;
import org.l2jdd.gameserver.model.olympiad.Olympiad;
import org.l2jdd.gameserver.model.votereward.VoteSystem;
import org.l2jdd.gameserver.network.ClientNetworkManager;
import org.l2jdd.gameserver.network.NpcStringId;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.loginserver.LoginServerNetworkManager;
import org.l2jdd.gameserver.network.telnet.TelnetServer;
import org.l2jdd.gameserver.scripting.ScriptEngineManager;
import org.l2jdd.gameserver.taskmanager.TaskManager;
import org.l2jdd.gameserver.ui.Gui;
import org.l2jdd.gameserver.util.Broadcast;

public class GameServer
{
	private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());
	
	private final DeadLockDetector _deadDetectThread;
	private static GameServer INSTANCE;
	public static final Calendar dateTimeServerStarted = Calendar.getInstance();
	
	public long getUsedMemoryMB()
	{
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
	}
	
	public DeadLockDetector getDeadLockDetectorThread()
	{
		return _deadDetectThread;
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
		
		printSection("Database");
		DatabaseFactory.init();
		
		printSection("ThreadPool");
		ThreadPool.init();
		
		printSection("IdManager");
		IdManager.getInstance();
		if (!IdManager.hasInitialized())
		{
			LOGGER.severe(getClass().getSimpleName() + ": Could not read object IDs from database. Please check your configuration.");
			throw new Exception("Could not initialize the ID factory!");
		}
		
		// load script engines
		printSection("Scripting Engine");
		EventDispatcher.getInstance();
		ScriptEngineManager.getInstance();
		
		printSection("Telnet");
		TelnetServer.getInstance();
		
		printSection("World");
		// start game time control early
		GameTimeController.init();
		World.getInstance();
		MapRegionManager.getInstance();
		ZoneManager.getInstance();
		DoorData.getInstance();
		FenceData.getInstance();
		AnnouncementsTable.getInstance();
		GlobalVariablesManager.getInstance();
		
		printSection("Data");
		ActionData.getInstance();
		CategoryData.getInstance();
		SecondaryAuthData.getInstance();
		CombinationItemsData.getInstance();
		SayuneData.getInstance();
		DailyMissionHandler.getInstance().executeScript();
		DailyMissionData.getInstance();
		
		printSection("Skills");
		SkillConditionHandler.getInstance().executeScript();
		EffectHandler.getInstance().executeScript();
		EnchantSkillGroupsData.getInstance();
		SkillTreeData.getInstance();
		SkillData.getInstance();
		PetSkillData.getInstance();
		
		printSection("Items");
		ConditionHandler.getInstance().executeScript();
		ItemTable.getInstance();
		EnchantItemGroupsData.getInstance();
		EnchantItemData.getInstance();
		EnchantItemOptionsData.getInstance();
		ItemCrystallizationData.getInstance();
		OptionData.getInstance();
		VariationData.getInstance();
		EnsoulData.getInstance();
		EnchantItemHPBonusData.getInstance();
		BuyListData.getInstance();
		MultisellData.getInstance();
		EquipmentUpgradeData.getInstance();
		AgathionData.getInstance();
		RecipeData.getInstance();
		ArmorSetData.getInstance();
		FishingData.getInstance();
		HennaData.getInstance();
		PrimeShopData.getInstance();
		PcCafePointsManager.getInstance();
		AppearanceItemData.getInstance();
		AlchemyData.getInstance();
		CommissionManager.getInstance();
		LuckyGameData.getInstance();
		AttendanceRewardData.getInstance();
		
		printSection("Characters");
		ClassListData.getInstance();
		InitialEquipmentData.getInstance();
		InitialShortcutData.getInstance();
		ExperienceData.getInstance();
		PlayerXpPercentLostData.getInstance();
		KarmaData.getInstance();
		HitConditionBonusData.getInstance();
		PlayerTemplateData.getInstance();
		CharNameTable.getInstance();
		AdminData.getInstance();
		PetDataTable.getInstance();
		CubicData.getInstance();
		CharSummonTable.getInstance().init();
		BeautyShopData.getInstance();
		MentorManager.getInstance();
		
		if (Config.FACTION_SYSTEM_ENABLED)
		{
			FactionManager.getInstance();
		}
		
		if (Config.PREMIUM_SYSTEM_ENABLED)
		{
			LOGGER.info("PremiumManager: Premium system is enabled.");
			PremiumManager.getInstance();
		}
		
		printSection("Clans");
		ClanTable.getInstance();
		ResidenceFunctionsData.getInstance();
		ClanHallData.getInstance();
		ClanHallAuctionManager.getInstance();
		ClanEntryManager.getInstance();
		ClanMasteryData.getInstance();
		ClanShopData.getInstance();
		
		printSection("Geodata");
		GeoEngine.getInstance();
		
		printSection("NPCs");
		NpcData.getInstance();
		FakePlayerData.getInstance();
		FakePlayerChatManager.getInstance();
		SpawnData.getInstance();
		WalkingManager.getInstance();
		StaticObjectData.getInstance();
		ItemAuctionManager.getInstance();
		CastleManager.getInstance().loadInstances();
		GrandBossManager.getInstance();
		EventDroplist.getInstance();
		
		printSection("Instance");
		InstanceManager.getInstance();
		
		printSection("Olympiad");
		Olympiad.getInstance();
		Hero.getInstance();
		
		// Call to load caches
		printSection("Cache");
		HtmCache.getInstance();
		CrestTable.getInstance();
		TeleportListData.getInstance();
		TeleporterData.getInstance();
		TimedHuntingZoneData.getInstance();
		MatchingRoomManager.getInstance();
		PetitionManager.getInstance();
		CursedWeaponsManager.getInstance();
		TransformData.getInstance();
		BotReportTable.getInstance();
		RankManager.getInstance();
		if (Config.SELLBUFF_ENABLED)
		{
			SellBuffsManager.getInstance();
		}
		if (Config.MULTILANG_ENABLE)
		{
			SystemMessageId.loadLocalisations();
			NpcStringId.loadLocalisations();
			SendMessageLocalisationData.getInstance();
			NpcNameLocalisationData.getInstance();
		}
		
		printSection("Scripts");
		QuestManager.getInstance();
		BoatManager.getInstance();
		AirShipManager.getInstance();
		ShuttleData.getInstance();
		GraciaSeedsManager.getInstance();
		
		try
		{
			LOGGER.info(getClass().getSimpleName() + ": Loading server scripts:");
			ScriptEngineManager.getInstance().executeScript(ScriptEngineManager.MASTER_HANDLER_FILE);
			ScriptEngineManager.getInstance().executeScriptList();
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, getClass().getSimpleName() + ": Failed to execute script list!", e);
		}
		
		SpawnData.getInstance().init();
		DBSpawnManager.getInstance();
		
		printSection("Event Engine");
		EventEngineData.getInstance();
		VoteSystem.initialize();
		
		printSection("Siege");
		SiegeManager.getInstance().getSieges();
		CastleManager.getInstance().activateInstances();
		FortManager.getInstance().loadInstances();
		FortManager.getInstance().activateInstances();
		FortSiegeManager.getInstance();
		SiegeScheduleData.getInstance();
		
		CastleManorManager.getInstance();
		SiegeGuardManager.getInstance();
		QuestManager.getInstance().report();
		
		if (Config.SAVE_DROPPED_ITEM)
		{
			ItemsOnGroundManager.getInstance();
		}
		
		if ((Config.AUTODESTROY_ITEM_AFTER > 0) || (Config.HERB_AUTO_DESTROY_TIME > 0))
		{
			ItemsAutoDestroy.getInstance();
		}
		
		MonsterRace.getInstance();
		
		TaskManager.getInstance();
		
		AntiFeedManager.getInstance().registerEvent(AntiFeedManager.GAME_ID);
		
		if (Config.ALLOW_MAIL)
		{
			MailManager.getInstance();
		}
		if (Config.CUSTOM_MAIL_MANAGER_ENABLED)
		{
			CustomMailManager.getInstance();
		}
		
		PunishmentManager.getInstance();
		
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		
		LOGGER.info("IdManager: Free ObjectID's remaining: " + IdManager.size());
		
		if ((Config.OFFLINE_TRADE_ENABLE || Config.OFFLINE_CRAFT_ENABLE) && Config.RESTORE_OFFLINERS)
		{
			OfflineTraderTable.getInstance().restoreOfflineTraders();
		}
		
		if (Config.SERVER_RESTART_SCHEDULE_ENABLED)
		{
			ServerRestartManager.getInstance();
		}
		
		if (Config.PRECAUTIONARY_RESTART_ENABLED)
		{
			PrecautionaryRestartManager.getInstance();
		}
		if (Config.DEADLOCK_DETECTOR)
		{
			_deadDetectThread = new DeadLockDetector(Duration.ofSeconds(Config.DEADLOCK_CHECK_INTERVAL), () ->
			{
				if (Config.RESTART_ON_DEADLOCK)
				{
					Broadcast.toAllOnlinePlayers("Server has stability issues - restarting now.");
					Shutdown.getInstance().startShutdown(null, 60, true);
				}
			});
			_deadDetectThread.setDaemon(true);
			_deadDetectThread.start();
		}
		else
		{
			_deadDetectThread = null;
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
	
	public long getStartedTime()
	{
		return ManagementFactory.getRuntimeMXBean().getStartTime();
	}
	
	public String getUptime()
	{
		final long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;
		final long hours = uptime / 3600;
		final long mins = (uptime - (hours * 3600)) / 60;
		final long secs = ((uptime - (hours * 3600)) - (mins * 60));
		if (hours > 0)
		{
			return hours + "hrs " + mins + "mins " + secs + "secs";
		}
		return mins + "mins " + secs + "secs";
	}
	
	public static void main(String[] args) throws Exception
	{
		INSTANCE = new GameServer();
	}
	
	private void printSection(String section)
	{
		String s = "=[ " + section + " ]";
		while (s.length() < 61)
		{
			s = "-" + s;
		}
		LOGGER.info(s);
	}
	
	public static GameServer getInstance()
	{
		return INSTANCE;
	}
}
