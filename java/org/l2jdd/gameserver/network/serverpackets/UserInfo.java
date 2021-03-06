
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.data.xml.ExperienceData;
import org.l2jdd.gameserver.enums.AttributeType;
import org.l2jdd.gameserver.enums.ClassId;
import org.l2jdd.gameserver.enums.ItemGrade;
import org.l2jdd.gameserver.enums.UserInfoType;
import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.instancemanager.CursedWeaponsManager;
import org.l2jdd.gameserver.instancemanager.RankManager;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Sdw, UnAfraid
 */
public class UserInfo extends AbstractMaskPacket<UserInfoType>
{
	private PlayerInstance _player;
	
	private int _relation;
	private int _runSpd;
	private int _walkSpd;
	private int _swimRunSpd;
	private int _swimWalkSpd;
	private final int _flRunSpd = 0;
	private final int _flWalkSpd = 0;
	private int _flyRunSpd;
	private int _flyWalkSpd;
	private double _moveMultiplier;
	private int _enchantLevel;
	private int _armorEnchant;
	private String _title;
	
	private final byte[] _masks = new byte[]
	{
		(byte) 0x00,
		(byte) 0x00,
		(byte) 0x00,
		(byte) 0x00
	};
	
	private int _initSize = 5;
	
	public UserInfo(PlayerInstance player)
	{
		this(player, true);
	}
	
	public UserInfo(PlayerInstance player, boolean addAll)
	{
		if (!player.isSubclassLocked()) // Changing class.
		{
			_player = player;
			_relation = calculateRelation(player);
			_moveMultiplier = player.getMovementSpeedMultiplier();
			_runSpd = (int) Math.round(player.getRunSpeed() / _moveMultiplier);
			_walkSpd = (int) Math.round(player.getWalkSpeed() / _moveMultiplier);
			_swimRunSpd = (int) Math.round(player.getSwimRunSpeed() / _moveMultiplier);
			_swimWalkSpd = (int) Math.round(player.getSwimWalkSpeed() / _moveMultiplier);
			_flyRunSpd = player.isFlying() ? _runSpd : 0;
			_flyWalkSpd = player.isFlying() ? _walkSpd : 0;
			_enchantLevel = player.getInventory().getWeaponEnchant();
			_armorEnchant = player.getInventory().getArmorMinEnchant();
			_title = player.getTitle();
			if (player.isGM() && player.isInvisible())
			{
				_title = "[Invisible]";
			}
			
			if (addAll)
			{
				addComponentType(UserInfoType.values());
			}
		}
	}
	
	@Override
	protected byte[] getMasks()
	{
		return _masks;
	}
	
	@Override
	protected void onNewMaskAdded(UserInfoType component)
	{
		calcBlockSize(component);
	}
	
