
package ai.others;

import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.model.zone.ZoneType;
import org.l2jdd.gameserver.network.serverpackets.ExCastleState;

import ai.AbstractNpcAI;

/**
 * Shows castle side effect in cities.
 * @author Gigi
 * @date 2019-05-14 - [12:47:33]
 */
public class CastleSideEffect extends AbstractNpcAI
{
	private static final int[] ZONE_ID =
	{
		11020, // Giran
		11027, // Gludio
		11028, // Dion
		11029, // Oren
		11031, // aden
		11032, // Goddard
		11033, // Rune
		11034, // Heine
		11035, // Shuttgard
	};
	
	public CastleSideEffect()
	{
		addEnterZoneId(ZONE_ID);
	}
	
	@Override
	public String onEnterZone(Creature character, ZoneType zone)
	{
		if (character.isPlayer())
		{
			for (Castle castle : CastleManager.getInstance().getCastles())
			{
				character.sendPacket(new ExCastleState(castle));
			}
		}
		return super.onEnterZone(character, zone);
	}
	
	public static void main(String[] args)
	{
		new CastleSideEffect();
	}
}