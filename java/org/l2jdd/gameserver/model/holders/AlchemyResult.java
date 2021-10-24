
package org.l2jdd.gameserver.model.holders;

import org.l2jdd.gameserver.enums.TryMixCubeResultType;

/**
 * @author UnAfraid
 */
public class AlchemyResult extends ItemHolder
{
	private final TryMixCubeResultType _type;
	
	public AlchemyResult(int id, long count, TryMixCubeResultType type)
	{
		super(id, count);
		_type = type;
	}
	
	public TryMixCubeResultType getType()
	{
		return _type;
	}
}
