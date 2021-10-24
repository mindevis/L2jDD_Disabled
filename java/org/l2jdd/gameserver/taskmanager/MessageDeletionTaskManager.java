
package org.l2jdd.gameserver.taskmanager;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.commons.util.Chronos;
import org.l2jdd.gameserver.instancemanager.MailManager;
import org.l2jdd.gameserver.model.Message;
import org.l2jdd.gameserver.model.World;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 */
public class MessageDeletionTaskManager
{
	private static final Map<Integer, Long> PENDING_MESSAGES = new ConcurrentHashMap<>();
	private static boolean _working = false;
	
	public MessageDeletionTaskManager()
	{
		ThreadPool.scheduleAtFixedRate(() ->
		{
			if (_working)
			{
				return;
			}
			_working = true;
			
			Integer msgId;
			Message msg;
			final long time = Chronos.currentTimeMillis();
			for (Entry<Integer, Long> entry : PENDING_MESSAGES.entrySet())
			{
				if (time > entry.getValue().longValue())
				{
					msgId = entry.getKey();
					msg = MailManager.getInstance().getMessage(msgId.intValue());
					if (msg == null)
					{
						PENDING_MESSAGES.remove(msgId);
						return;
					}
					
					if (msg.hasAttachments())
					{
						final PlayerInstance sender = World.getInstance().getPlayer(msg.getSenderId());
						if (sender != null)
						{
							msg.getAttachments().returnToWh(sender.getWarehouse());
							sender.sendPacket(SystemMessageId.THE_MAIL_WAS_RETURNED_DUE_TO_THE_EXCEEDED_WAITING_TIME);
						}
						else
						{
							msg.getAttachments().returnToWh(null);
						}
						msg.getAttachments().deleteMe();
						msg.removeAttachments();
						
						final PlayerInstance receiver = World.getInstance().getPlayer(msg.getReceiverId());
						if (receiver != null)
						{
							receiver.sendPacket(new SystemMessage(SystemMessageId.THE_MAIL_WAS_RETURNED_DUE_TO_THE_EXCEEDED_WAITING_TIME));
						}
					}
					
					MailManager.getInstance().deleteMessageInDb(msgId.intValue());
					PENDING_MESSAGES.remove(msgId);
				}
			}
			
			_working = false;
		}, 10000, 10000);
	}
	
	public void add(int msgId, long deletionTime)
	{
		PENDING_MESSAGES.put(msgId, deletionTime);
	}
	
	public static MessageDeletionTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final MessageDeletionTaskManager INSTANCE = new MessageDeletionTaskManager();
	}
}
