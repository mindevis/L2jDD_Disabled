
package handlers.effecthandlers;

import java.util.HashSet;
import java.util.Set;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.holders.TemplateChanceHolder;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Transformation type effect, which disables attack or use of skills.
 * @author Nik
 */
public class ChangeBody extends AbstractEffect
{
	private final Set<TemplateChanceHolder> _transformations = new HashSet<>();
	
	public ChangeBody(StatSet params)
	{
		for (StatSet item : params.getList("templates", StatSet.class))
		{
			_transformations.add(new TemplateChanceHolder(item.getInt(".templateId"), item.getInt(".minChance"), item.getInt(".maxChance")));
		}
	}
	
	@Override
	public boolean canStart(Creature effector, Creature effected, Skill skill)
	{
		return !effected.isDoor();
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final int chance = Rnd.get(100);
		//@formatter:off
		_transformations.stream()
			.filter(t -> t.calcChance(chance)) // Calculate chance for each transformation.
			.mapToInt(TemplateChanceHolder::getTemplateId)
			.findAny()
			.ifPresent(id -> effected.transform(id, false)); // Transform effected to whatever successful random template without adding skills.
		//@formatter:on
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		effected.stopTransformation(false);
	}
}
