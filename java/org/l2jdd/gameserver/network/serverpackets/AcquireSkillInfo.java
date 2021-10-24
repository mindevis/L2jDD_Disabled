
package org.l2jdd.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.Config;
import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.enums.AcquireSkillType;
import org.l2jdd.gameserver.model.SkillLearn;
import org.l2jdd.gameserver.model.holders.ItemHolder;
import org.l2jdd.gameserver.model.skills.CommonSkill;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * Acquire Skill Info server packet implementation.
 * @author Zoey76
 */
public class AcquireSkillInfo implements IClientOutgoingPacket
{
	private final AcquireSkillType _type;
	private final int _id;
	private final int _level;
	private final long _spCost;
	private final List<Req> _reqs;
	
	/**
	 * Private class containing learning skill requisites.
	 */
	private static class Req
	{
		public int itemId;
		public long count;
		public int type;
		public int unk;
		
		/**
		 * @param pType TODO identify.
		 * @param pItemId the item Id.
		 * @param itemCount the item count.
		 * @param pUnk TODO identify.
		 */
		public Req(int pType, int pItemId, long itemCount, int pUnk)
		{
			itemId = pItemId;
			type = pType;
			count = itemCount;
			unk = pUnk;
		}
	}
	
	/**
	 * Constructor for the acquire skill info object.
	 * @param skillType the skill learning type.
	 * @param skillLearn the skill learn.
	 */
	public AcquireSkillInfo(AcquireSkillType skillType, SkillLearn skillLearn)
	{
		_id = skillLearn.getSkillId();
		_level = skillLearn.getSkillLevel();
		_spCost = skillLearn.getLevelUpSp();
		_type = skillType;
		_reqs = new ArrayList<>();
		if ((skillType != AcquireSkillType.PLEDGE) || Config.LIFE_CRYSTAL_NEEDED)
		{
			for (ItemHolder item : skillLearn.getRequiredItems())
			{
				if (!Config.DIVINE_SP_BOOK_NEEDED && (_id == CommonSkill.DIVINE_INSPIRATION.getId()))
				{
					continue;
				}
				_reqs.add(new Req(99, item.getId(), item.getCount(), 50));
			}
		}
	}
	
	/**
	 * Special constructor for Alternate Skill Learning system.<br>
	 * Sets a custom amount of SP.
	 * @param skillType the skill learning type.
	 * @param skillLearn the skill learn.
	 * @param sp the custom SP amount.
	 */
	public AcquireSkillInfo(AcquireSkillType skillType, SkillLearn skillLearn, int sp)
	{
		_id = skillLearn.getSkillId();
		_level = skillLearn.getSkillLevel();
		_spCost = sp;
		_type = skillType;
		_reqs = new ArrayList<>();
		for (ItemHolder item : skillLearn.getRequiredItems())
		{
			_reqs.add(new Req(99, item.getId(), item.getCount(), 50));
		}
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.ACQUIRE_SKILL_INFO.writeId(packet);
		
		packet.writeD(_id);
		packet.writeD(_level);
		packet.writeQ(_spCost);
		packet.writeD(_type.getId());
		packet.writeD(_reqs.size());
		for (Req temp : _reqs)
		{
			packet.writeD(temp.type);
			packet.writeD(temp.itemId);
			packet.writeQ(temp.count);
			packet.writeD(temp.unk);
		}
		return true;
	}
}
