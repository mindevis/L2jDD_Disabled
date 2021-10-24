
package org.l2jdd.gameserver.model.zone.type;

import org.l2jdd.gameserver.instancemanager.CastleManager;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.siege.Castle;
import org.l2jdd.gameserver.model.zone.ZoneId;
import org.l2jdd.gameserver.model.zone.ZoneType;

/**
 * Tax zone type.
 * @author malyelfik
 */
public class TaxZone extends ZoneType
{
	private int _domainId;
	private Castle _castle;
	
	public TaxZone(int id)
	{
		super(id);
	}
	
	@Override
	public void setParameter(String name, String value)
	{
		if (name.equalsIgnoreCase("domainId"))
		{
			_domainId = Integer.parseInt(value);
		}
		else
		{
			super.setParameter(name, value);
		}
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
		creature.setInsideZone(ZoneId.TAX, true);
		if (creature.isNpc())
		{
			((Npc) creature).setTaxZone(this);
		}
	}
	
	@Override
	protected void onExit(Creature creature)
	{
		creature.setInsideZone(ZoneId.TAX, false);
		if (creature.isNpc())
		{
			((Npc) creature).setTaxZone(null);
		}
	}
	
	/**
	 * Gets castle associated with tax zone.
	 * @return instance of {@link Castle} if found otherwise {@code null}
	 */
	public Castle getCastle()
	{
		// Lazy loading is used because zone is loaded before residence
		if (_castle == null)
		{
			_castle = CastleManager.getInstance().getCastleById(_domainId);
		}
		return _castle;
	}
}
