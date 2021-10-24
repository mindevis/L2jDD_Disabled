
package org.l2jdd.gameserver.network.serverpackets.ensoul;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.model.ensoul.EnsoulOption;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mobius
 */
public class ExEnSoulExtractionResult implements IClientOutgoingPacket
{
	private final boolean _success;
	private final ItemInstance _item;
	
	public ExEnSoulExtractionResult(boolean success, ItemInstance item)
	{
		_success = success;
		_item = item;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_ENSOUL_EXTRACTION_RESULT.writeId(packet);
		packet.writeC(_success ? 1 : 0);
		if (_success)
		{
			packet.writeC(_item.getSpecialAbilities().size());
			for (EnsoulOption option : _item.getSpecialAbilities())
			{
				packet.writeD(option.getId());
			}
			packet.writeC(_item.getAdditionalSpecialAbilities().size());
			for (EnsoulOption option : _item.getAdditionalSpecialAbilities())
			{
				packet.writeD(option.getId());
			}
		}
		return true;
	}
}
