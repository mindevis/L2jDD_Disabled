
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class DragonWeaponDefence extends AbstractStatPercentEffect
{
	public DragonWeaponDefence(StatSet params)
	{
		super(params, Stat.DRAGON_WEAPON_DEFENCE);
	}
}
