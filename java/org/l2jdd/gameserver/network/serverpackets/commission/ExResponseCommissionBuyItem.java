
package org.l2jdd.gameserver.network.serverpackets.commission;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.ItemInfo;
import org.l2jdd.gameserver.model.commission.CommissionItem;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author NosBit
 */
public class ExResponseCommissionBuyItem implements IClientOutgoingPacket
{
	public static final ExResponseCommissionBuyItem FAILED = new ExResponseCommissionBuyItem(null);
	
	private final CommissionItem _commissionItem;
	
	public ExResponseCommissionBuyItem(CommissionItem commissionItem)
	{
		_commissionItem = commissionItem;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_RESPONSE_COMMISSION_BUY_ITEM.writeId(packet);
		
		packet.writeD(_commissionItem != null ? 1 : 0);
		if (_commissionItem != null)
		{
			final ItemInfo itemInfo = _commissionItem.getItemInfo();
			packet.writeD(itemInfo.getEnchantLevel());
			packet.writeD(itemInfo.getItem().getId());
			packet.writeQ(itemInfo.getCount());
		}
		return true;
	}
}
