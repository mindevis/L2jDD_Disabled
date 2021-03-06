
package org.l2jdd.gameserver.data.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.l2jdd.Config;
import org.l2jdd.commons.database.DatabaseFactory;
import org.l2jdd.gameserver.data.xml.NpcData;
import org.l2jdd.gameserver.data.xml.PetDataTable;
import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.model.PetData;
import org.l2jdd.gameserver.model.actor.instance.PetInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.actor.instance.ServitorInstance;
import org.l2jdd.gameserver.model.actor.templates.NpcTemplate;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.serverpackets.PetItemList;

/**
 * @author Nyaran
 */
public class CharSummonTable
{
	private static final Logger LOGGER = Logger.getLogger(CharSummonTable.class.getName());
	private static final Map<Integer, Integer> _pets = new ConcurrentHashMap<>();
	private static final Map<Integer, Set<Integer>> _servitors = new ConcurrentHashMap<>();
	
	// SQL
	private static final String INIT_PET = "SELECT ownerId, item_obj_id FROM pets WHERE restore = 'true'";
	private static final String INIT_SUMMONS = "SELECT ownerId, summonId FROM character_summons";
	private static final String LOAD_SUMMON = "SELECT summonSkillId, summonId, curHp, curMp, time FROM character_summons WHERE ownerId = ?";
	private static final String REMOVE_SUMMON = "DELETE FROM character_summons WHERE ownerId = ? and summonId = ?";
	private static final String SAVE_SUMMON = "REPLACE INTO character_summons (ownerId,summonId,summonSkillId,curHp,curMp,time) VALUES (?,?,?,?,?,?)";
	
	public Map<Integer, Integer> getPets()
	{
		return _pets;
	}
	
	public Map<Integer, Set<Integer>> getServitors()
	{
		return _servitors;
	}
	
	public void init()
	{
		if (Config.RESTORE_SERVITOR_ON_RECONNECT)
		{
			try (Connection con = DatabaseFactory.getConnection();
				Statement s = con.createStatement();
				ResultSet rs = s.executeQuery(INIT_SUMMONS))
			{
				while (rs.next())
				{
					_servitors.computeIfAbsent(rs.getInt("ownerId"), k -> ConcurrentHashMap.newKeySet()).add(rs.getInt("summonId"));
				}
			}
			catch (Exception e)
			{
				LOGGER.warning(getClass().getSimpleName() + ": Error while loading saved servitor: " + e);
			}
		}
		
		if (Config.RESTORE_PET_ON_RECONNECT)
		{
			try (Connection con = DatabaseFactory.getConnection();
				Statement s = con.createStatement();
				ResultSet rs = s.executeQuery(INIT_PET))
			{
				while (rs.next())
				{
					_pets.put(rs.getInt("ownerId"), rs.getInt("item_obj_id"));
				}
			}
			catch (Exception e)
			{
				LOGGER.warning(getClass().getSimpleName() + ": Error while loading saved pet: " + e);
			}
		}
	}
	
