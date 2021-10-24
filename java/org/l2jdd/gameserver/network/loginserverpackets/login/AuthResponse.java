
package org.l2jdd.gameserver.network.loginserverpackets.login;

import org.l2jdd.commons.network.BaseRecievePacket;

/**
 * @author -Wooden-
 */
public class AuthResponse extends BaseRecievePacket
{
	private final int _serverId;
	private final String _serverName;
	
	/**
	 * @param decrypt
	 */
	public AuthResponse(byte[] decrypt)
	{
		super(decrypt);
		_serverId = readC();
		_serverName = readS();
	}
	
	/**
	 * @return Returns the serverId.
	 */
	public int getServerId()
	{
		return _serverId;
	}
	
	/**
	 * @return Returns the serverName.
	 */
	public String getServerName()
	{
		return _serverName;
	}
}
