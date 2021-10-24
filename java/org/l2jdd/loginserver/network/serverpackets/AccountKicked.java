
package org.l2jdd.loginserver.network.serverpackets;

import org.l2jdd.commons.network.IOutgoingPacket;
import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.loginserver.network.OutgoingPackets;

/**
 * @author KenM
 */
public class AccountKicked implements IOutgoingPacket
{
	public enum AccountKickedReason
	{
		REASON_DATA_STEALER(0x01),
		REASON_GENERIC_VIOLATION(0x08),
		REASON_7_DAYS_SUSPENDED(0x10),
		REASON_PERMANENTLY_BANNED(0x20);
		
		private final int _code;
		
		AccountKickedReason(int code)
		{
			_code = code;
		}
		
		public int getCode()
		{
			return _code;
		}
	}
	
	private final AccountKickedReason _reason;
	
	/**
	 * @param reason
	 */
	public AccountKicked(AccountKickedReason reason)
	{
		_reason = reason;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.ACCOUNT_KICKED.writeId(packet);
		packet.writeD(_reason.getCode());
		
		return true;
	}
}
