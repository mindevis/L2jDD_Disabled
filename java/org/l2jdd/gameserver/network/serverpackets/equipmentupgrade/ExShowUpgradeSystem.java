
package org.l2jdd.gameserver.network.serverpackets.equipmentupgrade;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.AbstractItemPacket;

/**
 * @author Mobius
 */
public class ExShowUpgradeSystem extends AbstractItemPacket
{
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SHOW_UPGRADE_SYSTEM.writeId(packet);
		packet.writeH(0x01);
		return true;
	}
}
