
package handlers.effecthandlers;

import java.util.logging.Logger;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.data.xml.CubicData;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.actor.templates.CubicTemplate;
import org.l2jdd.gameserver.model.cubic.CubicInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.stats.Stat;
import org.l2jdd.gameserver.network.serverpackets.ExUserInfoCubic;

/**
 * Summon Cubic effect implementation.
 * @author Zoey76
 */
public class SummonCubic extends AbstractEffect
{
	private static final Logger LOGGER = Logger.getLogger(SummonCubic.class.getName());
	
	private final int _cubicId;
	private final int _cubicLvl;
	
	public SummonCubic(StatSet params)
	{
		_cubicId = params.getInt("cubicId", -1);
		_cubicLvl = params.getInt("cubicLvl", 0);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (!effected.isPlayer() || effected.isAlikeDead() || effected.getActingPlayer().inObserverMode())
		{
			return;
		}
		
		if (_cubicId < 0)
		{
			LOGGER.warning(SummonCubic.class.getSimpleName() + ": Invalid Cubic ID:" + _cubicId + " in skill ID: " + skill.getId());
			return;
		}
		
		final PlayerInstance player = effected.getActingPlayer();
		if (player.inObserverMode() || player.isMounted())
		{
			return;
		}
		
		// If cubic is already present, it's replaced.
		final CubicInstance cubic = player.getCubicById(_cubicId);
		if (cubic != null)
		{
			if (cubic.getTemplate().getLevel() > _cubicLvl)
			{
				// What do we do in such case?
				return;
			}
			
			cubic.deactivate();
		}
		else
		{
			// If maximum amount is reached, random cubic is removed.
			// Players with no mastery can have only one cubic.
			final double allowedCubicCount = player.getStat().getValue(Stat.MAX_CUBIC, 1);
			
			// Extra cubics are removed, one by one, randomly.
			final int currentCubicCount = player.getCubics().size();
			if (currentCubicCount >= allowedCubicCount)
			{
				player.getCubics().values().stream().skip((int) (currentCubicCount * Rnd.nextDouble())).findAny().get().deactivate();
			}
		}
		
		final CubicTemplate template = CubicData.getInstance().getCubicTemplate(_cubicId, _cubicLvl);
		if (template == null)
		{
			LOGGER.warning("Attempting to summon cubic without existing template id: " + _cubicId + " level: " + _cubicLvl);
			return;
		}
		
		// Adding a new cubic.
		player.addCubic(new CubicInstance(player, effector.getActingPlayer(), template));
		player.sendPacket(new ExUserInfoCubic(player));
		player.broadcastCharInfo();
	}
}
