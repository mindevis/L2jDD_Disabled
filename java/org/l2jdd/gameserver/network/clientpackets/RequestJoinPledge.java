
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.xml.FakePlayerData;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.AskJoinPledge;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @version $Revision: 1.3.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestJoinPledge implements IClientIncomingPacket
{
	private int _target;
	private int _pledgeType;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_target = packet.readD();
		_pledgeType = packet.readD();
		return true;
	}
	
	private void scheduleDeny(PlayerInstance player, String name)
	{
		if (player != null)
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.S1_DID_NOT_RESPOND_INVITATION_TO_THE_CLAN_HAS_BEEN_CANCELLED);
			sm.addString(name);
			player.sendPacket(sm);
			player.onTransactionResponse();
		}
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Clan clan = player.getClan();
		if (clan == null)
		{
			return;
		}
		
		if ((player.getTarget() != null) && (FakePlayerData.getInstance().isTalkable(player.getTarget().getName())))
		{
			if (FakePlayerData.getInstance().getInfo(player.getTarget().getId()).getClanId() > 0)
			{
				player.sendPacket(SystemMessageId.THAT_PLAYER_ALREADY_BELONGS_TO_ANOTHER_CLAN);
			}
			else
			{
				if (!player.isProcessingRequest())
				{
					ThreadPool.schedule(() -> scheduleDeny(player, player.getTarget().getName()), 10000);
					player.blockRequest();
				}
				else
				{
					final SystemMessage msg = new SystemMessage(SystemMessageId.C1_IS_ON_ANOTHER_TASK_PLEASE_TRY_AGAIN_LATER);
					msg.addString(player.getTarget().getName());
					player.sendPacket(msg);
				}
			}
			return;
		}
		
		final PlayerInstance target = World.getInstance().getPlayer(_target);
		if (target == null)
		{
			player.sendPacket(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET);
			return;
		}
		
		if (!clan.checkClanJoinCondition(player, target, _pledgeType))
		{
			return;
		}
		
		if (!player.getRequest().setRequest(target, this))
		{
			return;
		}
		
		final String pledgeName = player.getClan().getName();
		target.sendPacket(new AskJoinPledge(player, _pledgeType, pledgeName));
	}
	
	public int getPledgeType()
	{
		return _pledgeType;
	}
}
