
package org.l2jdd.gameserver.network.serverpackets;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.network.OutgoingPackets;

/**
 * @author -Wooden-
 */
public class ShowPCCafeCouponShowUI implements IClientOutgoingPacket
{
	public static final ShowPCCafeCouponShowUI STATIC_PACKET = new ShowPCCafeCouponShowUI();
	
	private ShowPCCafeCouponShowUI()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.SHOW_PCCAFE_COUPON_SHOW_UI.writeId(packet);
		
		return true;
	}
}
