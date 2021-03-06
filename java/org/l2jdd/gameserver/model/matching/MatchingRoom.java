
package org.l2jdd.gameserver.model.matching;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jdd.gameserver.enums.MatchingMemberType;
import org.l2jdd.gameserver.enums.MatchingRoomType;
import org.l2jdd.gameserver.enums.UserInfoType;
import org.l2jdd.gameserver.instancemanager.MapRegionManager;
import org.l2jdd.gameserver.instancemanager.MatchingRoomManager;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.interfaces.IIdentifiable;

/**
 * @author Sdw
 */
public abstract class MatchingRoom implements IIdentifiable
{
	private final int _id;
	private String _title;
	private int _loot;
	private int _minLevel;
	private int _maxLevel;
	private int _maxCount;
	private Set<PlayerInstance> _members;
	private PlayerInstance _leader;
	
	public MatchingRoom(String title, int loot, int minLevel, int maxLevel, int maxmem, PlayerInstance leader)
	{
		_id = MatchingRoomManager.getInstance().addMatchingRoom(this);
		_title = title;
		_loot = loot;
		_minLevel = minLevel;
		_maxLevel = maxLevel;
		_maxCount = maxmem;
		_leader = leader;
		addMember(_leader);
		onRoomCreation(leader);
	}
	
	public Set<PlayerInstance> getMembers()
	{
		if (_members == null)
		{
			synchronized (this)
			{
				if (_members == null)
				{
					_members = ConcurrentHashMap.newKeySet(1);
				}
			}
		}
		return _members;
	}
	
	public void addMember(PlayerInstance player)
	{
		if ((player.getLevel() < _minLevel) || (player.getLevel() > _maxLevel) || ((_members != null) && (_members.size() >= _maxCount)))
		{
			notifyInvalidCondition(player);
			return;
		}
		
		getMembers().add(player);
		MatchingRoomManager.getInstance().removeFromWaitingList(player);
		notifyNewMember(player);
		player.setMatchingRoom(this);
		player.broadcastUserInfo(UserInfoType.CLAN);
	}
	
	public void deleteMember(PlayerInstance player, boolean kicked)
	{
		boolean leaderChanged = false;
		if (player == _leader)
		{
			if (getMembers().isEmpty())
			{
				MatchingRoomManager.getInstance().removeMatchingRoom(this);
			}
			else
			{
				final Iterator<PlayerInstance> iter = getMembers().iterator();
				if (iter.hasNext())
				{
					_leader = iter.next();
					iter.remove();
					leaderChanged = true;
				}
			}
		}
		else
		{
			getMembers().remove(player);
		}
		
		player.setMatchingRoom(null);
		player.broadcastUserInfo(UserInfoType.CLAN);
		MatchingRoomManager.getInstance().addToWaitingList(player);
		
		notifyRemovedMember(player, kicked, leaderChanged);
	}
	
	@Override
	public int getId()
	{
		return _id;
	}
	
	public int getLootType()
	{
		return _loot;
	}
	
	public int getMinLevel()
	{
		return _minLevel;
	}
	
	public int getMaxLevel()
	{
		return _maxLevel;
	}
	
	public int getLocation()
	{
		return MapRegionManager.getInstance().getBBs(_leader.getLocation());
	}
	
	public int getMembersCount()
	{
		return _members == null ? 0 : _members.size();
	}
	
	public int getMaxMembers()
	{
		return _maxCount;
	}
	
	public String getTitle()
	{
		return _title;
	}
	
	public PlayerInstance getLeader()
	{
		return _leader;
	}
	
	public boolean isLeader(PlayerInstance player)
	{
		return player == _leader;
	}
	
	public void setMinLevel(int minLevel)
	{
		_minLevel = minLevel;
	}
	
	public void setMaxLevel(int maxLevel)
	{
		_maxLevel = maxLevel;
	}
	
	public void setLootType(int loot)
	{
		_loot = loot;
	}
	
	public void setMaxMembers(int maxCount)
	{
		_maxCount = maxCount;
	}
	
	public void setTitle(String title)
	{
		_title = title;
	}
	
	protected abstract void onRoomCreation(PlayerInstance player);
	
	protected abstract void notifyInvalidCondition(PlayerInstance player);
	
	protected abstract void notifyNewMember(PlayerInstance player);
	
	protected abstract void notifyRemovedMember(PlayerInstance player, boolean kicked, boolean leaderChanged);
	
	public abstract void disbandRoom();
	
	public abstract MatchingRoomType getRoomType();
	
	public abstract MatchingMemberType getMemberType(PlayerInstance player);
}
