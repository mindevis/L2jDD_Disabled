
package org.l2jdd.loginserver.network.clientpackets;

import org.l2jdd.Config;
import org.l2jdd.commons.network.IIncomingPacket;
import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.loginserver.LoginController;
import org.l2jdd.loginserver.LoginServer;
import org.l2jdd.loginserver.SessionKey;
import org.l2jdd.loginserver.network.LoginClient;
import org.l2jdd.loginserver.network.gameserverpackets.ServerStatus;
import org.l2jdd.loginserver.network.serverpackets.LoginFail.LoginFailReason;
import org.l2jdd.loginserver.network.serverpackets.PlayFail.PlayFailReason;
import org.l2jdd.loginserver.network.serverpackets.PlayOk;

/**
 * <pre>
 * Format is ddc
 * d: first part of session id
 * d: second part of session id
 * c: server ID
 * </pre>
 */
public class RequestServerLogin implements IIncomingPacket<LoginClient>
{
	private int _skey1;
	private int _skey2;
	private int _serverId;
	
	@Override
	public boolean read(LoginClient client, PacketReader packet)
	{
		if (packet.getReadableBytes() >= 9)
		{
			_skey1 = packet.readD();
			_skey2 = packet.readD();
			_serverId = packet.readC();
			return true;
		}
		return false;
	}
	
	@Override
	public void run(LoginClient client)
	{
		final SessionKey sk = client.getSessionKey();
		
		// if we didnt showed the license we cant check these values
		if (!Config.SHOW_LICENCE || sk.checkLoginPair(_skey1, _skey2))
		{
			if ((LoginServer.getInstance().getStatus() == ServerStatus.STATUS_DOWN) || ((LoginServer.getInstance().getStatus() == ServerStatus.STATUS_GM_ONLY) && (client.getAccessLevel() < 1)))
			{
				client.close(LoginFailReason.REASON_ACCESS_FAILED);
			}
			else if (LoginController.getInstance().isLoginPossible(client, _serverId))
			{
				client.setJoinedGS(true);
				client.sendPacket(new PlayOk(sk));
			}
			else
			{
				client.close(PlayFailReason.REASON_SERVER_OVERLOADED);
			}
		}
		else
		{
			client.close(LoginFailReason.REASON_ACCESS_FAILED);
		}
	}
}
