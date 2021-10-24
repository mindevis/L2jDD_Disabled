
package org.l2jdd.loginserver.network.serverpackets;

import org.l2jdd.commons.network.IOutgoingPacket;
import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.loginserver.network.OutgoingPackets;

/**
 * @author UnAfraid
 */
public class LoginOtpFail implements IOutgoingPacket
{
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.LOGIN_OPT_FAIL.writeId(packet);
		return true;
	}
}
