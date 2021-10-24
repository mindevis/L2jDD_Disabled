
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;

/**
 * @author ShanSoft
 * @structure chddSdS
 */
public class RequestModifyBookMarkSlot implements IClientIncomingPacket
{
	private int id;
	private int icon;
	private String name;
	private String tag;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		id = packet.readD();
		name = packet.readS();
		icon = packet.readD();
		tag = packet.readS();
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
		player.teleportBookmarkModify(id, icon, tag, name);
	}
}
