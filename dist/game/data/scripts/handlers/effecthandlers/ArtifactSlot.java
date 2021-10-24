
package handlers.effecthandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.stats.Stat;

/**
 * @author hlwrave
 */
public class ArtifactSlot extends AbstractStatAddEffect
{
	public ArtifactSlot(StatSet params)
	{
		super(params, Stat.ARTIFACT_SLOTS);
	}
}