	private void calcBlockSize(UserInfoType type)
	{
		switch (type)
		{
			case BASIC_INFO:
			{
				_initSize += type.getBlockLength() + (_player.getAppearance().getVisibleName().length() * 2);
				break;
			}
			case CLAN:
			{
				_initSize += type.getBlockLength() + (_title.length() * 2);
				break;
			}
			default:
			{
				_initSize += type.getBlockLength();
				break;
			}
		}
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		if (_player == null)
		{
			return false;
		}
		
		OutgoingPackets.USER_INFO.writeId(packet);
		
		packet.writeD(_player.getObjectId());
		packet.writeD(_initSize);
		packet.writeH(28); // 286 - 28
		packet.writeB(_masks);
		
		if (containsMask(UserInfoType.RELATION))
		{
			packet.writeD(_relation);
		}
		
		if (containsMask(UserInfoType.BASIC_INFO))
		{
			packet.writeH(23 + (_player.getAppearance().getVisibleName().length() * 2));
			packet.writeString(_player.getName());
			packet.writeC(_player.isGM() ? 0x01 : 0x00);
			packet.writeC(_player.getRace().ordinal());
			packet.writeC(_player.getAppearance().isFemale() ? 0x01 : 0x00);
			packet.writeD(ClassId.getClassId(_player.getBaseTemplate().getClassId().getId()).getRootClassId().getId());
			packet.writeD(_player.getClassId().getId());
			packet.writeC(_player.getLevel());
			packet.writeC(0x00); // 270
			packet.writeC(0x00); // 270
			packet.writeC(0x00); // 270
			packet.writeD(0x00); // 286
		}
		
		if (containsMask(UserInfoType.BASE_STATS))
		{
			packet.writeH(18);
			packet.writeH(_player.getSTR());
			packet.writeH(_player.getDEX());
			packet.writeH(_player.getCON());
			packet.writeH(_player.getINT());
			packet.writeH(_player.getWIT());
			packet.writeH(_player.getMEN());
			packet.writeH(_player.getLUC());
			packet.writeH(_player.getCHA());
		}
		
		if (containsMask(UserInfoType.MAX_HPCPMP))
		{
			packet.writeH(14);
			packet.writeD(_player.getMaxHp());
			packet.writeD(_player.getMaxMp());
			packet.writeD(_player.getMaxCp());
		}
		
		if (containsMask(UserInfoType.CURRENT_HPMPCP_EXP_SP))
		{
			packet.writeH(38);
			packet.writeD((int) Math.round(_player.getCurrentHp()));
			packet.writeD((int) Math.round(_player.getCurrentMp()));
			packet.writeD((int) Math.round(_player.getCurrentCp()));
			packet.writeQ(_player.getSp());
			packet.writeQ(_player.getExp());
			packet.writeF((float) (_player.getExp() - ExperienceData.getInstance().getExpForLevel(_player.getLevel())) / (ExperienceData.getInstance().getExpForLevel(_player.getLevel() + 1) - ExperienceData.getInstance().getExpForLevel(_player.getLevel())));
		}
		
		if (containsMask(UserInfoType.ENCHANTLEVEL))
		{
			packet.writeH(4);
			packet.writeC(_enchantLevel);
			packet.writeC(_armorEnchant);
		}
		
		if (containsMask(UserInfoType.APPAREANCE))
		{
			packet.writeH(15);
			packet.writeD(_player.getVisualHair());
			packet.writeD(_player.getVisualHairColor());
			packet.writeD(_player.getVisualFace());
			packet.writeC(_player.isHairAccessoryEnabled() ? 0x01 : 0x00);
		}
		
		if (containsMask(UserInfoType.STATUS))
		{
			packet.writeH(6);
			packet.writeC(_player.getMountType().ordinal());
			packet.writeC(_player.getPrivateStoreType().getId());
			packet.writeC(_player.getCrystallizeGrade() != ItemGrade.NONE ? 1 : 0);
			packet.writeC(_player.getAbilityPoints() - _player.getAbilityPointsUsed());
		}
		
		if (containsMask(UserInfoType.STATS))
		{
			packet.writeH(64); // 270
			packet.writeH(_player.getActiveWeaponItem() != null ? 40 : 20);
			packet.writeD(_player.getPAtk());
			packet.writeD(_player.getPAtkSpd());
			packet.writeD(_player.getPDef());
			packet.writeD(_player.getEvasionRate());
			packet.writeD(_player.getAccuracy());
			packet.writeD(_player.getCriticalHit());
			packet.writeD(_player.getMAtk());
			packet.writeD(_player.getMAtkSpd());
			packet.writeD(_player.getPAtkSpd()); // Seems like atk speed - 1
			packet.writeD(_player.getMagicEvasionRate());
			packet.writeD(_player.getMDef());
			packet.writeD(_player.getMagicAccuracy());
			packet.writeD(_player.getMCriticalHit());
			packet.writeD(0x00); // 270
			packet.writeD(0x00); // 270
		}
		
		if (containsMask(UserInfoType.ELEMENTALS))
		{
			packet.writeH(14);
			packet.writeH(_player.getDefenseElementValue(AttributeType.FIRE));
			packet.writeH(_player.getDefenseElementValue(AttributeType.WATER));
			packet.writeH(_player.getDefenseElementValue(AttributeType.WIND));
			packet.writeH(_player.getDefenseElementValue(AttributeType.EARTH));
			packet.writeH(_player.getDefenseElementValue(AttributeType.HOLY));
			packet.writeH(_player.getDefenseElementValue(AttributeType.DARK));
		}
		
		if (containsMask(UserInfoType.POSITION))
		{
			packet.writeH(18);
			packet.writeD(_player.getX());
			packet.writeD(_player.getY());
			packet.writeD(_player.getZ());
			packet.writeD(_player.isInVehicle() ? _player.getVehicle().getObjectId() : 0);
		}
		
		if (containsMask(UserInfoType.SPEED))
		{
			packet.writeH(18);
			packet.writeH(_runSpd);
			packet.writeH(_walkSpd);
			packet.writeH(_swimRunSpd);
			packet.writeH(_swimWalkSpd);
			packet.writeH(_flRunSpd);
			packet.writeH(_flWalkSpd);
			packet.writeH(_flyRunSpd);
			packet.writeH(_flyWalkSpd);
		}
		
		if (containsMask(UserInfoType.MULTIPLIER))
		{
			packet.writeH(18);
			packet.writeF(_moveMultiplier);
			packet.writeF(_player.getAttackSpeedMultiplier());
		}
		
		if (containsMask(UserInfoType.COL_RADIUS_HEIGHT))
		{
			packet.writeH(18);
			packet.writeF(_player.getCollisionRadius());
			packet.writeF(_player.getCollisionHeight());
		}
		
		if (containsMask(UserInfoType.ATK_ELEMENTAL))
		{
			packet.writeH(5);
			final AttributeType attackAttribute = _player.getAttackElement();
			packet.writeC(attackAttribute.getClientId());
			packet.writeH(_player.getAttackElementValue(attackAttribute));
		}
		
		if (containsMask(UserInfoType.CLAN))
		{
			packet.writeH(32 + (_title.length() * 2));
			packet.writeString(_title);
			packet.writeH(_player.getPledgeType());
			packet.writeD(_player.getClanId());
			packet.writeD(_player.getClanCrestLargeId());
			packet.writeD(_player.getClanCrestId());
			packet.writeD(_player.getClanPrivileges().getBitmask());
			packet.writeC(_player.isClanLeader() ? 0x01 : 0x00);
			packet.writeD(_player.getAllyId());
			packet.writeD(_player.getAllyCrestId());
			packet.writeC(_player.isInMatchingRoom() ? 0x01 : 0x00);
		}
		
		if (containsMask(UserInfoType.SOCIAL))
		{
			packet.writeH(30); // 228
			packet.writeC(_player.getPvpFlag());
			packet.writeD(_player.getReputation()); // Reputation
			packet.writeC(_player.getNobleLevel());
			packet.writeC(_player.isHero() || (_player.isGM() && Config.GM_HERO_AURA) ? 2 : 0); // 152 - Value for enabled changed to 2?
			packet.writeC(_player.getPledgeClass());
			packet.writeD(_player.getPkKills());
			packet.writeD(_player.getPvpKills());
			packet.writeH(_player.getRecomLeft());
			packet.writeH(_player.getRecomHave());
			// AFK animation.
			if ((_player.getClan() != null) && (CastleManager.getInstance().getCastleByOwner(_player.getClan()) != null)) // 196
			{
				packet.writeD(_player.isClanLeader() ? 100 : 101);
			}
			else
			{
				packet.writeD(0x00);
			}
			packet.writeD(0x00); // 228
		}
		
		if (containsMask(UserInfoType.VITA_FAME))
		{
			packet.writeH(19); // 196
			packet.writeD(_player.getVitalityPoints());
			packet.writeC(0x00); // Vita Bonus
			packet.writeD(_player.getFame());
			packet.writeD(_player.getRaidbossPoints());
			packet.writeC(0x00); // 196
			packet.writeH(_player.getSymbolSealPoints()); // Henna Seal Engraving Gauge
			packet.writeC(0x00); // 196
		}
		
		if (containsMask(UserInfoType.SLOTS))
		{
			packet.writeH(12); // 152
			packet.writeC(_player.getInventory().getTalismanSlots());
			packet.writeC(_player.getInventory().getBroochJewelSlots());
			packet.writeC(_player.getTeam().getId());
			packet.writeD(0x00);
			
			if (_player.getInventory().getAgathionSlots() > 0)
			{
				packet.writeC(0x01); // Charm slots
				packet.writeC(_player.getInventory().getAgathionSlots() - 1);
				packet.writeC(_player.getInventory().getArtifactSlots()); // Artifact set slots // 152
			}
			else
			{
				packet.writeC(0x00); // Charm slots
				packet.writeC(0x00);
				packet.writeC(_player.getInventory().getArtifactSlots()); // Artifact set slots // 152
			}
		}
		
		if (containsMask(UserInfoType.MOVEMENTS))
		{
			packet.writeH(4);
			packet.writeC(_player.isInsideZone(ZoneId.WATER) ? 1 : _player.isFlyingMounted() ? 2 : 0);
			packet.writeC(_player.isRunning() ? 0x01 : 0x00);
		}
		
		if (containsMask(UserInfoType.COLOR))
		{
			packet.writeH(10);
			packet.writeD(_player.getAppearance().getNameColor());
			packet.writeD(_player.getAppearance().getTitleColor());
		}
		
		if (containsMask(UserInfoType.INVENTORY_LIMIT))
		{
			packet.writeH(13);
			packet.writeH(0x00);
			packet.writeH(0x00);
			packet.writeH(_player.getInventoryLimit());
			
			packet.writeC(_player.isCursedWeaponEquipped() ? CursedWeaponsManager.getInstance().getLevel(_player.getCursedWeaponEquippedId()) : 0);
			
			packet.writeC(0x00); // 196
			packet.writeC(0x00); // 196
			packet.writeC(0x00); // 196
			packet.writeC(0x00); // 196
		}
		
		if (containsMask(UserInfoType.TRUE_HERO))
		{
			packet.writeH(9);
			packet.writeD(0x00);
			packet.writeH(0x00);
			packet.writeC(_player.isTrueHero() ? 100 : 0x00);
		}
		
		if (containsMask(UserInfoType.ATT_SPIRITS)) // 152
		{
			packet.writeH(26);
			packet.writeD(-1);
			packet.writeD(0x00);
			packet.writeD(0x00);
			packet.writeD(0x00);
			packet.writeD(0x00);
			packet.writeD(0x00);
		}
		
		if (containsMask(UserInfoType.RANKING)) // 196
		{
			packet.writeH(6);
			packet.writeD(RankManager.getInstance().getPlayerGlobalRank(_player) == 1 ? 1 : RankManager.getInstance().getPlayerRaceRank(_player) == 1 ? 2 : 0);
		}
		
		if (containsMask(UserInfoType.STAT_POINTS)) // 235
		{
			packet.writeH(16);
			packet.writeH(0x00); // Usable points?
			packet.writeH(0x00); // STR points
			packet.writeH(0x00); // DEX points
			packet.writeH(0x00); // CON points
			packet.writeH(0x00); // INT points
			packet.writeH(0x00); // WIT points
			packet.writeH(0x00); // MEN points
		}
		
		if (containsMask(UserInfoType.STAT_ABILITIES)) // 235
		{
			packet.writeH(18);
			packet.writeH(0x00); // STR additional
			packet.writeH(0x00); // DEX additional
			packet.writeH(0x00); // CON additional
			packet.writeH(0x00); // INT additional
			packet.writeH(0x00); // WIT additional
			packet.writeH(0x00); // MEN additional
			packet.writeH(0x00); // ?
			packet.writeH(0x00); // ?
		}
		
		if (containsMask(UserInfoType.ELIXIR_USED)) // 286
		{
			packet.writeH(1);
			packet.writeD(0x00);
		}
		
		// Send exp bonus change.
		if (containsMask(UserInfoType.VITA_FAME))
		{
			_player.sendPacket(new ExUserBoostStat(_player));
		}
		
		return true;
	}
	
	private int calculateRelation(PlayerInstance player)
	{
		int relation = 0;
		final Party party = player.getParty();
		final Clan clan = player.getClan();
		if (party != null)
		{
			relation |= 0x08; // Party member
			if (party.getLeader() == _player)
			{
				relation |= 0x10; // Party leader
			}
		}
		
		if (clan != null)
		{
			relation |= 0x20; // Clan member
			if (clan.getLeaderId() == player.getObjectId())
			{
				relation |= 0x40; // Clan leader
			}
		}
		
		if (player.isInSiege())
		{
			relation |= 0x80; // In siege
		}
		
		return relation;
	}
}
