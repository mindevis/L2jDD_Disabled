
package handlers.effecthandlers;

import org.l2jdd.gameserver.GameTimeController;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author Mobius
 */
public class HitAtNight extends AbstractStatEffect
{
	public HitAtNight(StatSet params)
	{
		super(params, Stat.HIT_AT_NIGHT);
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		GameTimeController.getInstance().addShadowSenseCharacter(effected);
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		GameTimeController.getInstance().removeShadowSenseCharacter(effected);
	}
}
