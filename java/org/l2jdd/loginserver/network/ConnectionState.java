
package org.l2jdd.loginserver.network;

import org.l2jdd.commons.network.IConnectionState;

/**
 * @author Mobius
 */
public enum ConnectionState implements IConnectionState
{
	CONNECTED,
	AUTHED_GG,
	AUTHED_LOGIN;
}
