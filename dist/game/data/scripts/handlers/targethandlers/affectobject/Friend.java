
package handlers.targethandlers.affectobject;

import org.l2jdd.Config;
import org.l2jdd.gameserver.handler.IAffectObjectHandler;
import org.l2jdd.gameserver.model.CommandChannel;
import org.l2jdd.gameserver.model.Party;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.skills.targets.AffectObject;
import org.l2jdd.gameserver.model.zone.ZoneId;

/**
 * @author Nik
 */
public class Friend implements IAffectObjectHandler
{
	@Override
	public boolean checkAffectedObject(Creature creature, Creature target)
	{
		if (creature == target)
		{
			return true;
		}
		
		final PlayerInstance player = creature.getActingPlayer();
		final PlayerInstance targetPlayer = target.getActingPlayer();
		if (player != null)
		{
			if (targetPlayer != null)
			{
				// Same player.
				if (player == targetPlayer)
				{
					return true;
				}
				
				if (Config.ALT_COMMAND_CHANNEL_FRIENDS)
				{
					final CommandChannel playerCC = player.getCommandChannel();
					final CommandChannel targetCC = targetPlayer.getCommandChannel();
					if ((playerCC != null) && (targetCC != null) && (playerCC.getLeaderObjectId() == targetCC.getLeaderObjectId()))
					{
						return true;
					}
				}
				
				// Party (command channel doesn't make you friends).
				final Party party = player.getParty();
				final Party targetParty = targetPlayer.getParty();
				if ((party != null) && (targetParty != null) && (party.getLeaderObjectId() == targetParty.getLeaderObjectId()))
				{
					return true;
				}
				
				// Arena.
				if (creature.isInsideZone(ZoneId.PVP) && target.isInsideZone(ZoneId.PVP))
				{
					return false;
				}
				
				// Duel.
				if (player.isInDuel() && targetPlayer.isInDuel() && (player.getDuelId() == targetPlayer.getDuelId()))
				{
					return false;
				}
				
				// Olympiad.
				if (player.isInOlympiadMode() && targetPlayer.isInOlympiadMode() && (player.getOlympiadGameId() == targetPlayer.getOlympiadGameId()))
				{
					return false;
				}
				
				// Clan.
				final Clan clan = player.getClan();
				final Clan targetClan = targetPlayer.getClan();
				if (clan != null)
				{
					if (clan == targetClan)
					{
						return true;
					}
					
					// War
					if ((targetClan != null) && clan.isAtWarWith(targetClan) && targetClan.isAtWarWith(clan))
					{
						return false;
					}
				}
				
				// Alliance.
				if ((player.getAllyId() != 0) && (player.getAllyId() == targetPlayer.getAllyId()))
				{
					return true;
				}
				
				// Siege.
				if (target.isInsideZone(ZoneId.SIEGE))
				{
					// Players in the same siege side at the same castle are considered friends.
					return player.isSiegeFriend(targetPlayer);
				}
				
				// By default any neutral non-flagged player is considered a friend.
				return (target.getActingPlayer().getPvpFlag() == 0) && (target.getActingPlayer().getReputation() >= 0);
			}
			
			// By default any npc that isnt mob is considered friend.
			return !target.isMonster() && !target.isAutoAttackable(player);
		}
		
		return !target.isAutoAttackable(creature);
	}
	
	@Override
	public Enum<AffectObject> getAffectObjectType()
	{
		return AffectObject.FRIEND;
	}
}
