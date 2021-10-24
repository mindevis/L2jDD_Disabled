
package handlers.targethandlers.affectobject;

import org.l2jdd.gameserver.handler.IAffectObjectHandler;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.targets.AffectObject;

/**
 * @author Nik
 */
public class Clan implements IAffectObjectHandler
{
	@Override
	public boolean checkAffectedObject(Creature creature, Creature target)
	{
		if (creature == target)
		{
			return true;
		}
		
		final PlayerInstance player = creature.getActingPlayer();
		if (player != null)
		{
			final org.l2jdd.gameserver.model.clan.Clan clan = player.getClan();
			if (clan != null)
			{
				return clan == target.getClan();
			}
		}
		else if (creature.isNpc() && target.isNpc())
		{
			return ((Npc) creature).isInMyClan(((Npc) target));
		}
		
		return false;
	}
	
	@Override
	public Enum<AffectObject> getAffectObjectType()
	{
		return AffectObject.CLAN;
	}
}
