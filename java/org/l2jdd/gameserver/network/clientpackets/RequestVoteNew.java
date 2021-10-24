
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.xml.FakePlayerData;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExVoteSystemInfo;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;
import org.l2jdd.gameserver.network.serverpackets.UserInfo;

public class RequestVoteNew implements IClientIncomingPacket
{
	private int _targetId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_targetId = packet.readD();
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
		
		final WorldObject object = player.getTarget();
		if (!(object instanceof PlayerInstance))
		{
			if (object == null)
			{
				client.sendPacket(SystemMessageId.SELECT_TARGET);
			}
			else if (object.isFakePlayer() && FakePlayerData.getInstance().isTalkable(object.getName()))
			{
				if (player.getRecomLeft() <= 0)
				{
					client.sendPacket(SystemMessageId.YOU_ARE_OUT_OF_RECOMMENDATIONS_TRY_AGAIN_LATER);
					return;
				}
				
				final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_HAVE_RECOMMENDED_C1_YOU_HAVE_S2_RECOMMENDATIONS_LEFT);
				sm.addString(FakePlayerData.getInstance().getProperName(object.getName()));
				sm.addInt(player.getRecomLeft());
				client.sendPacket(sm);
				
				player.setRecomLeft(player.getRecomLeft() - 1);
				client.sendPacket(new UserInfo(player));
				client.sendPacket(new ExVoteSystemInfo(player));
			}
			else
			{
				client.sendPacket(SystemMessageId.THAT_IS_AN_INCORRECT_TARGET);
			}
			return;
		}
		
		final PlayerInstance target = (PlayerInstance) object;
		if (target.getObjectId() != _targetId)
		{
			return;
		}
		
		if (target == player)
		{
			client.sendPacket(SystemMessageId.YOU_CANNOT_RECOMMEND_YOURSELF);
			return;
		}
		
		if (player.getRecomLeft() <= 0)
		{
			client.sendPacket(SystemMessageId.YOU_ARE_OUT_OF_RECOMMENDATIONS_TRY_AGAIN_LATER);
			return;
		}
		
		if (target.getRecomHave() >= 255)
		{
			client.sendPacket(SystemMessageId.YOUR_SELECTED_TARGET_CAN_NO_LONGER_RECEIVE_A_RECOMMENDATION);
			return;
		}
		
		player.giveRecom(target);
		
		SystemMessage sm = new SystemMessage(SystemMessageId.YOU_HAVE_RECOMMENDED_C1_YOU_HAVE_S2_RECOMMENDATIONS_LEFT);
		sm.addPcName(target);
		sm.addInt(player.getRecomLeft());
		client.sendPacket(sm);
		
		sm = new SystemMessage(SystemMessageId.YOU_HAVE_BEEN_RECOMMENDED_BY_C1);
		sm.addPcName(player);
		target.sendPacket(sm);
		
		client.sendPacket(new UserInfo(player));
		target.broadcastUserInfo();
		
		client.sendPacket(new ExVoteSystemInfo(player));
		target.sendPacket(new ExVoteSystemInfo(target));
	}
}
