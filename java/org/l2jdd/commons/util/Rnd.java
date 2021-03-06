
package org.l2jdd.commons.util;

import java.util.Random;

/**
 * @author Mobius
 */
public class Rnd
{
	/**
	 * Thread-specific random number generator.<br>
	 * Each is seeded with the thread ID, so the sequence of random numbers are unique between threads.
	 */
	// Java 1.8
	// private static ThreadLocal<Random> RANDOM = new ThreadLocal<Random>()
	// Java 10
	private static ThreadLocal<Random> RANDOM = new ThreadLocal<>()
	{
		@Override
		protected Random initialValue()
		{
			return new Random(System.nanoTime() + Thread.currentThread().getId());
		}
	};
	
	/**
	 * @return a random boolean value.
	 */
	public static boolean nextBoolean()
	{
		return RANDOM.get().nextBoolean();
	}
	
	/**
	 * Generates random bytes and places them into a user-supplied byte array. The number of random bytes produced is equal to the length of the byte array.
	 * @param bytes the byte array to fill with random bytes.
	 */
	public static void nextBytes(byte[] bytes)
	{
		RANDOM.get().nextBytes(bytes);
	}
	
	/**
	 * @param bound (int)
	 * @return a random int value between zero (inclusive) and the specified bound (exclusive).
	 */
	public static int get(int bound)
	{
		return (int) (RANDOM.get().nextDouble() * bound);
	}
	
	/**
	 * @param origin (int)
	 * @param bound (int)
	 * @return a random int value between the specified origin (inclusive) and the specified bound (inclusive).
	 */
	public static int get(int origin, int bound)
	{
		if (origin == bound)
		{
			return origin;
		}
		return origin + (int) (((bound - origin) + 1) * RANDOM.get().nextDouble());
	}
	
	/**
	 * @return a random int value.
	 */
	public static int nextInt()
	{
		return RANDOM.get().nextInt();
	}
	
	/**
	 * @param bound (long)
	 * @return a random long value between zero (inclusive) and the specified bound (exclusive).
	 */
	public static long get(long bound)
	{
		return (long) (RANDOM.get().nextDouble() * bound);
	}
	
	/**
	 * @param origin (long)
	 * @param bound (long)
	 * @return a random long value between the specified origin (inclusive) and the specified bound (inclusive).
	 */
	public static long get(long origin, long bound)
	{
		if (origin == bound)
		{
			return origin;
		}
		return origin + (long) (((bound - origin) + 1) * RANDOM.get().nextDouble());
	}
	
	/**
	 * @return a random long value.
	 */
	public static long nextLong()
	{
		return RANDOM.get().nextLong();
	}
	
	/**
	 * @param bound (double)
	 * @return a random double value between zero (inclusive) and the specified bound (exclusive).
	 */
	public static double get(double bound)
	{
		return RANDOM.get().nextDouble() * bound;
	}
	
	/**
	 * @param origin (double)
	 * @param bound (double)
	 * @return a random double value between the specified origin (inclusive) and the specified bound (inclusive).
	 */
	public static double get(double origin, double bound)
	{
		if (origin == bound)
		{
			return origin;
		}
		return origin + (((bound - origin) + 1) * RANDOM.get().nextDouble());
	}
	
	/**
	 * @return a random double value between zero (inclusive) and one (exclusive).
	 */
	public static double nextDouble()
	{
		return RANDOM.get().nextDouble();
	}
	
	/**
	 * @return the next random, Gaussian ("normally") distributed double value with mean 0.0 and standard deviation 1.0 from this random number generator's sequence.
	 */
	public static double nextGaussian()
	{
		return RANDOM.get().nextGaussian();
	}
}
