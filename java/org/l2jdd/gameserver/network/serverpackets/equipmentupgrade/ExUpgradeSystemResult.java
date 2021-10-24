
package org.l2jdd.gameserver.network.serverpackets.equipmentupgrade;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.serverpackets.AbstractItemPacket;

/**
 * @author Mobius
 */
public class ExUpgradeSystemResult extends AbstractItemPacket
{
	private final int _objectId;
	private final int _success;
	
	public ExUpgradeSystemResult(int objectId, int success)
	{
		_objectId = objectId;
		_success = success;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_UPGRADE_SYSTEM_RESULT.writeId(packet);
		packet.writeH(_success);
		packet.writeD(_objectId);
		return true;
	}
}
