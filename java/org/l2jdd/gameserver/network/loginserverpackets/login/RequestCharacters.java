
package org.l2jdd.gameserver.network.loginserverpackets.login;

import org.l2jdd.commons.network.BaseRecievePacket;

/**
 * @author mrTJO Thanks to mochitto
 */
public class RequestCharacters extends BaseRecievePacket
{
	private final String _account;
	
	public RequestCharacters(byte[] decrypt)
	{
		super(decrypt);
		_account = readS();
	}
	
	/**
	 * @return Return account name
	 */
	public String getAccount()
	{
		return _account;
	}
}
