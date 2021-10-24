
package org.l2jdd.gameserver.model.events.impl.creature.player;

import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.clan.ClanMember;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.IBaseEvent;

/**
 * @author UnAfraid
 */
public class OnPlayerClanLeaderChange implements IBaseEvent
{
	private final ClanMember _oldLeader;
	private final ClanMember _newLeader;
	private final Clan _clan;
	
	public OnPlayerClanLeaderChange(ClanMember oldLeader, ClanMember newLeader, Clan clan)
	{
		_oldLeader = oldLeader;
		_newLeader = newLeader;
		_clan = clan;
	}
	
	public ClanMember getOldLeader()
	{
		return _oldLeader;
	}
	
	public ClanMember getNewLeader()
	{
		return _newLeader;
	}
	
	public Clan getClan()
	{
		return _clan;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_PLAYER_CLAN_LEADER_CHANGE;
	}
}
