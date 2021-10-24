
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExRotation;
import org.l2jdd.gameserver.network.serverpackets.SocialAction;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;
import org.l2jdd.gameserver.util.Util;

/**
 * @author JIV
 */
public class AnswerCoupleAction implements IClientIncomingPacket
{
	private int _objectId;
	private int _actionId;
	private int _answer;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_actionId = packet.readD();
		_answer = packet.readD();
		_objectId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		final PlayerInstance target = World.getInstance().getPlayer(_objectId);
		if ((player == null) || (target == null))
		{
			return;
		}
		if ((target.getMultiSocialTarget() != player.getObjectId()) || (target.getMultiSociaAction() != _actionId))
		{
			return;
		}
		if (_answer == 0) // cancel
		{
			target.sendPacket(SystemMessageId.THE_COUPLE_ACTION_WAS_DENIED);
		}
		else if (_answer == 1) // approve
		{
			final int distance = (int) player.calculateDistance2D(target);
			if ((distance > 125) || (distance < 15) || (player.getObjectId() == target.getObjectId()))
			{
				client.sendPacket(SystemMessageId.THE_REQUEST_CANNOT_BE_COMPLETED_BECAUSE_THE_TARGET_DOES_NOT_MEET_LOCATION_REQUIREMENTS);
				target.sendPacket(SystemMessageId.THE_REQUEST_CANNOT_BE_COMPLETED_BECAUSE_THE_TARGET_DOES_NOT_MEET_LOCATION_REQUIREMENTS);
				return;
			}
			int heading = Util.calculateHeadingFrom(player, target);
			player.broadcastPacket(new ExRotation(player.getObjectId(), heading));
			player.setHeading(heading);
			heading = Util.calculateHeadingFrom(target, player);
			target.setHeading(heading);
			target.broadcastPacket(new ExRotation(target.getObjectId(), heading));
			player.broadcastPacket(new SocialAction(player.getObjectId(), _actionId));
			target.broadcastPacket(new SocialAction(_objectId, _actionId));
		}
		else if (_answer == -1) // refused
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.C1_IS_SET_TO_REFUSE_COUPLE_ACTIONS_AND_CANNOT_BE_REQUESTED_FOR_A_COUPLE_ACTION);
			sm.addPcName(player);
			target.sendPacket(sm);
		}
		target.setMultiSocialAction(0, 0);
	}
}
