
package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import org.l2jdd.commons.util.CommonUtil;
import org.l2jdd.gameserver.cache.HtmCache;
import org.l2jdd.gameserver.data.sql.ClanTable;
import org.l2jdd.gameserver.enums.CastleSide;
import org.l2jdd.gameserver.handler.IAdminCommandHandler;
import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jdd.gameserver.util.BuilderUtil;
import org.l2jdd.gameserver.util.Util;

/**
 * Admin Castle manage admin commands.
 * @author St3eT
 */
public class AdminCastle implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_castlemanage",
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		final String actualCommand = st.nextToken();
		if (actualCommand.equals("admin_castlemanage"))
		{
			if (st.hasMoreTokens())
			{
				final String param = st.nextToken();
				final Castle castle;
				if (Util.isDigit(param))
				{
					castle = CastleManager.getInstance().getCastleById(Integer.parseInt(param));
				}
				else
				{
					castle = CastleManager.getInstance().getCastle(param);
				}
				
				if (castle == null)
				{
					BuilderUtil.sendSysMessage(activeChar, "Invalid parameters! Usage: //castlemanage <castleId[1-9] / castleName>");
					return false;
				}
				
				if (!st.hasMoreTokens())
				{
					showCastleMenu(activeChar, castle.getResidenceId());
				}
				else
				{
					final String action = st.nextToken();
					final PlayerInstance target = checkTarget(activeChar) ? activeChar.getTarget().getActingPlayer() : null;
					switch (action)
					{
						case "showRegWindow":
						{
							castle.getSiege().listRegisterClan(activeChar);
							break;
						}
						case "addAttacker":
						{
							if (checkTarget(activeChar))
							{
								castle.getSiege().registerAttacker(target, true);
							}
							else
							{
								activeChar.sendPacket(SystemMessageId.INVALID_TARGET);
							}
							break;
						}
						case "removeAttacker":
						{
							if (checkTarget(activeChar))
							{
								castle.getSiege().removeSiegeClan(activeChar);
							}
							else
							{
								activeChar.sendPacket(SystemMessageId.INVALID_TARGET);
							}
							break;
						}
						case "addDeffender":
						{
							if (checkTarget(activeChar))
							{
								castle.getSiege().registerDefender(target, true);
							}
							else
							{
								activeChar.sendPacket(SystemMessageId.INVALID_TARGET);
							}
							break;
						}
						case "removeDeffender":
						{
							if (checkTarget(activeChar))
							{
								castle.getSiege().removeSiegeClan(target);
							}
							else
							{
								activeChar.sendPacket(SystemMessageId.INVALID_TARGET);
							}
							break;
						}
						case "startSiege":
						{
							if (!castle.getSiege().getAttackerClans().isEmpty())
							{
								castle.getSiege().startSiege();
							}
							else
							{
								BuilderUtil.sendSysMessage(activeChar, "There is currently not registered any clan for castle siege!");
							}
							break;
						}
						case "stopSiege":
						{
							if (castle.getSiege().isInProgress())
							{
								castle.getSiege().endSiege();
							}
							else
							{
								BuilderUtil.sendSysMessage(activeChar, "Castle siege is not currently in progress!");
							}
							showCastleMenu(activeChar, castle.getResidenceId());
							break;
						}
						case "setOwner":
						{
							if ((target == null) || !checkTarget(activeChar))
							{
								activeChar.sendPacket(SystemMessageId.INVALID_TARGET);
							}
							else if (target.getClan().getCastleId() > 0)
							{
								BuilderUtil.sendSysMessage(activeChar, "This clan already have castle!");
							}
							else if (castle.getOwner() != null)
							{
								BuilderUtil.sendSysMessage(activeChar, "This castle is already taken by another clan!");
							}
							else if (!st.hasMoreTokens())
							{
								BuilderUtil.sendSysMessage(activeChar, "Invalid parameters!!");
							}
							else
							{
								final CastleSide side = Enum.valueOf(CastleSide.class, st.nextToken().toUpperCase());
								if (side != null)
								{
									castle.setSide(side);
									castle.setOwner(target.getClan());
								}
							}
							showCastleMenu(activeChar, castle.getResidenceId());
							break;
						}
						case "takeCastle":
						{
							final Clan clan = ClanTable.getInstance().getClan(castle.getOwnerId());
							if (clan != null)
							{
								castle.removeOwner(clan);
							}
							else
							{
								BuilderUtil.sendSysMessage(activeChar, "Error during removing castle!");
							}
							showCastleMenu(activeChar, castle.getResidenceId());
							break;
						}
						case "switchSide":
						{
							if (castle.getSide() == CastleSide.DARK)
							{
								castle.setSide(CastleSide.LIGHT);
							}
							else if (castle.getSide() == CastleSide.LIGHT)
							{
								castle.setSide(CastleSide.DARK);
							}
							else
							{
								BuilderUtil.sendSysMessage(activeChar, "You can't switch sides when is castle neutral!");
							}
							showCastleMenu(activeChar, castle.getResidenceId());
							break;
						}
					}
				}
			}
			else
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(0, 1);
				html.setHtml(HtmCache.getInstance().getHtm(activeChar, "data/html/admin/castlemanage.htm"));
				activeChar.sendPacket(html);
			}
		}
		return true;
	}
	
	private void showCastleMenu(PlayerInstance player, int castleId)
	{
		final Castle castle = CastleManager.getInstance().getCastleById(castleId);
		if (castle != null)
		{
			final Clan ownerClan = castle.getOwner();
			final NpcHtmlMessage html = new NpcHtmlMessage(0, 1);
			html.setHtml(HtmCache.getInstance().getHtm(player, "data/html/admin/castlemanage_selected.htm"));
			html.replace("%castleId%", castle.getResidenceId());
			html.replace("%castleName%", castle.getName());
			html.replace("%ownerName%", ownerClan != null ? ownerClan.getLeaderName() : "NPC");
			html.replace("%ownerClan%", ownerClan != null ? ownerClan.getName() : "NPC");
			html.replace("%castleSide%", CommonUtil.capitalizeFirst(castle.getSide().toString().toLowerCase()));
			player.sendPacket(html);
		}
	}
	
	private boolean checkTarget(PlayerInstance player)
	{
		return ((player.getTarget() != null) && player.getTarget().isPlayer() && (((PlayerInstance) player.getTarget()).getClan() != null));
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}