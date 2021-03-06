
package org.l2jdd.gameserver.data.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import org.l2jdd.Config;
import org.l2jdd.commons.util.IXmlReader;
import org.l2jdd.gameserver.data.ItemTable;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.holders.ItemHolder;

/**
 * @author Mobius
 */
public class AttendanceRewardData implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(AttendanceRewardData.class.getName());
	private final List<ItemHolder> _rewards = new ArrayList<>();
	private int _rewardsCount = 0;
	
	protected AttendanceRewardData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		if (Config.ENABLE_ATTENDANCE_REWARDS)
		{
			_rewards.clear();
			parseDatapackFile("data/AttendanceRewards.xml");
			_rewardsCount = _rewards.size();
			LOGGER.info(getClass().getSimpleName() + ": Loaded " + _rewardsCount + " rewards.");
		}
		else
		{
			LOGGER.info(getClass().getSimpleName() + ": Disabled.");
		}
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "item", rewardNode ->
		{
			final StatSet set = new StatSet(parseAttributes(rewardNode));
			final int itemId = set.getInt("id");
			final int itemCount = set.getInt("count");
			if (ItemTable.getInstance().getTemplate(itemId) == null)
			{
				LOGGER.info(getClass().getSimpleName() + ": Item with id " + itemId + " does not exist.");
			}
			else
			{
				_rewards.add(new ItemHolder(itemId, itemCount));
			}
		}));
	}
	
	public List<ItemHolder> getRewards()
	{
		return _rewards;
	}
	
	public int getRewardsCount()
	{
		return _rewardsCount;
	}
	
	public static AttendanceRewardData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final AttendanceRewardData INSTANCE = new AttendanceRewardData();
	}
}
