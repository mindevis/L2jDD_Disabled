
package org.l2jdd.loginserver.network.clientpackets;

import org.l2jdd.commons.network.IIncomingPacket;
import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.loginserver.network.LoginClient;
import org.l2jdd.loginserver.network.serverpackets.PIAgreementAck;

/**
 * @author UnAfraid
 */
public class RequestPIAgreement implements IIncomingPacket<LoginClient>
{
	private int _accountId;
	private int _status;
	
	@Override
	public boolean read(LoginClient client, PacketReader packet)
	{
		_accountId = packet.readD();
		_status = packet.readC();
		return true;
	}
	
	@Override
	public void run(LoginClient client)
	{
		client.sendPacket(new PIAgreementAck(_accountId, _status));
	}
}
