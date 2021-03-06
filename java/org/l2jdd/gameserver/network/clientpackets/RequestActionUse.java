
package org.l2jdd.gameserver.network.clientpackets;

import java.util.Arrays;
import java.util.logging.Logger;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.xml.ActionData;
import org.l2jdd.gameserver.enums.PrivateStoreType;
import org.l2jdd.gameserver.handler.IPlayerActionHandler;
import org.l2jdd.gameserver.handler.PlayerActionHandler;
import org.l2jdd.gameserver.model.ActionDataHolder;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.skills.AbnormalType;
import org.l2jdd.gameserver.model.skills.BuffInfo;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ActionFailed;
import org.l2jdd.gameserver.network.serverpackets.ExBasicActionList;
import org.l2jdd.gameserver.network.serverpackets.RecipeShopManageList;

/**
 * This class manages the action use request packet.
 * @author Zoey76
 */
public class RequestActionUse implements IClientIncomingPacket
{
	private static final Logger LOGGER = Logger.getLogger(RequestActionUse.class.getName());
	
	private int _actionId;
	private boolean _ctrlPressed;
	private boolean _shiftPressed;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_actionId = packet.readD();
		_ctrlPressed = (packet.readD() == 1);
		_shiftPressed = (packet.readC() == 1);
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
		
		// Don't do anything if player is dead or confused
		if ((player.isFakeDeath() && (_actionId != 0)) || player.isDead() || player.isControlBlocked())
		{
			client.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final BuffInfo info = player.getEffectList().getFirstBuffInfoByAbnormalType(AbnormalType.BOT_PENALTY);
		if (info != null)
		{
			for (AbstractEffect effect : info.getEffects())
			{
				if (!effect.checkCondition(_actionId))
				{
					player.sendPacket(SystemMessageId.YOU_HAVE_BEEN_REPORTED_AS_AN_ILLEGAL_PROGRAM_USER_SO_YOUR_ACTIONS_HAVE_BEEN_RESTRICTED);
					player.sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}
			}
		}
		
		// Don't allow to do some action if player is transformed
		if (player.isTransformed())
		{
			final int[] allowedActions = player.isTransformed() ? ExBasicActionList.ACTIONS_ON_TRANSFORM : ExBasicActionList.DEFAULT_ACTION_LIST;
			if (Arrays.binarySearch(allowedActions, _actionId) < 0)
			{
				client.sendPacket(ActionFailed.STATIC_PACKET);
				LOGGER.warning("Player " + player + " used action which he does not have! Id = " + _actionId + " transform: " + player.getTransformation().get().getId());
				return;
			}
		}
		
		final ActionDataHolder actionHolder = ActionData.getInstance().getActionData(_actionId);
		if (actionHolder != null)
		{
			final IPlayerActionHandler actionHandler = PlayerActionHandler.getInstance().getHandler(actionHolder.getHandler());
			if (actionHandler != null)
			{
				actionHandler.useAction(player, actionHolder, _ctrlPressed, _shiftPressed);
				return;
			}
			LOGGER.warning("Couldnt find handler with name: " + actionHolder.getHandler());
			return;
		}
		
		switch (_actionId)
		{
			case 51: // General Manufacture
			{
				// Player shouldn't be able to set stores if he/she is alike dead (dead or fake death)
				if (player.isAlikeDead())
				{
					client.sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}
				
				if (player.isSellingBuffs())
				{
					client.sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}
				
				if (player.getPrivateStoreType() != PrivateStoreType.NONE)
				{
					player.setPrivateStoreType(PrivateStoreType.NONE);
					player.broadcastUserInfo();
				}
				if (player.isSitting())
				{
					player.standUp();
				}
				
				client.sendPacket(new RecipeShopManageList(player, false));
				break;
			}
			default:
			{
				LOGGER.warning(player.getName() + ": unhandled action type " + _actionId);
				break;
			}
		}
	}
}
