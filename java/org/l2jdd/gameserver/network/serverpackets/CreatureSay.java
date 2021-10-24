
package org.l2jdd.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import org.l2jdd.commons.network.PacketWriter;
import org.l2jdd.gameserver.enums.ChatType;
import org.l2jdd.gameserver.instancemanager.MentorManager;
import org.l2jdd.gameserver.instancemanager.RankManager;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.network.NpcStringId;
import org.l2jdd.gameserver.network.OutgoingPackets;
import org.l2jdd.gameserver.network.SystemMessageId;

public class CreatureSay implements IClientOutgoingPacket
{
	private final Creature _sender;
	private final ChatType _chatType;
	private String _senderName = null;
	private String _text = null;
	private int _charId = 0;
	private int _messageId = -1;
	private int _mask;
	private List<String> _parameters;
	
	/**
	 * @param sender
	 * @param receiver
	 * @param name
	 * @param chatType
	 * @param text
	 */
	public CreatureSay(PlayerInstance sender, PlayerInstance receiver, String name, ChatType chatType, String text)
	{
		_sender = sender;
		_senderName = name;
		_chatType = chatType;
		_text = text;
		if (receiver != null)
		{
			if (receiver.getFriendList().contains(sender.getObjectId()))
			{
				_mask |= 0x01;
			}
			if ((receiver.getClanId() > 0) && (receiver.getClanId() == sender.getClanId()))
			{
				_mask |= 0x02;
			}
			if ((MentorManager.getInstance().getMentee(receiver.getObjectId(), sender.getObjectId()) != null) || (MentorManager.getInstance().getMentee(sender.getObjectId(), receiver.getObjectId()) != null))
			{
				_mask |= 0x04;
			}
			if ((receiver.getAllyId() > 0) && (receiver.getAllyId() == sender.getAllyId()))
			{
				_mask |= 0x08;
			}
		}
		
		// Does not shows level
		if (sender.isGM())
		{
			_mask |= 0x10;
		}
	}
	
	public CreatureSay(Creature sender, ChatType chatType, String senderName, String text)
	{
		_sender = sender;
		_chatType = chatType;
		_senderName = senderName;
		_text = text;
	}
	
	public CreatureSay(Creature sender, ChatType chatType, NpcStringId npcStringId)
	{
		_sender = sender;
		_chatType = chatType;
		_messageId = npcStringId.getId();
	}
	
	public CreatureSay(ChatType chatType, int charId, SystemMessageId systemMessageId)
	{
		_sender = null;
		_chatType = chatType;
		_charId = charId;
		_messageId = systemMessageId.getId();
	}
	
	/**
	 * String parameter for argument S1,S2,.. in npcstring-e.dat
	 * @param text
	 */
	public void addStringParameter(String text)
	{
		if (_parameters == null)
		{
			_parameters = new ArrayList<>();
		}
		_parameters.add(text);
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.SAY2.writeId(packet);
		
		packet.writeD(_sender == null ? 0 : _sender.getObjectId());
		packet.writeD(_chatType.getClientId());
		if (_senderName != null)
		{
			packet.writeS(_senderName);
		}
		else
		{
			packet.writeD(_charId);
		}
		packet.writeD(_messageId); // High Five NPCString ID
		if (_text != null)
		{
			packet.writeS(_text);
			if ((_sender != null) && (_sender.isPlayer() || _sender.isFakePlayer()) && (_chatType == ChatType.WHISPER))
			{
				packet.writeC(_mask);
				if ((_mask & 0x10) == 0)
				{
					packet.writeC(_sender.getLevel());
				}
			}
		}
		else if (_parameters != null)
		{
			for (String s : _parameters)
			{
				packet.writeS(s);
			}
		}
		
		// Rank
		if ((_sender != null) && _sender.isPlayer())
		{
			final Clan clan = _sender.getClan();
			if ((clan != null) && ((_chatType == ChatType.CLAN) || (_chatType == ChatType.ALLIANCE)))
			{
				packet.writeC(0); // unknown clan byte
			}
			
			final int rank = RankManager.getInstance().getPlayerGlobalRank(_sender.getActingPlayer());
			if ((rank == 0) || (rank > 100))
			{
				packet.writeC(0);
			}
			else if (rank <= 10)
			{
				packet.writeC(1);
			}
			else if (rank <= 50)
			{
				packet.writeC(2);
			}
			else if (rank <= 100)
			{
				packet.writeC(3);
			}
			
			if (clan != null)
			{
				packet.writeC(clan.getCastleId());
			}
			else
			{
				packet.writeC(0);
			}
		}
		else
		{
			packet.writeC(0);
		}
		
		return true;
	}
	
	@Override
	public void runImpl(PlayerInstance player)
	{
		if (player != null)
		{
			player.broadcastSnoop(_chatType, _senderName, _text);
		}
	}
}
