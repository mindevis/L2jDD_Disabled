
package org.l2jdd.gameserver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.enums.Race;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.CreatureSay;
import org.l2jdd.gameserver.network.serverpackets.IClientOutgoingPacket;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Battlecruiser
 */
public abstract class AbstractPlayerGroup
{
	/**
	 * @return a list of all members of this group
	 */
	public abstract List<PlayerInstance> getMembers();
	
	/**
	 * @return a list of object IDs of the members of this group
	 */
	public List<Integer> getMembersObjectId()
	{
		final List<Integer> ids = new ArrayList<>();
		forEachMember(m ->
		{
			ids.add(m.getObjectId());
			return true;
		});
		return ids;
	}
	
	/**
	 * @return the leader of this group
	 */
	public abstract PlayerInstance getLeader();
	
	/**
	 * Change the leader of this group to the specified player.
	 * @param leader the player to set as the new leader of this group
	 */
	public abstract void setLeader(PlayerInstance leader);
	
	/**
	 * @return the leader's object ID
	 */
	public int getLeaderObjectId()
	{
		final PlayerInstance leader = getLeader();
		if (leader == null)
		{
			return 0;
		}
		return leader.getObjectId();
	}
	
	/**
	 * Check if a given player is the leader of this group.
	 * @param player the player to check
	 * @return {@code true} if the specified player is the leader of this group, {@code false} otherwise
	 */
	public boolean isLeader(PlayerInstance player)
	{
		return getLeader().getObjectId() == player.getObjectId();
	}
	
	/**
	 * @return the count of all players in this group
	 */
	public int getMemberCount()
	{
		return getMembers().size();
	}
	
	/**
	 * @return the count of all player races in this group
	 */
	public int getRaceCount()
	{
		final List<Race> partyRaces = new ArrayList<>();
		for (PlayerInstance member : getMembers())
		{
			if (!partyRaces.contains(member.getRace()))
			{
				partyRaces.add(member.getRace());
			}
		}
		return partyRaces.size();
	}
	
	/**
	 * @return the level of this group
	 */
	public abstract int getLevel();
	
	/**
	 * Broadcast a packet to every member of this group.
	 * @param packet the packet to broadcast
	 */
	public void broadcastPacket(IClientOutgoingPacket packet)
	{
		forEachMember(m ->
		{
			if (m != null)
			{
				m.sendPacket(packet);
			}
			return true;
		});
	}
	
	/**
	 * Broadcast a system message to this group.
	 * @param message the system message to broadcast
	 */
	public void broadcastMessage(SystemMessageId message)
	{
		broadcastPacket(new SystemMessage(message));
	}
	
	/**
	 * Broadcast a text message to this group.
	 * @param text to broadcast
	 */
	public void broadcastString(String text)
	{
		broadcastPacket(new SystemMessage(text));
	}
	
	public void broadcastCreatureSay(CreatureSay msg, PlayerInstance broadcaster)
	{
		forEachMember(m ->
		{
			if ((m != null) && !BlockList.isBlocked(m, broadcaster))
			{
				m.sendPacket(msg);
			}
			return true;
		});
	}
	
	/**
	 * Check if this group contains a given player.
	 * @param player the player to check
	 * @return {@code true} if this group contains the specified player, {@code false} otherwise
	 */
	public boolean containsPlayer(PlayerInstance player)
	{
		return getMembers().contains(player);
	}
	
	/**
	 * @return a random member of this group
	 */
	public PlayerInstance getRandomPlayer()
	{
		return getMembers().get(Rnd.get(getMembers().size()));
	}
	
	/**
	 * Iterates over the group and executes procedure on each member
	 * @param procedure the prodecure to be executed on each member.<br>
	 *            If executing the procedure on a member returns {@code true}, the loop continues to the next member, otherwise it breaks the loop
	 * @return {@code true} if the procedure executed correctly, {@code false} if the loop was broken prematurely
	 */
	public boolean forEachMember(Function<PlayerInstance, Boolean> procedure)
	{
		for (PlayerInstance player : getMembers())
		{
			if (!procedure.apply(player))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		return (obj instanceof AbstractPlayerGroup) && (getLeaderObjectId() == ((AbstractPlayerGroup) obj).getLeaderObjectId());
	}
}
