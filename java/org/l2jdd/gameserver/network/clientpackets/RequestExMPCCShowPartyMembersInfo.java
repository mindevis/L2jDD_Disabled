
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.serverpackets.ExMPCCShowPartyMemberInfo;

/**
 * Format:(ch) d
 * @author chris_00
 */
public class RequestExMPCCShowPartyMembersInfo implements IClientIncomingPacket
{
	private int _partyLeaderId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_partyLeaderId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final PlayerInstance target = World.getInstance().getPlayer(_partyLeaderId);
		if ((target != null) && (target.getParty() != null))
		{
			client.sendPacket(new ExMPCCShowPartyMemberInfo(target.getParty()));
		}
	}
}
