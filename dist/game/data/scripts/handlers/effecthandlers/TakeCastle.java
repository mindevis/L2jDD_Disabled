
package handlers.effecthandlers;

import org.l2jdd.gameserver.enums.CastleSide;
import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Take Castle effect implementation.
 * @author Adry_85, St3eT
 */
public class TakeCastle extends AbstractEffect
{
	private final CastleSide _side;
	
	public TakeCastle(StatSet params)
	{
		_side = params.getEnum("side", CastleSide.class);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (!effector.isPlayer())
		{
			return;
		}
		
		final Castle castle = CastleManager.getInstance().getCastle(effector);
		castle.engrave(effector.getClan(), effected, _side);
	}
}
