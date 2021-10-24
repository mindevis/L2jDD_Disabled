
package org.l2jdd.loginserver.network.clientpackets;

import org.l2jdd.Config;
import org.l2jdd.commons.network.IIncomingPacket;
import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.loginserver.network.LoginClient;
import org.l2jdd.loginserver.network.serverpackets.PIAgreementCheck;

/**
 * @author UnAfraid
 */
public class RequestPIAgreementCheck implements IIncomingPacket<LoginClient>
{
	private int _accountId;
	
	@Override
	public boolean read(LoginClient client, PacketReader packet)
	{
		_accountId = packet.readD();
		final byte[] padding0 = new byte[3];
		final byte[] checksum = new byte[4];
		final byte[] padding1 = new byte[12];
		packet.readB(padding0, 0, padding0.length);
		packet.readB(checksum, 0, checksum.length);
		packet.readB(padding1, 0, padding1.length);
		return true;
	}
	
	@Override
	public void run(LoginClient client)
	{
		client.sendPacket(new PIAgreementCheck(_accountId, Config.SHOW_PI_AGREEMENT ? 0x01 : 0x00));
	}
}
