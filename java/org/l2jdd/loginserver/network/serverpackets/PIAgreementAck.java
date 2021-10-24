
package org.l2jdd.loginserver.network.serverpackets;

import org.l2jdd.commons.network.IOutgoingPacket;
import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.loginserver.network.OutgoingPackets;

/**
 * @author UnAfraid
 */
public class PIAgreementAck implements IOutgoingPacket
{
	private final int _accountId;
	private final int _status;
	
	public PIAgreementAck(int accountId, int status)
	{
		_accountId = accountId;
		_status = status;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.PI_AGREEMENT_ACK.writeId(packet);
		packet.writeD(_accountId);
		packet.writeC(_status);
		return true;
	}
}
