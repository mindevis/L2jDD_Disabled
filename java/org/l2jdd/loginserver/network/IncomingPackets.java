
package org.l2jdd.loginserver.network;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.l2jdd.commons.network.IConnectionState;
import org.l2jdd.commons.network.IIncomingPacket;
import org.l2jdd.commons.network.IIncomingPackets;
import org.l2jdd.loginserver.network.clientpackets.AuthGameGuard;
import org.l2jdd.loginserver.network.clientpackets.RequestAuthLogin;
import org.l2jdd.loginserver.network.clientpackets.RequestCmdLogin;
import org.l2jdd.loginserver.network.clientpackets.RequestPIAgreement;
import org.l2jdd.loginserver.network.clientpackets.RequestPIAgreementCheck;
import org.l2jdd.loginserver.network.clientpackets.RequestServerList;
import org.l2jdd.loginserver.network.clientpackets.RequestServerLogin;

/**
 * @author Mobius
 */
public enum IncomingPackets implements IIncomingPackets<LoginClient>
{
	AUTH_GAME_GUARD(0x07, AuthGameGuard::new, ConnectionState.CONNECTED),
	REQUEST_AUTH_LOGIN(0x00, RequestAuthLogin::new, ConnectionState.AUTHED_GG),
	REQUEST_LOGIN(0x0B, RequestCmdLogin::new, ConnectionState.AUTHED_GG),
	REQUEST_SERVER_LOGIN(0x02, RequestServerLogin::new, ConnectionState.AUTHED_LOGIN),
	REQUEST_SERVER_LIST(0x05, RequestServerList::new, ConnectionState.AUTHED_LOGIN),
	REQUEST_PI_AGREEMENT_CHECK(0x0E, RequestPIAgreementCheck::new, ConnectionState.AUTHED_LOGIN),
	REQUEST_PI_AGREEMENT(0x0F, RequestPIAgreement::new, ConnectionState.AUTHED_LOGIN);
	
	public static final IncomingPackets[] PACKET_ARRAY;
	
	static
	{
		final short maxPacketId = (short) Arrays.stream(values()).mapToInt(IIncomingPackets::getPacketId).max().orElse(0);
		PACKET_ARRAY = new IncomingPackets[maxPacketId + 1];
		for (IncomingPackets incomingPacket : values())
		{
			PACKET_ARRAY[incomingPacket.getPacketId()] = incomingPacket;
		}
	}
	
	private short _packetId;
	private Supplier<IIncomingPacket<LoginClient>> _incomingPacketFactory;
	private Set<IConnectionState> _connectionStates;
	
	IncomingPackets(int packetId, Supplier<IIncomingPacket<LoginClient>> incomingPacketFactory, IConnectionState... connectionStates)
	{
		// packetId is an unsigned byte
		if (packetId > 0xFF)
		{
			throw new IllegalArgumentException("packetId must not be bigger than 0xFF");
		}
		
		_packetId = (short) packetId;
		_incomingPacketFactory = incomingPacketFactory != null ? incomingPacketFactory : () -> null;
		_connectionStates = new HashSet<>(Arrays.asList(connectionStates));
	}
	
	@Override
	public int getPacketId()
	{
		return _packetId;
	}
	
	@Override
	public IIncomingPacket<LoginClient> newIncomingPacket()
	{
		return _incomingPacketFactory.get();
	}
	
	@Override
	public Set<IConnectionState> getConnectionStates()
	{
		return _connectionStates;
	}
}
