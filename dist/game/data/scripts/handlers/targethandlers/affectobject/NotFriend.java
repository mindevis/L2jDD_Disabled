
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
 * Not Friend affect object implementation. Based on Gracia Final retail tests.<br>
 * Such are considered flagged/karma players (except party/clan/ally). Doesn't matter if in command channel.<br>
 * In arena such are considered clan/ally/command channel (except party).<br>
 * In peace zone such are considered monsters.<br>
 * Monsters consider such all players, npcs (Citizens, Guild Masters, Merchants, Guards, etc. except monsters). Doesn't matter if in peace zone or arena.
 * @author Nik
 */
public class NotFriend implements IAffectObjectHandler
{
	@Override
	public boolean checkAffectedObject(Creature creature, Creature target)
	{
		if (creature == target)
		{
			return false;
		}
		
		final PlayerInstance player = creature.getActingPlayer();
		final PlayerInstance targetPlayer = target.getActingPlayer();
		if ((player != null) && (targetPlayer != null))
		{
			// Same player.
			if (player == targetPlayer)
			{
				return false;
			}
			
			// Peace Zone.
			if (target.isInsidePeaceZone(player) && !player.getAccessLevel().allowPeaceAttack())
			{
				return false;
			}
			
			if (Config.ALT_COMMAND_CHANNEL_FRIENDS)
			{
				final CommandChannel playerCC = player.getCommandChannel();
				final CommandChannel targetCC = targetPlayer.getCommandChannel();
				if ((playerCC != null) && (targetCC != null) && (playerCC.getLeaderObjectId() == targetCC.getLeaderObjectId()))
				{
					return false;
				}
			}
			
			// Party (command channel doesn't make you friends).
			final Party party = player.getParty();
			final Party targetParty = targetPlayer.getParty();
			if ((party != null) && (targetParty != null) && (party.getLeaderObjectId() == targetParty.getLeaderObjectId()))
			{
				return false;
			}
			
			// Events.
			if (player.isOnCustomEvent() && (player.getTeam() == target.getTeam()))
			{
				return false;
			}
			
			// Arena.
			if (creature.isInsideZone(ZoneId.PVP) && target.isInsideZone(ZoneId.PVP))
			{
				return true;
			}
			
			// Duel.
			if (player.isInDuel() && targetPlayer.isInDuel() && (player.getDuelId() == targetPlayer.getDuelId()))
			{
				return true;
			}
			
			// Olympiad.
			if (player.isInOlympiadMode() && targetPlayer.isInOlympiadMode() && (player.getOlympiadGameId() == targetPlayer.getOlympiadGameId()))
			{
				return true;
			}
			
			// Clan.
			final Clan clan = player.getClan();
			final Clan targetClan = targetPlayer.getClan();
			if (clan != null)
			{
				if (clan == targetClan)
				{
					return false;
				}
				
				// War
				if ((targetClan != null) && clan.isAtWarWith(targetClan) && targetClan.isAtWarWith(clan))
				{
					return true;
				}
			}
			
			// Alliance.
			if ((player.getAllyId() != 0) && (player.getAllyId() == targetPlayer.getAllyId()))
			{
				return false;
			}
			
			// Siege.
			if (target.isInsideZone(ZoneId.SIEGE))
			{
				// Players in the same siege side at the same castle are considered friends.
				return !player.isSiegeFriend(targetPlayer);
			}
			
			// At this point summon should be prevented from attacking friendly targets.
			if (creature.isSummon() && (target == creature.getTarget()))
			{
				return true;
			}
			
			// By default any flagged/PK player is considered enemy.
			return (target.getActingPlayer().getPvpFlag() > 0) || (target.getActingPlayer().getReputation() < 0);
		}
		
		return target.isAutoAttackable(creature);
	}
	
	@Override
	public Enum<AffectObject> getAffectObjectType()
	{
		return AffectObject.NOT_FRIEND;
	}
}