	public void removeServitor(PlayerInstance player, int summonObjectId)
	{
		_servitors.computeIfPresent(player.getObjectId(), (k, v) ->
		{
			v.remove(summonObjectId);
			return !v.isEmpty() ? v : null;
		});
		
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(REMOVE_SUMMON))
		{
			ps.setInt(1, player.getObjectId());
			ps.setInt(2, summonObjectId);
			ps.execute();
		}
		catch (SQLException e)
		{
			LOGGER.warning(getClass().getSimpleName() + ": Summon cannot be removed: " + e);
		}
	}
	
	public void restorePet(PlayerInstance player)
	{
		final ItemInstance item = player.getInventory().getItemByObjectId(_pets.get(player.getObjectId()));
		if (item == null)
		{
			LOGGER.warning(getClass().getSimpleName() + ": Null pet summoning item for: " + player);
			return;
		}
		final PetData petData = PetDataTable.getInstance().getPetDataByItemId(item.getId());
		if (petData == null)
		{
			LOGGER.warning(getClass().getSimpleName() + ": Null pet data for: " + player + " and summoning item: " + item);
			return;
		}
		final NpcTemplate npcTemplate = NpcData.getInstance().getTemplate(petData.getNpcId());
		if (npcTemplate == null)
		{
			LOGGER.warning(getClass().getSimpleName() + ": Null pet NPC template for: " + player + " and pet Id:" + petData.getNpcId());
			return;
		}
		
		final PetInstance pet = PetInstance.spawnPet(npcTemplate, player, item);
		if (pet == null)
		{
			LOGGER.warning(getClass().getSimpleName() + ": Null pet instance for: " + player + " and pet NPC template:" + npcTemplate);
			return;
		}
		
		pet.setShowSummonAnimation(true);
		pet.setTitle(player.getName());
		
		if (!pet.isRespawned())
		{
			pet.setCurrentHp(pet.getMaxHp());
			pet.setCurrentMp(pet.getMaxMp());
			pet.getStat().setExp(pet.getExpForThisLevel());
			pet.setCurrentFed(pet.getMaxFed());
		}
		
		pet.setRunning();
		
		if (!pet.isRespawned())
		{
			pet.storeMe();
		}
		
		item.setEnchantLevel(pet.getLevel());
		player.setPet(pet);
		pet.spawnMe(player.getX() + 50, player.getY() + 100, player.getZ());
		pet.startFeed();
		pet.setFollowStatus(true);
		pet.getOwner().sendPacket(new PetItemList(pet.getInventory().getItems()));
		pet.broadcastStatusUpdate();
	}
	
	public void restoreServitor(PlayerInstance player)
	{
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(LOAD_SUMMON))
		{
			ps.setInt(1, player.getObjectId());
			try (ResultSet rs = ps.executeQuery())
			{
				Skill skill;
				while (rs.next())
				{
					final int summonObjId = rs.getInt("summonId");
					final int skillId = rs.getInt("summonSkillId");
					final int curHp = rs.getInt("curHp");
					final int curMp = rs.getInt("curMp");
					final int time = rs.getInt("time");
					
					removeServitor(player, summonObjId);
					skill = SkillData.getInstance().getSkill(skillId, player.getSkillLevel(skillId));
					if (skill == null)
					{
						return;
					}
					skill.applyEffects(player, player);
					
					if (player.hasServitors())
					{
						final ServitorInstance summon = player.getServitors().values().stream().map(s -> ((ServitorInstance) s)).filter(s -> s.getReferenceSkill() == skillId).findAny().orElse(null);
						summon.setCurrentHp(curHp);
						summon.setCurrentMp(curMp);
						summon.setLifeTimeRemaining(time);
					}
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.warning(getClass().getSimpleName() + ": Servitor cannot be restored: " + e);
		}
	}
	
	public void saveSummon(ServitorInstance summon)
	{
		if (summon == null)
		{
			return;
		}
		
		_servitors.computeIfAbsent(summon.getOwner().getObjectId(), k -> ConcurrentHashMap.newKeySet()).add(summon.getObjectId());
		
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(SAVE_SUMMON))
		{
			ps.setInt(1, summon.getOwner().getObjectId());
			ps.setInt(2, summon.getObjectId());
			ps.setInt(3, summon.getReferenceSkill());
			ps.setInt(4, (int) Math.round(summon.getCurrentHp()));
			ps.setInt(5, (int) Math.round(summon.getCurrentMp()));
			ps.setInt(6, Math.max(0, summon.getLifeTimeRemaining()));
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.warning(getClass().getSimpleName() + ": Failed to store summon: " + summon + " from " + summon.getOwner() + ", error: " + e);
		}
	}
	
	public static CharSummonTable getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final CharSummonTable INSTANCE = new CharSummonTable();
	}
}
