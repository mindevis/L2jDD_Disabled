
package org.l2jdd.gameserver.network.serverpackets;

import java.util.Collection;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.holders.DamageTakenHolder;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author Mobius
 */
public class ExDieInfo implements IClientOutgoingPacket
{
	private final Collection<ItemInstance> _droppedItems;
	private final Collection<DamageTakenHolder> _lastDamageTaken;
	
	public ExDieInfo(Collection<ItemInstance> droppedItems, Collection<DamageTakenHolder> lastDamageTaken)
	{
		_droppedItems = droppedItems;
		_lastDamageTaken = lastDamageTaken;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_DIE_INFO.writeId(packet);
		
		packet.writeH(_droppedItems.size());
		for (ItemInstance item : _droppedItems)
		{
			packet.writeD(item.getId());
			packet.writeD(item.getEnchantLevel());
			packet.writeD((int) item.getCount());
		}
		
		packet.writeD(_lastDamageTaken.size());
		for (DamageTakenHolder damageHolder : _lastDamageTaken)
		{
			packet.writeS(damageHolder.getCreature().getName());
			packet.writeH(0x00);
			packet.writeD(damageHolder.getSkillId());
			packet.writeF(damageHolder.getDamage());
			packet.writeD(0x00);
		}
		
		return true;
	}
}
