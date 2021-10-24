
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.ExAskJoinMPCC;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * Format: (ch) S<br>
 * D0 0D 00 5A 00 77 00 65 00 72 00 67 00 00 00
 * @author chris_00
 */
public class RequestExAskJoinMPCC implements IClientIncomingPacket
{
	private String _name;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_name = packet.readS();
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
		
		final PlayerInstance target = World.getInstance().getPlayer(_name);
		if (target == null)
		{
			return;
		}
		// invite yourself? ;)
		if (player.isInParty() && target.isInParty() && player.getParty().equals(target.getParty()))
		{
			return;
		}
		
		SystemMessage sm;
		// activeChar is in a Party?
		if (player.isInParty())
		{
			final Party activeParty = player.getParty();
			// activeChar is PartyLeader? && activeChars Party is already in a CommandChannel?
			if (activeParty.getLeader().equals(player))
			{
				// if activeChars Party is in CC, is activeChar CCLeader?
				if (activeParty.isInCommandChannel() && activeParty.getCommandChannel().getLeader().equals(player))
				{
					// in CC and the CCLeader
					// target in a party?
					if (target.isInParty())
					{
						// targets party already in a CChannel?
						if (target.getParty().isInCommandChannel())
						{
							sm = new SystemMessage(SystemMessageId.C1_S_PARTY_IS_ALREADY_A_MEMBER_OF_THE_COMMAND_CHANNEL);
							sm.addString(target.getName());
							player.sendPacket(sm);
						}
						else
						{
							// ready to open a new CC
							// send request to targets Party's PartyLeader
							askJoinMPCC(player, target);
						}
					}
					else
					{
						player.sendMessage(target.getName() + " doesn't have party and cannot be invited to Command Channel.");
					}
				}
				else if (activeParty.isInCommandChannel() && !activeParty.getCommandChannel().getLeader().equals(player))
				{
					// in CC, but not the CCLeader
					sm = new SystemMessage(SystemMessageId.YOU_DO_NOT_HAVE_AUTHORITY_TO_INVITE_SOMEONE_TO_THE_COMMAND_CHANNEL);
					player.sendPacket(sm);
				}
				else
				{
					// target in a party?
					if (target.isInParty())
					{
						// targets party already in a CChannel?
						if (target.getParty().isInCommandChannel())
						{
							sm = new SystemMessage(SystemMessageId.C1_S_PARTY_IS_ALREADY_A_MEMBER_OF_THE_COMMAND_CHANNEL);
							sm.addString(target.getName());
							player.sendPacket(sm);
						}
						else
						{
							// ready to open a new CC
							// send request to targets Party's PartyLeader
							askJoinMPCC(player, target);
						}
					}
					else
					{
						player.sendMessage(target.getName() + " doesn't have party and cannot be invited to Command Channel.");
					}
				}
			}
			else
			{
				player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_AUTHORITY_TO_INVITE_SOMEONE_TO_THE_COMMAND_CHANNEL);
			}
		}
	}
	
	private void askJoinMPCC(PlayerInstance requestor, PlayerInstance target)
	{
		boolean hasRight = false;
		if (requestor.isClanLeader() && (requestor.getClan().getLevel() >= 5))
		{
			// Clan leader of level5 Clan or higher.
			hasRight = true;
		}
		else if (requestor.getInventory().getItemByItemId(8871) != null)
		{
			// 8871 Strategy Guide.
			// TODO: Should destroyed after successful invite?
			hasRight = true;
		}
		else if ((requestor.getPledgeClass() >= 5) && (requestor.getKnownSkill(391) != null))
		{
			// At least Baron or higher and the skill Clan Imperium
			hasRight = true;
		}
		
		if (!hasRight)
		{
			requestor.sendPacket(SystemMessageId.NO_CLANS_DECLARED_A_WAR_ON_YOU);
			return;
		}
		
		// Get the target's party leader, and do whole actions on him.
		final PlayerInstance targetLeader = target.getParty().getLeader();
		SystemMessage sm;
		if (!targetLeader.isProcessingRequest())
		{
			requestor.onTransactionRequest(targetLeader);
			sm = new SystemMessage(SystemMessageId.C1_IS_INVITING_YOU_TO_A_COMMAND_CHANNEL_DO_YOU_ACCEPT);
			sm.addString(requestor.getName());
			targetLeader.sendPacket(sm);
			targetLeader.sendPacket(new ExAskJoinMPCC(requestor.getName()));
			requestor.sendMessage("You invited " + targetLeader.getName() + " to your Command Channel.");
		}
		else
		{
			sm = new SystemMessage(SystemMessageId.C1_IS_ON_ANOTHER_TASK_PLEASE_TRY_AGAIN_LATER);
			sm.addString(targetLeader.getName());
			requestor.sendPacket(sm);
		}
	}
}
