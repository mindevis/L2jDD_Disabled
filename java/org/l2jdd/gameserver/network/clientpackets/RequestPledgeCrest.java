
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.serverpackets.PledgeCrest;

/**
 * @version $Revision: 1.4.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestPledgeCrest implements IClientIncomingPacket
{
	private int _crestId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_crestId = packet.readD();
		packet.readD(); // clanId
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		client.sendPacket(new PledgeCrest(_crestId));
	}
}
