
package handlers.voicedcommandhandlers;

import java.util.StringTokenizer;

import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.data.sql.CharNameTable;
import org.l2jdd.gameserver.data.xml.AdminData;
import org.l2jdd.gameserver.handler.IVoicedCommandHandler;
import org.l2jdd.gameserver.instancemanager.PunishmentManager;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.punishment.PunishmentAffect;
import org.l2jdd.gameserver.model.punishment.PunishmentTask;
import org.l2jdd.gameserver.model.punishment.PunishmentType;
import org.l2jdd.gameserver.model.skills.AbnormalVisualEffect;
import org.l2jdd.gameserver.util.BuilderUtil;
import org.l2jdd.gameserver.util.Util;

public class ChatAdmin implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"banchat",
		"chatban",
		"unbanchat",
		"chatunban"
	};
	
	@Override
	public boolean useVoicedCommand(String command, PlayerInstance activeChar, String params)
	{
		if (!AdminData.getInstance().hasAccess(command, activeChar.getAccessLevel()))
		{
			return false;
		}
		
		switch (command)
		{
			case "banchat":
			case "chatban":
			{
				if (params == null)
				{
					BuilderUtil.sendSysMessage(activeChar, "Usage: .banchat name [minutes]");
					return true;
				}
				final StringTokenizer st = new StringTokenizer(params);
				if (st.hasMoreTokens())
				{
					final String name = st.nextToken();
					long expirationTime = 0;
					if (st.hasMoreTokens())
					{
						final String token = st.nextToken();
						if (Util.isDigit(token))
						{
							expirationTime = Integer.parseInt(token);
						}
					}
					
					final int objId = CharNameTable.getInstance().getIdByName(name);
					if (objId > 0)
					{
						final PlayerInstance player = World.getInstance().getPlayer(objId);
						if ((player == null) || !player.isOnline())
						{
							BuilderUtil.sendSysMessage(activeChar, "Player not online!");
							return false;
						}
						if (player.isChatBanned())
						{
							BuilderUtil.sendSysMessage(activeChar, "Player is already punished!");
							return false;
						}
						if (player == activeChar)
						{
							BuilderUtil.sendSysMessage(activeChar, "You can't ban yourself!");
							return false;
						}
						if (player.isGM())
						{
							BuilderUtil.sendSysMessage(activeChar, "You can't ban a GM!");
							return false;
						}
						if (AdminData.getInstance().hasAccess(command, player.getAccessLevel()))
						{
							BuilderUtil.sendSysMessage(activeChar, "You can't ban moderator!");
							return false;
						}
						
						PunishmentManager.getInstance().startPunishment(new PunishmentTask(objId, PunishmentAffect.CHARACTER, PunishmentType.CHAT_BAN, Chronos.currentTimeMillis() + (expirationTime * 1000 * 60), "Chat banned by moderator", activeChar.getName()));
						if (expirationTime > 0)
						{
							BuilderUtil.sendSysMessage(activeChar, "Player " + player.getName() + " chat banned for " + expirationTime + " minutes.");
						}
						else
						{
							BuilderUtil.sendSysMessage(activeChar, "Player " + player.getName() + " chat banned forever.");
						}
						player.sendMessage("Chat banned by moderator " + activeChar.getName());
						player.getEffectList().startAbnormalVisualEffect(AbnormalVisualEffect.NO_CHAT);
					}
					else
					{
						BuilderUtil.sendSysMessage(activeChar, "Player not found!");
						return false;
					}
				}
				break;
			}
			case "unbanchat":
			case "chatunban":
			{
				if (params == null)
				{
					BuilderUtil.sendSysMessage(activeChar, "Usage: .unbanchat name");
					return true;
				}
				final StringTokenizer st = new StringTokenizer(params);
				if (st.hasMoreTokens())
				{
					final String name = st.nextToken();
					final int objId = CharNameTable.getInstance().getIdByName(name);
					if (objId > 0)
					{
						final PlayerInstance player = World.getInstance().getPlayer(objId);
						if ((player == null) || !player.isOnline())
						{
							BuilderUtil.sendSysMessage(activeChar, "Player not online!");
							return false;
						}
						if (!player.isChatBanned())
						{
							BuilderUtil.sendSysMessage(activeChar, "Player is not chat banned!");
							return false;
						}
						
						PunishmentManager.getInstance().stopPunishment(objId, PunishmentAffect.CHARACTER, PunishmentType.CHAT_BAN);
						BuilderUtil.sendSysMessage(activeChar, "Player " + player.getName() + " chat unbanned.");
						player.sendMessage("Chat unbanned by moderator " + activeChar.getName());
						player.getEffectList().stopAbnormalVisualEffect(AbnormalVisualEffect.NO_CHAT);
					}
					else
					{
						BuilderUtil.sendSysMessage(activeChar, "Player not found!");
						return false;
					}
				}
				break;
			}
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}
