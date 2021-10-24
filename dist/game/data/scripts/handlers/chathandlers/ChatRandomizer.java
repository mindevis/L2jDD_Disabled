
package handlers.chathandlers;

import org.l2jdd.commons.util.Rnd;

/**
 * @author Mobius
 */
class ChatRandomizer
{
	static String randomize(String text)
	{
		final StringBuilder textOut = new StringBuilder();
		for (char c : text.toCharArray())
		{
			if ((c > 96) && (c < 123))
			{
				textOut.append(Character.toString((char) Rnd.get(96, 123)));
			}
			else if ((c > 64) && (c < 91))
			{
				textOut.append(Character.toString((char) Rnd.get(64, 91)));
			}
			else if ((c == 32) || (c == 44) || (c == 46))
			{
				textOut.append(c);
			}
			else
			{
				textOut.append(Character.toString((char) Rnd.get(47, 64)));
			}
		}
		return textOut.toString();
	}
}
