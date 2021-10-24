
package org.l2jdd.gameserver.util;

import org.l2jdd.Config;
import org.l2jdd.gameserver.data.xml.SendMessageLocalisationData;
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.serverpackets.CreatureSay;
import org.l2jdd.gameserver.network.serverpackets.ExUserInfoAbnormalVisualEffect;

/**
 * @author lord_rex
 */
public class BuilderUtil
{
	private BuilderUtil()
	{
		// utility class
	}
	
	/**
	 * Sends builder system message to the player.
	 * @param player
	 * @param message
	 */
	public static void sendSysMessage(PlayerInstance player, String message)
	{
		if (Config.GM_STARTUP_BUILDER_HIDE)
		{
			player.sendPacket(new CreatureSay(null, ChatType.GENERAL, "SYS", SendMessageLocalisationData.getLocalisation(player, message)));
		}
		else
		{
			player.sendMessage(message);
		}
	}
	
	/**
	 * Sends builder html message to the player.
	 * @param player
	 * @param message
	 */
	public static void sendHtmlMessage(PlayerInstance player, String message)
	{
		player.sendPacket(new CreatureSay(null, ChatType.GENERAL, "HTML", message));
	}
	
	/**
	 * Changes player's hiding state.
	 * @param player
	 * @param hide
	 * @return {@code true} if hide state was changed, otherwise {@code false}
	 */
	public static boolean setHiding(PlayerInstance player, boolean hide)
	{
		if (player.isInvisible() && hide)
		{
			// already hiding
			return false;
		}
		
		if (!player.isInvisible() && !hide)
		{
			// already visible
			return false;
		}
		
		player.setSilenceMode(hide);
		player.setInvul(hide);
		player.setInvisible(hide);
		
		player.broadcastUserInfo();
		player.sendPacket(new ExUserInfoAbnormalVisualEffect(player));
		return true;
	}
}
