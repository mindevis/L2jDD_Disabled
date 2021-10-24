
package org.l2jdd.gameserver.model.events.impl.creature.player;

import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.clan.ClanMember;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.impl.IBaseEvent;

/**
 * @author UnAfraid
 */
public class OnPlayerClanDestroy implements IBaseEvent
{
	private final ClanMember _clanMember;
	private final Clan _clan;
	
	public OnPlayerClanDestroy(ClanMember clanMember, Clan clan)
	{
		_clanMember = clanMember;
		_clan = clan;
	}
	
	public ClanMember getClanMember()
	{
		return _clanMember;
	}
	
	public Clan getClan()
	{
		return _clan;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_PLAYER_CLAN_DESTROY;
	}
}
