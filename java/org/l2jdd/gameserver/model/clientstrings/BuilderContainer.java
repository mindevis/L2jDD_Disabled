
package org.l2jdd.gameserver.model.clientstrings;

/**
 * @author Forsaiken
 */
final class BuilderContainer extends Builder
{
	private final Builder[] _builders;
	
	BuilderContainer(Builder[] builders)
	{
		_builders = builders;
	}
	
	@Override
	public String toString(Object param)
	{
		return toString(new Object[]
		{
			param
		});
	}
	
	@Override
	public String toString(Object... params)
	{
		final int buildersLength = _builders.length;
		final int paramsLength = params.length;
		final String[] builds = new String[buildersLength];
		Builder builder;
		String build;
		int i;
		int paramIndex;
		int buildTextLen = 0;
		if (paramsLength != 0)
		{
			for (i = buildersLength; i-- > 0;)
			{
				builder = _builders[i];
				paramIndex = builder.getIndex();
				build = (paramIndex != -1) && (paramIndex < paramsLength) ? builder.toString(params[paramIndex]) : builder.toString();
				buildTextLen += build.length();
				builds[i] = build;
			}
		}
		else
		{
			for (i = buildersLength; i-- > 0;)
			{
				build = _builders[i].toString();
				buildTextLen += build.length();
				builds[i] = build;
			}
		}
		
		final FastStringBuilder fsb = new FastStringBuilder(buildTextLen);
		for (i = 0; i < buildersLength; i++)
		{
			fsb.append(builds[i]);
		}
		return fsb.toString();
	}
	
	@Override
	public int getIndex()
	{
		return -1;
	}
}