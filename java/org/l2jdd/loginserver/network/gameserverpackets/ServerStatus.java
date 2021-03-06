
package org.l2jdd.loginserver.network.gameserverpackets;

import java.util.logging.Logger;

import org.l2jdd.commons.network.BaseRecievePacket;
import org.l2jdd.loginserver.GameServerTable;
import org.l2jdd.loginserver.GameServerTable.GameServerInfo;
import org.l2jdd.loginserver.GameServerThread;

/**
 * @author -Wooden-
 */
public class ServerStatus extends BaseRecievePacket
{
	protected static final Logger LOGGER = Logger.getLogger(ServerStatus.class.getName());
	
	public static final int SERVER_LIST_STATUS = 0x01;
	public static final int SERVER_TYPE = 0x02;
	public static final int SERVER_LIST_SQUARE_BRACKET = 0x03;
	public static final int MAX_PLAYERS = 0x04;
	public static final int TEST_SERVER = 0x05;
	public static final int SERVER_AGE = 0x06;
	
	// Server Status
	public static final int STATUS_AUTO = 0x00;
	public static final int STATUS_GOOD = 0x01;
	public static final int STATUS_NORMAL = 0x02;
	public static final int STATUS_FULL = 0x03;
	public static final int STATUS_DOWN = 0x04;
	public static final int STATUS_GM_ONLY = 0x05;
	
	// Server Types
	public static final int SERVER_NORMAL = 0x01;
	public static final int SERVER_RELAX = 0x02;
	public static final int SERVER_TEST = 0x04;
	public static final int SERVER_NOLABEL = 0x08;
	public static final int SERVER_CREATION_RESTRICTED = 0x10;
	public static final int SERVER_EVENT = 0x20;
	public static final int SERVER_FREE = 0x40;
	
	// Server Ages
	public static final int SERVER_AGE_ALL = 0x00;
	public static final int SERVER_AGE_15 = 0x0F;
	public static final int SERVER_AGE_18 = 0x12;
	
	public static final int ON = 0x01;
	public static final int OFF = 0x00;
	
	/**
	 * @param decrypt
	 * @param server
	 */
	public ServerStatus(byte[] decrypt, GameServerThread server)
	{
		super(decrypt);
		
		final GameServerInfo gsi = GameServerTable.getInstance().getRegisteredGameServerById(server.getServerId());
		if (gsi != null)
		{
			final int size = readD();
			for (int i = 0; i < size; i++)
			{
				final int type = readD();
				final int value = readD();
				switch (type)
				{
					case SERVER_LIST_STATUS:
					{
						gsi.setStatus(value);
						break;
					}
					case SERVER_LIST_SQUARE_BRACKET:
					{
						gsi.setShowingBrackets(value == ON);
						break;
					}
					case MAX_PLAYERS:
					{
						gsi.setMaxPlayers(value);
						break;
					}
					case SERVER_TYPE:
					{
						gsi.setServerType(value);
						break;
					}
					case SERVER_AGE:
					{
						gsi.setAgeLimit(value);
						break;
					}
				}
			}
		}
	}
}
