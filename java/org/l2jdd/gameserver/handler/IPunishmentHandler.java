
package org.l2jdd.gameserver.handler;

import java.util.logging.Logger;

import org.l2jdd.gameserver.model.punishment.PunishmentTask;
import org.l2jdd.gameserver.model.punishment.PunishmentType;

/**
 * @author UnAfraid
 */
public interface IPunishmentHandler
{
	Logger LOGGER = Logger.getLogger(IPunishmentHandler.class.getName());
	
	void onStart(PunishmentTask task);
	
	void onEnd(PunishmentTask task);
	
	PunishmentType getType();
}
