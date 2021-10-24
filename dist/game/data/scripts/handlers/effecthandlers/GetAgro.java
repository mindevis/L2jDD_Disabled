
package handlers.effecthandlers;

import java.util.Set;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.templates.NpcTemplate;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Get Agro effect implementation.
 * @author Adry_85, Mobius
 */
public class GetAgro extends AbstractEffect
{
	public GetAgro(StatSet params)
	{
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.AGGRESSION;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if ((effected != null) && effected.isAttackable())
		{
			effected.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, effector);
			
			// Monsters from the same clan should assist.
			final NpcTemplate template = ((Attackable) effected).getTemplate();
			final Set<Integer> clans = template.getClans();
			if (clans != null)
			{
				World.getInstance().forEachVisibleObjectInRange(effected, Attackable.class, template.getClanHelpRange(), nearby ->
				{
					if (!nearby.isMovementDisabled() && nearby.getTemplate().isClan(clans))
					{
						nearby.addDamageHate(effector, 1, 200);
						nearby.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, effector);
					}
				});
			}
		}
	}
}
