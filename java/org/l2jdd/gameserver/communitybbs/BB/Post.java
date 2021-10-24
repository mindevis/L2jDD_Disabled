
package org.l2jdd.gameserver.communitybbs.BB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.l2jdd.commons.database.DatabaseFactory;
import org.l2jdd.gameserver.communitybbs.Manager.PostBBSManager;

/**
 * @author Maktakien
 */
public class Post
{
	private static final Logger LOGGER = Logger.getLogger(Post.class.getName());
	
	public static class CPost
	{
		public int postId;
		public String postOwner;
		public int postOwnerId;
		public long postDate;
		public int postTopicId;
		public int postForumId;
		public String postTxt;
	}
	
	private final Collection<CPost> _post;
	
	/**
	 * @param postOwner
	 * @param postOwnerId
	 * @param date
	 * @param tid
	 * @param postForumId
	 * @param txt
	 */
	public Post(String postOwner, int postOwnerId, long date, int tid, int postForumId, String txt)
	{
		_post = ConcurrentHashMap.newKeySet();
		final CPost cp = new CPost();
		cp.postId = 0;
		cp.postOwner = postOwner;
		cp.postOwnerId = postOwnerId;
		cp.postDate = date;
		cp.postTopicId = tid;
		cp.postForumId = postForumId;
		cp.postTxt = txt;
		_post.add(cp);
		insertindb(cp);
	}
	
	private void insertindb(CPost cp)
	{
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO posts (post_id,post_owner_name,post_ownerid,post_date,post_topic_id,post_forum_id,post_txt) values (?,?,?,?,?,?,?)"))
		{
			ps.setInt(1, cp.postId);
			ps.setString(2, cp.postOwner);
			ps.setInt(3, cp.postOwnerId);
			ps.setLong(4, cp.postDate);
			ps.setInt(5, cp.postTopicId);
			ps.setInt(6, cp.postForumId);
			ps.setString(7, cp.postTxt);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Error while saving new Post to db " + e.getMessage(), e);
		}
	}
	
	public Post(Topic t)
	{
		_post = ConcurrentHashMap.newKeySet();
		load(t);
	}
	
	public CPost getCPost(int id)
	{
		int i = 0;
		for (CPost cp : _post)
		{
			if (i++ == id)
			{
				return cp;
			}
		}
		return null;
	}
	
	public void deleteme(Topic t)
	{
		PostBBSManager.getInstance().delPostByTopic(t);
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("DELETE FROM posts WHERE post_forum_id=? AND post_topic_id=?"))
		{
			ps.setInt(1, t.getForumID());
			ps.setInt(2, t.getID());
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Error while deleting post: " + e.getMessage(), e);
		}
	}
	
	/**
	 * @param t
	 */
	private void load(Topic t)
	{
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM posts WHERE post_forum_id=? AND post_topic_id=? ORDER BY post_id ASC"))
		{
			ps.setInt(1, t.getForumID());
			ps.setInt(2, t.getID());
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final CPost cp = new CPost();
					cp.postId = rs.getInt("post_id");
					cp.postOwner = rs.getString("post_owner_name");
					cp.postOwnerId = rs.getInt("post_ownerid");
					cp.postDate = rs.getLong("post_date");
					cp.postTopicId = rs.getInt("post_topic_id");
					cp.postForumId = rs.getInt("post_forum_id");
					cp.postTxt = rs.getString("post_txt");
					_post.add(cp);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Data error on Post " + t.getForumID() + "/" + t.getID() + " : " + e.getMessage(), e);
		}
	}
	
	/**
	 * @param i
	 */
	public void updatetxt(int i)
	{
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE posts SET post_txt=? WHERE post_id=? AND post_topic_id=? AND post_forum_id=?"))
		{
			final CPost cp = getCPost(i);
			ps.setString(1, cp.postTxt);
			ps.setInt(2, cp.postId);
			ps.setInt(3, cp.postTopicId);
			ps.setInt(4, cp.postForumId);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Error while saving new Post to db " + e.getMessage(), e);
		}
	}
}
