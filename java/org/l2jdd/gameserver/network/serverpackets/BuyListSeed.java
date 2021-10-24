
package org.l2jdd.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.instancemanager.CastleManorManager;
import org.l2jdd.gameserver.model.SeedProduction;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author l3x
 */
public class BuyListSeed implements IClientOutgoingPacket
{
	private final int _manorId;
	private final long _money;
	private final List<SeedProduction> _list = new ArrayList<>();
	
	public BuyListSeed(long currentMoney, int castleId)
	{
		_money = currentMoney;
		_manorId = castleId;
		for (SeedProduction s : CastleManorManager.getInstance().getSeedProduction(castleId, false))
		{
			if ((s.getAmount() > 0) && (s.getPrice() > 0))
			{
				_list.add(s);
			}
		}
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.BUY_LIST_SEED.writeId(packet);
		
		packet.writeQ(_money); // current money
		packet.writeD(0x00); // TODO: Find me!
		packet.writeD(_manorId); // manor id
		
		if (!_list.isEmpty())
		{
			packet.writeH(_list.size()); // list length
			for (SeedProduction s : _list)
			{
				packet.writeC(0x00); // mask item 0 to print minimal item information
				packet.writeD(s.getId()); // ObjectId
				packet.writeD(s.getId()); // ItemId
				packet.writeC(0xFF); // T1
				packet.writeQ(s.getAmount()); // Quantity
				packet.writeC(0x05); // Item Type 2 : 00-weapon, 01-shield/armor, 02-ring/earring/necklace, 03-questitem, 04-adena, 05-item
				packet.writeC(0x00); // Filler (always 0)
				packet.writeH(0x00); // Equipped : 00-No, 01-yes
				packet.writeQ(0x00); // Slot : 0006-lr.ear, 0008-neck, 0030-lr.finger, 0040-head, 0100-l.hand, 0200-gloves, 0400-chest, 0800-pants, 1000-feet, 4000-r.hand, 8000-r.hand
				packet.writeH(0x00); // Enchant level (pet level shown in control item)
				packet.writeD(-1);
				packet.writeD(-9999);
				packet.writeC(0x01); // GOD Item enabled = 1 disabled (red) = 0
				packet.writeQ(s.getPrice()); // price
			}
			_list.clear();
		}
		else
		{
			packet.writeH(0x00);
		}
		return true;
	}
}
