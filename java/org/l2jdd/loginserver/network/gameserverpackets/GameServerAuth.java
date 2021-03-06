
package org.l2jdd.loginserver.network.gameserverpackets;

import java.util.Arrays;
import java.util.logging.Logger;

import org.l2jdd.Config;
import org.l2jdd.commons.network.BaseRecievePacket;
import org.l2jdd.loginserver.GameServerTable;
import org.l2jdd.loginserver.GameServerTable.GameServerInfo;
import org.l2jdd.loginserver.GameServerThread;
import org.l2jdd.loginserver.network.GameServerPacketHandler.GameServerState;
import org.l2jdd.loginserver.network.loginserverpackets.AuthResponse;
import org.l2jdd.loginserver.network.loginserverpackets.LoginServerFail;

/**
 * <pre>
 * Format: cccddb
 * c desired ID
 * c accept alternative ID
 * c reserve Host
 * s ExternalHostName
 * s InetranlHostName
 * d max players
 * d hexid size
 * b hexid
 * </pre>
 * 
 * @author -Wooden-
 */
public class GameServerAuth extends BaseRecievePacket
{
	protected static final Logger LOGGER = Logger.getLogger(GameServerAuth.class.getName());
	GameServerThread _server;
	private final byte[] _hexId;
	private final int _desiredId;
	@SuppressWarnings("unused")
	private final boolean _hostReserved;
	private final boolean _acceptAlternativeId;
	private final int _maxPlayers;
	private final int _port;
	private final String[] _hosts;
	
	/**
	 * @param decrypt
	 * @param server
	 */
	public GameServerAuth(byte[] decrypt, GameServerThread server)
	{
		super(decrypt);
		_server = server;
		_desiredId = readC();
		_acceptAlternativeId = readC() != 0;
		_hostReserved = readC() != 0;
		_port = readH();
		_maxPlayers = readD();
		int size = readD();
		_hexId = readB(size);
		size = 2 * readD();
		_hosts = new String[size];
		for (int i = 0; i < size; i++)
		{
			_hosts[i] = readS();
		}
		
		if (handleRegProcess())
		{
			final AuthResponse ar = new AuthResponse(server.getGameServerInfo().getId());
			server.sendPacket(ar);
			server.setLoginConnectionState(GameServerState.AUTHED);
		}
	}
	
	private boolean handleRegProcess()
	{
		final GameServerTable gameServerTable = GameServerTable.getInstance();
		final int id = _desiredId;
		final byte[] hexId = _hexId;
		GameServerInfo gsi = gameServerTable.getRegisteredGameServerById(id);
		// is there a gameserver registered with this id?
		if (gsi != null)
		{
			// does the hex id match?
			if (Arrays.equals(gsi.getHexId(), hexId))
			{
				// check to see if this GS is already connected
				synchronized (gsi)
				{
					if (gsi.isAuthed())
					{
						_server.forceClose(LoginServerFail.REASON_ALREADY_LOGGED8IN);
						return false;
					}
					_server.attachGameServerInfo(gsi, _port, _hosts, _maxPlayers);
				}
			}
			else
			{
				// there is already a server registered with the desired id and different hex id
				// try to register this one with an alternative id
				if (Config.ACCEPT_NEW_GAMESERVER && _acceptAlternativeId)
				{
					gsi = new GameServerInfo(id, hexId, _server);
					if (gameServerTable.registerWithFirstAvailableId(gsi))
					{
						_server.attachGameServerInfo(gsi, _port, _hosts, _maxPlayers);
						gameServerTable.registerServerOnDB(gsi);
					}
					else
					{
						_server.forceClose(LoginServerFail.REASON_NO_FREE_ID);
						return false;
					}
				}
				else
				{
					// server id is already taken, and we cant get a new one for you
					_server.forceClose(LoginServerFail.REASON_WRONG_HEXID);
					return false;
				}
			}
		}
		else
		{
			// can we register on this id?
			if (Config.ACCEPT_NEW_GAMESERVER)
			{
				gsi = new GameServerInfo(id, hexId, _server);
				if (gameServerTable.register(id, gsi))
				{
					_server.attachGameServerInfo(gsi, _port, _hosts, _maxPlayers);
					gameServerTable.registerServerOnDB(gsi);
				}
				else
				{
					// some one took this ID meanwhile
					_server.forceClose(LoginServerFail.REASON_ID_RESERVED);
					return false;
				}
			}
			else
			{
				_server.forceClose(LoginServerFail.REASON_WRONG_HEXID);
				return false;
			}
		}
		return true;
	}
}
