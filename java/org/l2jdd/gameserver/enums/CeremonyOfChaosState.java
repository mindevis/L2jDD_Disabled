
package org.l2jdd.gameserver.enums;

import org.l2jdd.gameserver.model.eventengine.IEventState;

/**
 * @author Sdw
 */
public enum CeremonyOfChaosState implements IEventState
{
	SCHEDULED,
	REGISTRATION,
	PREPARING_FOR_TELEPORT,
	PREPARING_FOR_FIGHT,
	RUNNING
}
